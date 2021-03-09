package net.bytecms.web.adapt;

public interface FiveCoinGen {
    /**
     * 生成一个光滑无痕的硬币钢镚
     */
    public abstract Coin genCoin();
    /**
     * 给硬币印制面值
     */
    public abstract Coin printCoinFaceValue(Coin coin);
    /**
     * 给硬币印制制作日期
     */
    public abstract Coin printCoinGenDate(Coin coin);


    public Coin getMyCoin();
}
