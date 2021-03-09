package net.bytecms.freemark.corelibs.observer;


import net.bytecms.freemark.corelibs.notify.AbstractNotify;

/**
 * 抽象观察者
 */
public interface Observer {

    /**
     * 接收到通知开始执行
     * @param data
     */
     void update(ObserverData data);



     void update(ObserverData data, AbstractNotify notifyRes);

}
