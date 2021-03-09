package net.bytecms.core.iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanPropertyBox implements GenPropertyIterator {

    private Object bean;

    List<Field> propertys;

    public BeanPropertyBox(Object bean) {
        this.bean = bean;
        this.propertys=getProperty(bean);
    }

    Object getBean(){
        return this.bean;
    }

    List<Field> getProperty(Object bean){
        List<Field> fields=new ArrayList<>(16);
        fields.addAll(Arrays.asList(bean.getClass().getDeclaredFields()));
        Class<?> suCl=bean.getClass().getSuperclass();
        while (suCl!=null){
            fields.addAll(Arrays.asList(suCl.getDeclaredFields()));
            suCl=suCl.getSuperclass();
        }
        return fields;
    }

    public int size(){
        return propertys.size();
    }

    public Field getField(int index){
        return propertys.get(index);
    }

    @Override
    public ThinkIterator iterator(Class cls)  {
      try {
          Constructor<?> cons = cls.getConstructor(BeanPropertyBox.class);
          return (ThinkIterator) cons.newInstance(this);
      }catch (Exception e){
          throw  new RuntimeException("迭代器获取失败:"+e.getMessage());
      }
    }
}
