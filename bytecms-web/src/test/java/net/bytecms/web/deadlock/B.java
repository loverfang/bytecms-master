package net.bytecms.web.deadlock;

public class B {
    public synchronized void last() {
        System.out.println("进入b类的 last 方法");
    }

    public synchronized void bar(A a){
        System.out.println("获取当前线程名称："+Thread.currentThread().getName()+"" +
                "进入B实例的bar方法");// setp2
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("获取当前线程名称："+Thread.currentThread().getName()+"" +
                "准备调用A实例的 last 方法");// setp4
        a.last();
    }
}
