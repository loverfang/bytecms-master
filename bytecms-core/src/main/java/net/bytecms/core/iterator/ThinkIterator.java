package net.bytecms.core.iterator;

public interface ThinkIterator {

    /**
     * 是否存在下一个属性
     * @return
     */
    boolean hasNext();

    /**
     * 获取下一个属性
     * @return
     */
    Object next();
}
