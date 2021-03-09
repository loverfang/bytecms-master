package net.bytecms.web.iterator;

public class CoinClassifyIterator implements Iterator {

    private CoinClassify coinClassify;

    private int i=0;

    CoinClassifyIterator(CoinClassify coinClassify) {
        this.coinClassify = coinClassify;
    }

    @Override
    public boolean hasNext() {
        return i<coinClassify.size();
    }

    @Override
    public Object next() {
        Object object= coinClassify.getCoin(i);
        i++;
        return object;
    }

    @Override
    public Iterator sort() {
        coinClassify.sort();
        return this;
    }
}
