package coinpedia.app.com.coinpedia.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.product_tour.PrefConstants;
import coinpedia.app.com.coinpedia.product_tour.ProductTourActivity;
import coinpedia.app.com.coinpedia.product_tour.SAppUtil;
import coinpedia.app.com.coinpedia.utils.ConnectivityManagerClass;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.FormatterClass;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import coinpedia.app.com.coinpedia.utils.ProgressClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import coinpedia.app.com.coinpedia.cache.UpdaterDbServiceManager;

public class SplashActivity extends AppCompatActivity {

    ImageView logoPedia, textPedia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoPedia = (ImageView)findViewById(R.id.idPediaLogo);
        textPedia = (ImageView)findViewById(R.id.idPediaText);
        if(new SharedPrefClass(SplashActivity.this).getProductTour().equals("yes") ) {
            Navigatior.getClassInstance().navigateToActivity(SplashActivity.this,HomeActivity.class);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

      //  startService(new Intent(this, UpdaterDbServiceManager.class));

        logoPedia.getLayoutParams().width = getWidth()*50/100;
        logoPedia.getLayoutParams().height = getHeight()*50/100;

        textPedia.getLayoutParams().width = getWidth()*75/100;
        textPedia.getLayoutParams().height = getHeight()*15/100;

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottomtotop);
        textPedia.startAnimation(animation);
     //   navigate();
        isDataAvailable();
    }

    public void isDataAvailable(){

        if(new SharedPrefClass(SplashActivity.this).isCoinDataAvailable() == true){
            navigate();
            startService(new Intent(this, UpdaterDbServiceManager.class));
        }else {
            if(ConnectivityManagerClass.getInstance().isNetworkAvailable(SplashActivity.this) == true)
            {
                new BackgroundTask(SplashActivity.this){
                    @Override
                    protected void onPostExecute(Boolean status) {
                        super.onPostExecute(status);
                        if(status == true)
                        {
                            new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("All done!")
                                    .setContentText("Excited to help you.!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            navigate();
                                            startService(new Intent(SplashActivity.this, UpdaterDbServiceManager.class));
                                            sweetAlertDialog.dismiss();
                                        }
                                    })
                                    .show();

                        }else {
                            isDataAvailable();
                        }

                    }
                }.execute();
            }else {

                new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Oops!")
                        .setContentText("Network Failed! Check your Internet")
                        .setCustomImage(R.drawable.ic_disconnected)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }

        }
    }
    // Timer using Handler
    private final int SPLASH_TIME = 1500;
    private void navigate() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(new SharedPrefClass(SplashActivity.this).getProductTour().equals("yes") ) {
                            Navigatior.getClassInstance().navigateToActivity(SplashActivity.this,HomeActivity.class);

                        }else {
                            new SharedPrefClass(SplashActivity.this).setProductTour();
                            checkShowTutorial();
                            startActivity(new Intent(SplashActivity.this, ProductTourActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                }, SPLASH_TIME);
    }

    private void checkShowTutorial(){
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if(currentVersionCode>oldVersionCode){
            startActivity(new Intent(SplashActivity.this,ProductTourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }
    private int getWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return width;
    }
    private int getHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    class BackgroundTask extends AsyncTask<Void, Void, Boolean>
    {
        Context mContext;
        BackgroundTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressClass.getProgressInstance().startProgress(SplashActivity.this);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Boolean status = saveCoinData();
            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
       //     pDialog.dismiss();
            ProgressClass.getProgressInstance().stopProgress();
        }

        public Boolean  saveCoinData()
        {

            String result;
            String inputLine;
            Boolean dataSyncStatus = false;

            try {
                URL myUrl = new URL(Consts.coincapFullURL);
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();
                InputStreamReader streamReader = new  InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
                if(result != null)
                {
                    new SharedPrefClass(mContext).setDate(new FormatterClass().simpleDate());
                    new SharedPrefClass(mContext).setJsonResponse(result);
                    dataSyncStatus = true;
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return dataSyncStatus;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    finish();
    }
}
