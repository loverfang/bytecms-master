package net.bytecms.web.tempmethod;

/**
 * 5 角硬币
 */
public class FiveJiaoCoin extends  AbsCoinGen {
    private Coin coin;
    FiveJiaoCoin(Coin coin){
        this.coin=coin;
    }
    @Override
    public Coin genCoin() {
        return coin;
    }
    @Override
    public Coin printCoinFaceValue(Coin coin) {
        //coin.setFaceValue("5角");
        System.out.println("===开始打印硬币面值===:"+coin.getFaceValue());
        return coin;
    }
    @Override
    public Coin printCoinGenDate(Coin coin) {
        // coin.setDate("2020-01-22");
        System.out.println("===开始打印硬币日期===:"+coin.getDate());
        return coin;
    }
}
