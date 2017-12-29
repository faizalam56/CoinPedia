package coinpedia.app.com.coinpedia.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Divakar on 7/6/2017.
 */

public class SharedPrefClass {

    Context _context;
    private static final String TAG = SharedPrefClass.class.getSimpleName();
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    private static final String PREF_NAME = "ProplinPref";
    private static final String LOGIN_KEY = "key";
    private static final String SELECTED_FRONT_VIEW_SHORTS= "selected_view_shorts";
    private static final String CURRENT_COIN = "current_coin_name";
    private static final String UDID= "udid";
    private static final String PRODUCT_SHOWN= "product_shown";
    private static final String JSON_RESPONSE = "coinpedia_json";
    private static final String JSON_DATE= "coinpedia_json_date";
    private static final String CHAT_INIT_STATUS= "chat_init_status";
    private static final String JSON_MONTH_RESPONSE = "month_json_response";


    public SharedPrefClass(Context _context)
    {
        this._context = _context;
    }
    public void createLoginSession(String key)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, key);
        editor.commit();
    }
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<>();
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         sharedPreferences.getString(LOGIN_KEY, null);

        user.put(LOGIN_KEY, sharedPreferences.getString(LOGIN_KEY, null));
        return user;
    }
    public String getSimpleKey()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOGIN_KEY, null);
    }
    public boolean IS_LOGIN()
    {
        boolean staus = false;

        try {
            String value_key = "";
            HashMap<String, String> user = new SharedPrefClass(_context).getUserDetails();
            for (Map.Entry me : user.entrySet()) {
                System.out.println(me.getKey() + " : " + me.getValue());
                value_key = me.getValue().toString();
                if (value_key.length() > 0) {
                    staus = true;
                }
            }
        }catch (NullPointerException npe)
        {
            Log.e(TAG, "#Error : "+npe, npe);
        }
        return staus;
    }
    public void logoutUser(){
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    public void setSelectedFront(String shortsName)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(SELECTED_FRONT_VIEW_SHORTS, shortsName);
        editor.commit();
    }
    public String getSelectedFront()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(SELECTED_FRONT_VIEW_SHORTS, "");
    }
 public void setCurrentCoin(String coinName)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(CURRENT_COIN, coinName);
        editor.commit();
    }
    public String getCurrentCoin()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(CURRENT_COIN, null);
    }
    public void setUdId(String key)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(UDID, key);
        editor.commit();
    }
    public String getUdId()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(UDID, null);
    }
    public void setProductTour()
    {
        String str = "yes";
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(PRODUCT_SHOWN, str);
        editor.commit();
    }
    public String getProductTour()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(PRODUCT_SHOWN, "no");
    }
    public void setJsonResponse(String json)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(JSON_RESPONSE, json);
        editor.commit();
    }
    public String getJsonResponse()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(JSON_RESPONSE, "no");
    }
    public boolean isCoinDataAvailable()
    {
        boolean isAvailable = true;
        if(getJsonResponse().equalsIgnoreCase("no"))
        {
         isAvailable = false;
        }
        return isAvailable;
    }
    public void setDate(String dateStr)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(JSON_DATE, dateStr);
        editor.commit();
    }
    public String getDate()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(JSON_DATE, "Not Available");
    }
    public void setChatInit()
    {
        Boolean checked_status = true;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(CHAT_INIT_STATUS, checked_status);
        editor.commit();
    }
    public Boolean getChatInit()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CHAT_INIT_STATUS, false);
    }

    public void setMonthResponse(String json)
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(JSON_MONTH_RESPONSE, json);
        editor.commit();
    }
    public String getMonthResponse()
    {
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(JSON_MONTH_RESPONSE, "no");
    }

}
