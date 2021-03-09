package net.bytecms.web.iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoinClassify implements GenIterator {

    private List<Coin> coins;

    CoinClassify() {
        this.coins = new ArrayList<>(16);
    }

    public void addCoin(Coin coin) {
        coins.add(coin);
    }

    public int size(){
        return coins.size();
    }

    public Coin getCoin(int index){
        return coins.get(index);
    }

    public void sort(){
        Collections.sort(coins, new Comparator<Coin>() {
            @Override
            public int compare(Coin c1, Coin c2) {
                if(c1.getUnit().equals(c2.getUnit())){
                    return 0;
                }else if(c1.getUnit().equals("角") && c2.getUnit().equals("元")){
                    return -1;
                }else if(c1.getUnit().equals("元") && c2.getUnit().equals("角")){
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public Iterator genIterator() {
        return new CoinClassifyIterator(this);
    }
}
