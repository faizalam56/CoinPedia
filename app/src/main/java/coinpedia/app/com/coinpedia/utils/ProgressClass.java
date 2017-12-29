package coinpedia.app.com.coinpedia.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.product_tour.ProductTourActivity;
import coinpedia.app.com.coinpedia.ui.HomeActivity;
import coinpedia.app.com.coinpedia.ui.SplashActivity;

/**
 * Created by senzec on 28/7/17.
 */

public class ProgressClass {

    private static ProgressClass progressClass = null;
    private ProgressDialog progressDialog;
    private SweetAlertDialog pDialog, pDialogText;
    private int i = -1;
    private final int SPLASH_TIME = 20000;

    private ProgressClass()
    {

    }
    public static ProgressClass getProgressInstance()
    {
        if(progressClass == null)
        {
            progressClass = new ProgressClass();
        }
        return progressClass;
    }

     public void startProgress(Context mContext)
    {
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(true);
        new CountDownTimer(1500 * 7, 1500) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i){
                    case 0:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.error_stroke_color));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.red_btn_bg_pressed_color));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.warning_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
            //    pDialog.dismiss();
               /* pDialog.setTitleText("Success!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);*/
            }
        }.start();

    }
    public void startProgressTextChanged(Context mContext, String customText)
    {
        pDialogText = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(customText);
        pDialogText.show();
        pDialogText.setCancelable(true);
        new CountDownTimer(1500 * 7, 1500) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i){
                    case 0:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.error_stroke_color));
                        break;
                    case 2:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.red_btn_bg_pressed_color));
                        break;
                    case 4:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 6:
                        pDialogText.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.warning_stroke_color));
                        break;
                }
            }



            public void onFinish() {
                i = -1;
                //    pDialog.dismiss();
               /* pDialog.setTitleText("Success!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);*/
            }
        }.start();

    }

    public void startTimeProgressbar(Context mContext)
    {
        startProgressTextChanged(mContext, "Loading");
        CountDownTimer counter = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilDone){
//                startProgressTextChanged(mContext, "Loading");
            }

            public void onFinish() {
                stopProgressText();
                startProgressTextChanged(mContext, "Custom Text");

            }
        }.start();

    }



    public void alertProgress()
    {
        pDialog.setTitleText("Success!")
                .setConfirmText("OK")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    }
    public void stopProgress()
    {
     //   progressDialog.cancel();
        pDialog.dismiss();
    }
    public void stopProgressText()
    {
     //   progressDialog.cancel();
        for(int i = 0; i<10; i++) {
         if(pDialogText != null)
         { pDialogText.dismiss(); }
        }
    }



}
