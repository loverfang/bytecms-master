package net.bytecms.web.iterator;

import org.junit.Test;

public class TestDemo {


    @Test
    public void tIterator(){
        CoinClassify coinClassify=new CoinClassify();
        coinClassify.addCoin(new Coin(1,"角"));
        coinClassify.addCoin(new Coin(5,"角"));
        coinClassify.addCoin(new Coin(1,"元"));
        coinClassify.addCoin(new Coin(1,"角"));
        coinClassify.addCoin(new Coin(1,"角"));
        coinClassify.addCoin(new Coin(1,"元"));
        coinClassify.addCoin(new Coin(5,"角"));
        coinClassify.addCoin(new Coin(1,"元"));
        coinClassify.addCoin(new Coin(5,"角"));
        Iterator iterator=coinClassify.genIterator().sort();
        // Iterator iterator=coinClassify.genIterator();
        while (iterator.hasNext()){
            Coin coin= (Coin) iterator.next();
            System.out.println(coin.getFaceValue()+":"+coin.getUnit());
        }
    }

}
