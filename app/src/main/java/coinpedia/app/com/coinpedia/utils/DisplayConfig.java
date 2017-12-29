package coinpedia.app.com.coinpedia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by senzec on 11/8/17.
 */

public class DisplayConfig {

    Context _context;
    DisplayConfig(Context _context)
    {
        this._context = _context;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }




}
