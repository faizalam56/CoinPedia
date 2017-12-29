package coinpedia.app.com.coinpedia.model.front;

import java.util.Comparator;

/**
 * Created by senzec on 18/7/17.
 */

public class DbToViewModel {

    public DbToViewModel(String longs, String shorts, String perc, Double price, Double volume) {
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

    String longs, shorts, perc;
    Double price;
    Double volume ;

    class ComparatorClass2 implements Comparator<DbToViewModel> {
        public int compare(DbToViewModel p1, DbToViewModel p2) {
            if (p1.getPrice() < p2.getPrice()) return -1;
            if (p1.getPrice() > p2.getPrice()) return 1;
            return 0;
        }
    }

}
