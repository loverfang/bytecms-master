package net.bytecms.web.adapt;

/**
 * 1 角硬币
 */
public class OneJiaoAdapt1 implements OneCoinGen {

    private FiveCoinGen fiveCoinGen;

    OneJiaoAdapt1(FiveCoinGen fiveCoinGen){
        this.fiveCoinGen=fiveCoinGen;
    }

    @Override
    public Coin genCoin() {
        return null;
    }

    @Override
    public Coin printCoinFaceValue(Coin coin) {
        return null;
    }

    @Override
    public Coin printCoinGenDate(Coin coin) {
        return null;
    }

    @Override
    public Coin getMyCoin() {
        return fiveCoinGen.getMyCoin();
    }
}
