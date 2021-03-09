package net.bytecms.web.adapt;

import org.junit.Test;

public class TestDemo {


    @Test
    public void tTemp(){
        FiveCoinGen five=new FiveCoinGenImpl(new Coin("5角","2020-01-21"));
        five.getMyCoin();

        OneCoinGen one=new OneJiaoAdapt(new Coin("1角","2020-01-01"));
        one.getMyCoin();
    }

}
