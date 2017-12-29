package coinpedia.app.com.coinpedia.utils;

import java.util.Comparator;

import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;

/**
 * Created by senzec on 21/7/17.
 */

    public class ComparatorClass implements Comparator<ApiToViewModel> {
        public int compare(ApiToViewModel p1, ApiToViewModel p2) {
            // FOR DESCENDING ORDER
            if (p1.getPrice() < p2.getPrice()) return 1;
            if (p1.getPrice() > p2.getPrice()) return -1;
            // FOR ASCENDING ORDER
            /*if (p1.getPrice() < p2.getPrice()) return -1;
            if (p1.getPrice() > p2.getPrice()) return 1;
            */
            return 0;
        }
    }