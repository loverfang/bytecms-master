package net.bytecms.web.iterator;

/**
 * 抽象迭代器类，定义了几个接口
 */
public interface Iterator {

    /**
     * 是否还有硬币
     * @return
     */
    boolean hasNext();

    /**
     * 获取当前硬币，并调整指针到下一个
     * @return
     */
    Object next();


    /**
     * 硬币排序
     * @return
     */
    Iterator sort();
}
