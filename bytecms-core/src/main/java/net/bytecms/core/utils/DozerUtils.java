package net.bytecms.core.utils;

import net.bytecms.core.model.BaseModel;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DozerUtils
 * @Author: LG
 * @Date: 2019/5/16 10:41
 * @Version: 1.0
 **/
public class DozerUtils {

    /**
     * 封装dozer处理集合的方法：List<D> --> List<T>
     */
    public static <T extends BaseModel, D > List<T> D2TList(final Mapper mapper, List<D> sourceList, Class<T> targetObjectClass) {
        List<T> targetList = new ArrayList<T>();
        for (D d : sourceList) {
            targetList.add(mapper.map(d, targetObjectClass));
        }
        return targetList;
    }


    /**
     * 封装dozer处理集合的方法：List<T> --> List<D>
     */


    public static <D , T extends BaseModel> List<D> T2DList(Mapper mapper, List<T> sourceList, Class<D> targetObjectClass) {
        List<D> targetList = new ArrayList<>();
        for (T t : sourceList) {
            targetList.add(mapper.map(t, targetObjectClass));
        }
        return targetList;
    }
}
