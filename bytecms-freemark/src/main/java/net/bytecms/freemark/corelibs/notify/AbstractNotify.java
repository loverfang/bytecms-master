package net.bytecms.freemark.corelibs.notify;

import net.bytecms.freemark.corelibs.observer.Observer;
import net.bytecms.freemark.corelibs.observer.ObserverData;
import net.bytecms.freemark.corelibs.subject.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * 通知组件
 */
@Slf4j
@Component
public abstract class AbstractNotify {

    protected void notify(Subject subject, Observer observer, ObserverData observerData, boolean onceCallBack){
        synchronized (subject){
            Instant start = Instant.now();
            subject.registerObserver(observer);
            subject.setObserverData(observerData);
            try {
                subject.callBack(observerData);
                if(onceCallBack){
                    subject.notifyObserver(this);
                }else{
                    subject.notifyObserver();
                }
                notifyAllSuccess(observer,observerData);
                Instant end = Instant.now();
                long timeElapsed = Duration.between(start, end).toMillis(); // 单位为毫秒
                log.info("执行【"+observerData.getObserverAction().getAction()+"】用时："+timeElapsed+"毫秒");
            }catch (Exception e){
                notifyError(e,observer,observerData);
                log.error(e.getMessage());
                log.error("文章静态化失败,等待发布时再次尝试");
            }finally {
                subject.removeObserver(observer);
            }
        }
    }


    protected void notify(Subject subject, List<Observer> observers, ObserverData observerData, boolean onceCallBack){
        synchronized (subject){
            Instant start = Instant.now();
            subject.registerObserver(observers);
            subject.setObserverData(observerData);
            try {
                subject.callBack(observerData);
                if(onceCallBack){
                    subject.notifyObserver(this);
                }else{
                    subject.notifyObserver();
                }
                notifyAllSuccess(observers,observerData);
                Instant end = Instant.now();
                long timeElapsed = Duration.between(start, end).toMillis(); // 单位为毫秒
                log.info("执行【"+observerData.getObserverAction().getAction()+"】用时："+timeElapsed+"毫秒");
            }catch (Exception e){
                notifyError(e,observers,observerData);
                log.error(e.getMessage());
                log.error("文章静态化失败,等待发布时再次尝试");
            }finally {
                subject.removeAllObserver();
            }
        }
    }


    /**
     * 所有通知全部成功
     * @param observer
     * @param observerData
     */
    public abstract void notifyAllSuccess(Observer observer, ObserverData observerData);




    /**
     * 所有通知全部成功
     * @param observers
     * @param observerData
     */
    public abstract void notifyAllSuccess(List<Observer> observers, ObserverData observerData);


    /**
     * 一次通知成功
     * @param resMap
     */
    public abstract void notifyOnceSuccess(Map<String,Object> resMap);

    /**
     * 通知失败异常/业务处理
     * @param e
     * @param observer
     * @param observerData
     */
    public abstract void notifyError(Exception e,Observer observer, ObserverData observerData);


    /**
     * 通知失败异常/业务处理
     * @param e
     * @param observers
     * @param observerData
     */
    public abstract void notifyError(Exception e,List<Observer> observers, ObserverData observerData);
}
