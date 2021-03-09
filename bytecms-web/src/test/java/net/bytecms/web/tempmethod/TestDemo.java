package net.bytecms.web.tempmethod;

import org.junit.Test;

public class TestDemo {


    @Test
    public void tTemp(){
        AbsCoinGen five=new FiveJiaoCoin(new Coin("5角","2020-01-21"));
        AbsCoinGen one=new OneJiaoCoin(new Coin("1角","2020-01-01"));
        five.getMyCoin();
        one.getMyCoin();
    }

}
