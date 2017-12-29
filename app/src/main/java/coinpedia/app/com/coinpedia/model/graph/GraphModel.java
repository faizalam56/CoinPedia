package coinpedia.app.com.coinpedia.model.graph;

/**
 * Created by senzec on 8/8/17.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphModel {

    @SerializedName("price")
    @Expose
    private List<List<Float>> price = null;

    public List<List<Float>> getPrice() {
        return price;
    }

    public void setPrice(List<List<Float>> price) {
        this.price = price;
    }

}