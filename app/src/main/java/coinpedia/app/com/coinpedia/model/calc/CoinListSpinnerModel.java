package coinpedia.app.com.coinpedia.model.calc;

/**
 * Created by senzec on 9/8/17.
 */

public class CoinListSpinnerModel {

    public CoinListSpinnerModel(String coinName, double coinPrice) {
        this.coinName = coinName;
        this.coinPrice = coinPrice;
    }

    public String getCoinName() {
        return coinName;
    }

    public double getCoinPrice() {
        return coinPrice;
    }

    String coinName;
    double coinPrice;
}
