package coinpedia.app.com.coinpedia.model.front;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by senzec on 18/7/17.
 */

public class ApiToViewModel implements Serializable{

    public ApiToViewModel(String longs, String shorts, String perc, Double price, Double volume) {
        this.longs = longs;
        this.shorts = shorts;
        this.perc = perc;
        this.price = price;
        this.volume = volume;
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs;
    }

    public String getShorts() {
        return shorts;
    }

    public void setShorts(String shorts) {
        this.shorts = shorts;
    }

    public String getPerc() {
        return perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }

    public Double getPrice() {
        return price;
    }

    public Double  getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public void setPrice(Double price) {
        this.price = price;
    }



    public String getChkStatus() {
        return chkStatus;
    }

    public void setChkStatus(String chkStatus) {
        this.chkStatus = chkStatus;
    }
    String longs, shorts, perc;
    Double price;
    Double volume ;
    String chkStatus = "";



}
