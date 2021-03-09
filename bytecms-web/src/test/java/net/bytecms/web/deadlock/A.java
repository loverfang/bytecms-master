package net.bytecms.web.deadlock;

public class A {

    public synchronized void f(B b){
        System.out.println("获取当前线程名称："+Thread.currentThread().getName()+"" +
                "进入A实例的f方法"); // setp1
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("获取当前线程名称："+Thread.currentThread().getName()+"" +
                "准备调用B实例的 last 方法");// setp3
        b.last();

    }

    public synchronized void last() {
        System.out.println("进入A类的 last 方法");
    }

}
