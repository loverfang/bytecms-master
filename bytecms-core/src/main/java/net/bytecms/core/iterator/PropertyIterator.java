package net.bytecms.core.iterator;

import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.utils.Checker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class PropertyIterator implements ThinkIterator {

    public BeanPropertyBox beanPropertyBox;

    private int i=0;

    Map<String,Object> param=new LinkedHashMap<>(16);

    public PropertyIterator(BeanPropertyBox beanPropertyBox){
        this.beanPropertyBox=beanPropertyBox;
    }

    @Override
    public boolean hasNext() {
        return i<beanPropertyBox.size();
    }

    @Override
    public Object next() {
        param.clear();
        Object obj=beanPropertyBox.getBean();
        Field field= beanPropertyBox.getField(i);
        field.setAccessible(true);
        DirectMark directMark=field.getAnnotation(DirectMark.class);
        if(Checker.BeNotNull(directMark)){
            try {
                Object val = field.get(obj);
                if(Checker.BeNotNull(val)){
                    if(val instanceof Map)
                        param.putAll((Map) val);
                    else
                        param.put(Checker.BeNotBlank(directMark.name()) ? directMark.name():field.getName(), val);
                }
            }catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        i++;
        return param;
    }
}
