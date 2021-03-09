package net.bytecms.web.adapt;

/**
 * 1 角硬币
 */
public class OneJiaoAdapt extends FiveCoinGenImpl implements OneCoinGen {

    private OneCoinGen oneCoinGen;

    OneJiaoAdapt(Coin coin) {
        super(coin);
    }

    @Override
    public Coin genCoin() {
        return super.genCoin();
    }

    @Override
    public Coin printCoinFaceValue(Coin coin) {
        return super.printCoinFaceValue(coin);
    }

    @Override
    public Coin printCoinGenDate(Coin coin) {
        return super.printCoinGenDate(coin);
    }
}
