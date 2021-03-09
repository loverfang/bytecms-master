package net.bytecms.core.iterator;
import net.bytecms.core.annotation.SolrMark;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.Checker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class SolrIterator implements ThinkIterator {

    public BeanPropertyBox beanPropertyBox;

    private int i=0;

    Map<String,Object> param=new LinkedHashMap<>(16);

    public SolrIterator(BeanPropertyBox beanPropertyBox){
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
        SolrMark solrMark=field.getAnnotation(SolrMark.class);
        if(Checker.BeNotNull(solrMark)){
            try {
                Object val = field.get(obj);
                if(Checker.BeNotNull(val)){
                    String name = Checker.BeNotBlank(solrMark.name()) ? solrMark.name():field.getName();
                    if(solrMark.expand()){
                        name= Constants.EXTEND_FIELD_PREFIX+name;
                    }
                    if(val instanceof Map){
                        Map valMap=(Map) val;
                        Iterator<Map.Entry<String, Object>> it = valMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, Object> data = it.next();
                            param.put(Constants.EXTEND_FIELD_PREFIX + data.getKey(), data.getValue());
                        }
                    }
                    else
                        param.put(name, val);
                }
            }catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        i++;
        return param;
    }
}
