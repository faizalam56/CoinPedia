package coinpedia.app.com.coinpedia.coindetail;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import coinpedia.app.com.coinpedia.cache.DownloadImageTask;
import coinpedia.app.com.coinpedia.cache.ImagesCache;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.ui.GraphActivity;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.model.front.FrontModel;
import coinpedia.app.com.coinpedia.parseApi.ApiClient;
import coinpedia.app.com.coinpedia.parseApi.ApiInterface;
import coinpedia.app.com.coinpedia.parseApi.OkHttpClass;
import coinpedia.app.com.coinpedia.ui.SplashActivity;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.FormatterClass;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import coinpedia.app.com.coinpedia.utils.ProgressClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoinDetailMainFreagment extends Fragment implements View.OnClickListener {

    // INIT
    @BindView(R.id.idCoinIcon)
    ImageView ivCoinIcon;
    @BindView(R.id.idCoinName)
    TextView tvCoinName;

    @BindView(R.id.idTimeTV)
    TextView timeTV;
    TextView tvPrice, tvPerc, tvOpen, tvHigh, tvLow, tvMktCap, tvChange, tvCurrency;
    Button btnGoGraph;

    @BindView(R.id.idChangeInfo)
    TextView changeInfoArrow;

    // DECLARE
    private static final String TAG = CoinDetailMainFreagment.class.getSimpleName();
    View detailView;
    String selected_shorts;
    //int selectedFront;

    ArrayList<ApiToViewModel> coinDetailList;
    private ApiInterface apiInterface;
    Double open, high, low;
    Typeface font;

    SmoothProgressBar spbHorizontal;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        detailView = inflater.inflate(R.layout.fragment_coindetail_main, container, false);
        ButterKnife.bind(this, detailView);

        selected_shorts = new SharedPrefClass(detailView.getContext()).getSelectedFront();
        //selectedFront = new SharedPrefClass(detailView.getContext()).getSelectedFront();
       // parseIOApi("NOFILTER");

        tvPrice = (TextView) detailView.findViewById(R.id.idPriceTV);
        tvPerc = (TextView) detailView.findViewById(R.id.idPercTV);
        tvOpen = (TextView) detailView.findViewById(R.id.idOpenTV);
        tvHigh = (TextView) detailView.findViewById(R.id.idHighTV);
        tvLow = (TextView) detailView.findViewById(R.id.idLowTV);
        tvMktCap = (TextView) detailView.findViewById(R.id.idMktCapTV);
        tvChange = (TextView) detailView.findViewById(R.id.idChangeTV);
        tvCurrency = (TextView) detailView.findViewById(R.id.idCurrencyRelTV);
        btnGoGraph = (Button) detailView.findViewById(R.id.idGoGraph);
        spbHorizontal = (SmoothProgressBar)detailView.findViewById(R.id.idHorizontalSPB);
        //    parseIODatabase(selected_shorts);
        return detailView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) getActivity().findViewById(android.R.id.content));
        font = Typeface.createFromAsset(detailView.getContext().getAssets(), "fontawesome/fonts/fontawesome-webfont.ttf");
        changeInfoArrow.setTypeface(font);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiInterface = ApiClient.getClient(Consts.coinpediaURL).create(ApiInterface.class);
        getCoinDataFromCache();
        btnGoGraph.setOnClickListener(this);
    }

    public void getCoinDataFromCache() {

        ArrayList<ApiToViewModel> arrayListProduct = new ArrayList<>();
        String jsonResponse = new SharedPrefClass(detailView.getContext()).getJsonResponse();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            timeTV.setText("Last Update - " + new SharedPrefClass(detailView.getContext()).getDate());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String shorts = jsonObject.getString("short");
                if (selected_shorts.equalsIgnoreCase(shorts)) {
                    String longs = jsonObject.getString("long");
                    Double perc = jsonObject.getDouble("perc");
                    Double price = jsonObject.getDouble("price");
                    Double volume = jsonObject.getDouble("volume");
                    Double supply = jsonObject.getDouble("supply");
                    Double mktcap = jsonObject.getDouble("mktcap");

                    SetIOData(price, perc, mktcap, supply, 0.0, 0.0, 0.0);
              //      parseIOApiHttp(shorts);
                    parseIOApi2(shorts);
                    new SharedPrefClass(detailView.getContext()).setCurrentCoin(shorts);
                    setHeaderView(longs);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void parseIOApi2(String coin) {
// HTTP
        // new OkHttpClass(detailView.getCeontext()).execute(Consts.coinpediaFullURL);
        spbHorizontal.setVisibility(View.VISIBLE);
        new OkHttpClass(detailView.getContext()) {
            @Override
            protected void onPostExecute(String responseJson) {
                super.onPostExecute(responseJson);
                if (responseJson != null) {
                    spbHorizontal.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(responseJson);
                        JSONArray jsonArray = jsonObject.getJSONArray("coinresponse");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            open = c.getDouble("open");
                            high = c.getDouble("high");
                            low = c.getDouble("low");

                            System.out.print(low);
                            SetIOData(null, null, null, null, open, high, low);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute(Consts.todayChangeURL+coin);

    }

    public void parseIOApiHttp(String coin)
    {
        new BackgroundTask(detailView.getContext()){
            @Override
            protected void onPostExecute(String results) {
                super.onPostExecute(results);

                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(results);
                    JSONArray jsonArray = jsonObject.getJSONArray("coinresponse");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        open = c.getDouble("open");
                        high = c.getDouble("high");
                        low = c.getDouble("low");

                        System.out.print(low);
                        SetIOData(null, null, null, null, open, high, low);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(coin);
    }

    public void SetIOData(Double price, Double perc, Double mktcap, Double supply, Double open, Double high, Double low) {
        //
        try {

            if (price != null) {
                tvPrice.setText("$ " + new FormatterClass().formatPrecisionDetail(price));
            }
            if (perc != null) {
                tvPerc.setText(new FormatterClass().precisionPerc(perc) + "%");
                Animation animation = AnimationUtils.loadAnimation(detailView.getContext(), R.anim.bottomtotopfront);
                tvPerc.startAnimation(animation);
            }
            if (mktcap != null) {
                tvMktCap.setText(new FormatterClass().formatPrecisionDetail(mktcap));
            }
            if (supply != null) {
                tvCurrency.setText("$ " + new FormatterClass().formatPrecisionDetail(supply));
            }

            tvOpen.setText(new FormatterClass().convertDecimalPerc(String.valueOf(open)));
            tvHigh.setText("$ " + new FormatterClass().convertDecimalPerc(String.valueOf(high)));
            tvLow.setText("$ " + new FormatterClass().convertDecimalPerc(String.valueOf(low)));
            tvChange.setText("$ " + new FormatterClass().convertDecimalPerc(String.valueOf(high - low)));


            Activity activity = getActivity();
            if (activity != null) {

            /*changeInfoArrow.setTypeface(font);*/
                if (high < low) {
                    changeInfoArrow.setText(getResources().getString(R.string.down_arrow));
                    changeInfoArrow.setTextColor(Color.parseColor("#FF4444"));
                } else {
                    changeInfoArrow.setText(getResources().getString(R.string.up_arrow));
                    changeInfoArrow.setTextColor(Color.parseColor("#00FF00"));
                }
                if (perc < 0) {
                /*changeInfoArrow.setText(getResources().getString(R.string.down_arrow));
                changeInfoArrow.setTextColor(Color.parseColor("#FF4444"));*/
                    tvPerc.setTextColor(Color.parseColor("#FF4444"));
                } else {
                /*changeInfoArrow.setText(getResources().getString(R.string.up_arrow));
                changeInfoArrow.setTextColor(Color.parseColor("#00FF00"));*/
                    tvPerc.setTextColor(Color.parseColor("#00FF00"));
                }
            }
        } catch (NullPointerException npe) {
            Log.e(TAG, "Error : " + npe, npe);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idGoGraph:
                Navigatior.getClassInstance().navigateToActivityContext(detailView.getContext(), GraphActivity.class);
                break;
        }
    }

    public void setHeaderView(String coinLongs) {
        tvCoinName.setText(coinLongs);
        String imgName = coinLongs.trim().replaceAll("\\s+", "");


        String img = "https://coincap.io/images/coins/" + imgName + ".png";

        /*Glide.with(mContext)
                .load("https://coincap.io/images/coins/"+imgName+".png")
                .into(holder.mCoinIV);*/

        //IMAGE CACHE START
        ImagesCache cache = ImagesCache.getInstance();//Singleton instance handled in ImagesCache class.
        cache.initializeCache();

        Bitmap bm = cache.getImageFromWarehouse(img);

        if (bm != null) {
            //   holder.mCoinIV.setImageBitmap(bm);
            Glide.with(detailView.getContext())
                    .load(bm)
                    .into(ivCoinIcon);
        } else {
            //  holder.mCoinIV.setImageBitmap(null);
            Glide.with(detailView.getContext())
                    .load("https://coincap.io/images/coins/" + imgName + ".png")
                    .into(ivCoinIcon);

            DownloadImageTask imgTask = new DownloadImageTask(cache, ivCoinIcon, 300, 300);//Since you are using it from `Activity` call second Constructor.

            imgTask.execute(img);
        }
        //IMAGE CACHE END


    }

    class BackgroundTask extends AsyncTask<String, String, String>
    {
        Context mContext;
        BackgroundTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   ProgressClass.getProgressInstance().startProgress(detailView.getContext());
            spbHorizontal.setVisibility(View.VISIBLE);
       //     Toast.makeText(detailView.getContext(),"Output-1 : "+new Date(), Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(String... params) {

            String status = saveCoinData(params[0]);
            return status;
        }
        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
       //     ProgressClass.getProgressInstance().stopProgress();
            spbHorizontal.setVisibility(View.GONE);
       //     Toast.makeText(detailView.getContext(),"Output-2: "+new Date(), Toast.LENGTH_LONG).show();
        }

        public String  saveCoinData(String coin)
        {
            String result = null;
            String inputLine = null;
            try {
                URL myUrl = new URL(Consts.todayChangeURL+coin);
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStreamReader streamReader = new  InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                /*while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }*/
                if(reader.readLine() != null){
                   // stringBuilder.append(inputLine);
                    result = reader.readLine();
                }
                reader.close();
             //   streamReader.close();
             //   result = stringBuilder.toString();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
