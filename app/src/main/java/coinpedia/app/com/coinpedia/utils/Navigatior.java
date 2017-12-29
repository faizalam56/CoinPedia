package coinpedia.app.com.coinpedia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import coinpedia.app.com.coinpedia.R;

/**
 * Created by Divakar on 6/19/2017.
 */

public class Navigatior {
    private static Navigatior instanceClass = null;

    private Navigatior()
    {

    }

    public static Navigatior getClassInstance()
    {
        if( instanceClass == null)
        {   instanceClass = new Navigatior();    }

        return instanceClass;
    }


    public void navigateToActivity(Activity activity, Class<?> toClass) {
        Intent intent = new Intent(activity, toClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void navigateToActivityWithData(Activity activity, Class<?> toClass, String data, int num) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtra("data", data);
        intent.putExtra("num", num);
        activity.startActivity(intent);
     //   activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    public void navigateToActivityWithBundleData(Activity activity, Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
     //   activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //CONTEXT

    public void navigateToActivityContext(Context activity, Class<?> toClass) {
        Intent intent = new Intent(activity, toClass);
        activity.startActivity(intent);
      //  activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navigateToActivityWithDataContext(Context activity, Class<?> toClass, String data, int num) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtra("data", data);
        intent.putExtra("num", num);
        activity.startActivity(intent);
     //   activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    public void navigateToActivityWithBundleDataContext(Context activity, Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
     //   activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
