package net.bytecms.freemark.corelibs.subject;

import net.bytecms.freemark.corelibs.notify.AbstractNotify;
import net.bytecms.freemark.corelibs.observer.Observer;
import net.bytecms.freemark.corelibs.observer.ObserverData;

import java.util.List;

/**
 * 抽象被观察者(观察对象)
 */
public interface Subject {

    /**
     * 添加观察者
     * @param o
     */
    void registerObserver(Observer o);


    /**
     * 添加观察者
     * @param os
     */
    void registerObserver(List<Observer> os);

    /**
     * 删除观察者
     * @param o
     */
     void removeObserver(Observer o);


    /**
     * 删除所有观察者
     */
    void removeAllObserver();


    /**
     * 通知观察者
     */
     void notifyObserver();


    /**
     * 通知观察者并实时监控通知结果
     */
    void notifyObserver(AbstractNotify notifyRes);


    void setObserverData(ObserverData observerData);


    void callBack(ObserverData observerData);

}
