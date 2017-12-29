package coinpedia.app.com.coinpedia;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.AttributedCharacterIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;

import coinpedia.app.com.coinpedia.cache.ImagesCache;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.ui.CurrencySelectorListActivity;
import coinpedia.app.com.coinpedia.ui.SplashActivity;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;

/**
 * Implementation of App Widget functionality.
 */
public class CoinpediaAppWidget extends AppWidgetProvider {

    ArrayAdapter<String> adapter;
    ArrayList<ApiToViewModel> frontDbAL = new ArrayList<ApiToViewModel>();
    Context context;
    String[] Currencyname,shortName,percentage,imageName,image;
    Double[] price;
    private static ArrayList<ApiToViewModel> widgetList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.coinpedia_app_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent configIntent = new Intent(context, SplashActivity.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

        views.setOnClickPendingIntent(R.id.ll_app_widget, configPendingIntent);

        Intent configIntent1 = new Intent(context, CurrencySelectorListActivity.class);

        PendingIntent configPendingIntent1 = PendingIntent.getActivity(context, 0, configIntent1, 0);

        views.setOnClickPendingIntent(R.id.iv_setting, configPendingIntent1);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.context = context;
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            if(!isIntentData) {
                frontDbAL = getCoinDataFromCache();
                size = frontDbAL.size();
            }
            setOutputFrontList();

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.coinpedia_app_widget);

            views.setTextViewText(R.id.tv_coin_shortName1,shortName[0]);
            views.setTextViewText(R.id.tv_coin_shortName2,shortName[1]);
            views.setTextViewText(R.id.tv_coin_shortName3,shortName[2]);
            views.setTextViewText(R.id.tv_coin_shortName4,shortName[3]);

            AppWidgetTarget appWidgetTarget1 = new AppWidgetTarget( context, views, R.id.iv_coin_logo1, appWidgetIds );
            AppWidgetTarget appWidgetTarget2 = new AppWidgetTarget( context, views, R.id.iv_coin_logo2, appWidgetIds );
            AppWidgetTarget appWidgetTarget3 = new AppWidgetTarget( context, views, R.id.iv_coin_logo3, appWidgetIds );
            AppWidgetTarget appWidgetTarget4 = new AppWidgetTarget( context, views, R.id.iv_coin_logo4, appWidgetIds );

            loadImageOnWidget(appWidgetTarget1,image[0]);
            loadImageOnWidget(appWidgetTarget2,image[1]);
            loadImageOnWidget(appWidgetTarget3,image[2]);
            loadImageOnWidget(appWidgetTarget4,image[3]);

            views.setTextViewText(R.id.tv_coinPrice1,"$ "+convertDecimalPrice(price[0]));
            views.setTextViewText(R.id.tv_coinPrice2,"$ "+convertDecimalPrice(price[1]));
            views.setTextViewText(R.id.tv_coinPrice3,"$ "+convertDecimalPrice(price[2]));
            views.setTextViewText(R.id.tv_coinPrice4,"$ "+convertDecimalPrice(price[3]));

            views.setTextViewText(R.id.tv_coinPercent_status1,convertDecimalPerc(percentage[0].toString())+"%");
            views.setTextViewText(R.id.tv_coinPercent_status2,convertDecimalPerc(percentage[1].toString())+"%");
            views.setTextViewText(R.id.tv_coinPercent_status3,convertDecimalPerc(percentage[2].toString())+"%");
            views.setTextViewText(R.id.tv_coinPercent_status4,convertDecimalPerc(percentage[3].toString())+"%");
            setPercentageColor(views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    boolean isIntentData=false;
    int size=0;
    ArrayList<ApiToViewModel>apiToViewModels;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {

            ArrayList<ApiToViewModel>apiToViewModels= (ArrayList<ApiToViewModel>) intent.getSerializableExtra("selected_currency");
            System.out.println("apiToViewModels "+apiToViewModels);
            if(apiToViewModels!=null){
                isIntentData=true;
                frontDbAL=apiToViewModels;
                size=frontDbAL.size();
                this.context=context;
                getCoinDataFromCache();
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName provider = new ComponentName(context.getPackageName(), getClass().getName());
            int ids[] = appWidgetManager.getAppWidgetIds(provider);

            intent.getExtras();

            onUpdate(context, appWidgetManager, ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public ArrayList<ApiToViewModel> getCoinDataFromCache()
    {
        apiToViewModels=new ArrayList<>();
        ArrayList<ApiToViewModel> arrayListProduct = new ArrayList<>();
        String jsonResponse = new SharedPrefClass(context).getJsonResponse();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for(int i = 0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String shorts = jsonObject.getString("short");
                String longs = jsonObject.getString("long");
                String perc = jsonObject.getString("perc");
                Double price = jsonObject.getDouble("price");
                Double volume = jsonObject.getDouble("volume");

                ApiToViewModel productPojo = new ApiToViewModel(longs, shorts, perc, price, volume);
                arrayListProduct.add(productPojo);

                if(apiToViewModels.size()<4)apiToViewModels.add(productPojo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayListProduct;
    }

    public void setOutputFrontList()
    {
        Currencyname = new String[4];
        shortName = new String[4];
        percentage = new String[4];
        price = new Double[4];
        imageName = new String[4];
        image = new String[4];

        if(size>4) {
            size=4;
            for (int i = 0; i < size; i++) {
                Currencyname[i] = frontDbAL.get(i).getLongs().toString();
                shortName[i] = frontDbAL.get(i).getShorts().toString();
                percentage[i] = frontDbAL.get(i).getPerc().toString();
                price[i] = frontDbAL.get(i).getPrice();
                imageName[i] = frontDbAL.get(i).getLongs().toString().trim().replaceAll("\\s+", "");
                image[i] = "https://coincap.io/images/coins/" + imageName[i] + ".png";
            }
        }else{
            for (int i = 0; i < size; i++) {
                Currencyname[i] = frontDbAL.get(i).getLongs().toString();
                shortName[i] = frontDbAL.get(i).getShorts().toString();
                percentage[i] = frontDbAL.get(i).getPerc().toString();
                price[i] = frontDbAL.get(i).getPrice();
                imageName[i] = frontDbAL.get(i).getLongs().toString().trim().replaceAll("\\s+", "");
                image[i] = "https://coincap.io/images/coins/" + imageName[i] + ".png";
            }

            for (int i = size; i < 4; i++) {
                Currencyname[i] = apiToViewModels.get(i).getLongs().toString();
                shortName[i] = apiToViewModels.get(i).getShorts().toString();
                percentage[i] = apiToViewModels.get(i).getPerc().toString();
                price[i] = apiToViewModels.get(i).getPrice();
                imageName[i] = apiToViewModels.get(i).getLongs().toString().trim().replaceAll("\\s+", "");
                image[i] = "https://coincap.io/images/coins/" + imageName[i] + ".png";
            }
        }

    }

    public String convertDecimalPerc(String value)
    {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(Double.parseDouble(value));
    }

    public String convertDecimalPrice(Double priceValue)
    {
        String convertedPrice = "";
        if(priceValue<1)
        {DecimalFormat df = new DecimalFormat("####0.0000000");
            convertedPrice = df.format(priceValue);}
        else if(priceValue>=1) {
            DecimalFormat df = new DecimalFormat("####0.00");
            convertedPrice = df.format(priceValue);
        }
        return convertedPrice;
    }


    private void setPercentageColor(RemoteViews views){

        for(int i=0;i<4;i++) {
            if (Double.parseDouble(percentage[i].toString()) < 0) {
                if(i==0) {
                    views.setTextColor(R.id.tv_coinPercent_status1, Color.parseColor("#FF4444"));
                }
                if(i==1) {
                    views.setTextColor(R.id.tv_coinPercent_status2, Color.parseColor("#FF4444"));
                }
                if(i==2) {
                    views.setTextColor(R.id.tv_coinPercent_status3, Color.parseColor("#FF4444"));
                }
                if(i==3) {
                    views.setTextColor(R.id.tv_coinPercent_status4, Color.parseColor("#FF4444"));
                }
            } else {
                if(i==0) {
                    views.setTextColor(R.id.tv_coinPercent_status1, Color.parseColor("#006400"));
                }
                if(i==1) {
                    views.setTextColor(R.id.tv_coinPercent_status2, Color.parseColor("#006400"));
                }
                if(i==2) {
                    views.setTextColor(R.id.tv_coinPercent_status3, Color.parseColor("#006400"));
                }
                if(i==3) {
                    views.setTextColor(R.id.tv_coinPercent_status4, Color.parseColor("#006400"));
                }
            }
        }

    }


    private void loadImageOnWidget(AppWidgetTarget appWidgetTarget,String imgUrl){
        Glide
                .with( context.getApplicationContext() ) // safer!
                .load( imgUrl )
                .asBitmap()
                .into( appWidgetTarget );
    }

    public static void setDuyuruHaberList(ArrayList<ApiToViewModel> data){
        CoinpediaAppWidget.widgetList = data;
    }
}

