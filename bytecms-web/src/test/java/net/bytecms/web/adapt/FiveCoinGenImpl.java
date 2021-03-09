package net.bytecms.web.adapt;

/**
 * 5 角硬币
 */
public class FiveCoinGenImpl implements FiveCoinGen {
    private Coin coin;
    FiveCoinGenImpl(Coin coin){
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
        System.out.println("===开始打印硬币花边===");
        System.out.println("===开始等级制作人员信息===");
        System.out.println("===开始记录当前操作时间等等业务操作模拟很复杂的操作等===");
        return coin;
    }
    @Override
    public Coin printCoinGenDate(Coin coin) {
        // coin.setDate("2020-01-22");
        System.out.println("===开始打印硬币日期===:"+coin.getDate());
        System.out.println("===开始校验日期是否正确===");
        System.out.println("===开始计算当日最大打印个数===");
        System.out.println("===开始记录当前操作时间等等业务操作模拟很复杂的操作等===");
        System.out.println("===========================");
        return coin;
    }

    @Override
    public Coin getMyCoin() {
        Coin coin=genCoin();
        printCoinFaceValue(coin);
        printCoinGenDate(coin);
        return coin;
    }
}
