package coinpedia.app.com.coinpedia.model.coindetail;

/**
 * Created by senzec on 22/7/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinDetailModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("coinresponse")
    @Expose
    private List<Coinresponse> coinresponse = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Coinresponse> getCoinresponse() {
        return coinresponse;
    }

    public void setCoinresponse(List<Coinresponse> coinresponse) {
        this.coinresponse = coinresponse;
    }

}

