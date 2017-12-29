package coinpedia.app.com.coinpedia.product_tour;

import android.content.Context;
import android.content.pm.PackageManager;

public class SAppUtil {
    public static int getAppVersionCode(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        return version;
    }
}
