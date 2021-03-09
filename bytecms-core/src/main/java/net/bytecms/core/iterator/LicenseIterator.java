package net.bytecms.core.iterator;

import net.bytecms.core.annotation.LicenseMark;
import net.bytecms.core.utils.Checker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class LicenseIterator implements ThinkIterator {

    public BeanPropertyBox beanPropertyBox;

    private int i=0;

    Map<String,Object> param=new LinkedHashMap<>(16);

    public LicenseIterator(BeanPropertyBox beanPropertyBox){
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
        LicenseMark licenseMark=field.getAnnotation(LicenseMark.class);
        if(Checker.BeNotNull(licenseMark)){
            try {
                Object val = field.get(obj);
                if(Checker.BeNotNull(val)){
                    param.put(field.getName(), val);
                }
            }catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        i++;
        return param;
    }
}
