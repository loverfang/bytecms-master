package net.bytecms.web.deadlock;

public class DeadLock implements Runnable {
    A a=new A();
    B b = new B();
    public void init(){
        Thread.currentThread().setName("主线程");
        a.f(b);
        System.out.println("进入主线线程之后");
    }
    @Override
    public void run() {
        Thread.currentThread().setName("副线程");
        b.bar(a);
        System.out.println("进入副线线程之后");
    }
    public static void main(String[] args){
        DeadLock deadLock=new DeadLock();
        new Thread(deadLock).start();
        deadLock.init();
    }
}
