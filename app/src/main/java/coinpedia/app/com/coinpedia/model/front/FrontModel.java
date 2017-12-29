package coinpedia.app.com.coinpedia.model.front;

/**
 * Created by senzec on 18/7/17.
 */



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FrontModel {

    @SerializedName("published")
    @Expose
    private Boolean published;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("vwapDataBTC")
    @Expose
    private Double vwapDataBTC;
    @SerializedName("vwapData")
    @Expose
    private String vwapData;
    @SerializedName("cap24hrChange")
    @Expose
    private String cap24hrChange;
    @SerializedName("short")
    @Expose
    private String _short;
    @SerializedName("position24")
    @Expose
    private String position24;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("perc")
    @Expose
    private String perc;
    @SerializedName("supply")
    @Expose
    private String supply;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("usdVolume")
    @Expose
    private String usdVolume;
    @SerializedName("mktcap")
    @Expose
    private Object mktcap;
    @SerializedName("cap24hrChangePercent")
    @Expose
    private String cap24hrChangePercent;
    @SerializedName("capPercent")
    @Expose
    private String capPercent;
    @SerializedName("isBuy")
    @Expose
    private Boolean isBuy;
    @SerializedName("shapeshift")
    @Expose
    private Boolean shapeshift;
    @SerializedName("delta")
    @Expose
    private String delta;

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getVwapDataBTC() {
        return vwapDataBTC;
    }

    public void setVwapDataBTC(Double vwapDataBTC) {
        this.vwapDataBTC = vwapDataBTC;
    }

    public String getVwapData() {
        return vwapData;
    }

    public void setVwapData(String vwapData) {
        this.vwapData = vwapData;
    }

    public String getCap24hrChange() {
        return cap24hrChange;
    }

    public void setCap24hrChange(String cap24hrChange) {
        this.cap24hrChange = cap24hrChange;
    }

    public String getShort() {
        return _short;
    }

    public void setShort(String _short) {
        this._short = _short;
    }

    public String getPosition24() {
        return position24;
    }

    public void setPosition24(String position24) {
        this.position24 = position24;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getPerc() {
        return perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUsdVolume() {
        return usdVolume;
    }

    public void setUsdVolume(String usdVolume) {
        this.usdVolume = usdVolume;
    }

    public Object getMktcap() {
        return mktcap;
    }

    public void setMktcap(Object mktcap) {
        this.mktcap = mktcap;
    }

    public String getCap24hrChangePercent() {
        return cap24hrChangePercent;
    }

    public void setCap24hrChangePercent(String cap24hrChangePercent) {
        this.cap24hrChangePercent = cap24hrChangePercent;
    }

    public String getCapPercent() {
        return capPercent;
    }

    public void setCapPercent(String capPercent) {
        this.capPercent = capPercent;
    }

    public Boolean getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
    }

    public Boolean getShapeshift() {
        return shapeshift;
    }

    public void setShapeshift(Boolean shapeshift) {
        this.shapeshift = shapeshift;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

}