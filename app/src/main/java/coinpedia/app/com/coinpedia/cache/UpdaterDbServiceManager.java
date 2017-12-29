package coinpedia.app.com.coinpedia.cache;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.FormatterClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;

public class UpdaterDbServiceManager extends Service {

    private static final String TAG = UpdaterDbServiceManager.class.getSimpleName().toString();
    Context context = this;
    private Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("COINPEDIA - Service Started");
                new BackgroundTask(context).execute();
            }
        }, 0, 15000);//15 Seconds
    }

   /* public void updateDbFromServer()
    {

        ApiInterface apiInterface = ApiClient.getClient(Consts.coincapURL).create(ApiInterface.class);
        apiInterface.getForntData().enqueue(new Callback<List<FrontModel>>() {
            @Override
            public void onResponse(Call<List<FrontModel>> call, Response<List<FrontModel>> response) {
                if(response.isSuccessful())
                {
                    ArrayList<ApiToViewModel> frontAL = new ArrayList<ApiToViewModel>();
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    dbAdapterClass.deleteCoinTable();
                    //  = response.body().get(0).getPublished();

                    for(int i = 0; i<response.body().size(); i++) {
                       *//* Boolean published = response.body().get(i).getPublished();
                        Double vwapDataBTC = response.body().get(i).getVwapDataBTC();
                        String vwapData = response.body().get(i).getVwapData();
                        String cap24hrChange = response.body().get(i).getCap24hrChange();
                        String position24 = response.body().get(i).getPosition24();
                        String supply = response.body().get(i).getSupply();
                        String usdVolume = response.body().get(i).getUsdVolume();
                        Double mktcap = Double.parseDouble(response.body().get(i).getMktcap().toString());
                        String cap24hrChangePercent = response.body().get(i).getCap24hrChangePercent();
                        String capPercent = response.body().get(i).getCapPercent();
                        Boolean isBuy = response.body().get(i).getIsBuy();
                        Boolean shapeshift = response.body().get(i).getShapeshift();
                        *//*

                        long time = response.body().get(i).getTime();
                        String shorts = response.body().get(i).getShort();
                        String longs = response.body().get(i).getLong();
                        String perc = response.body().get(i).getPerc();
                        Double price = Double.parseDouble(response.body().get(i).getPrice().toString());
                        Double volume = Double.parseDouble(response.body().get(i).getVolume());

                        // DATABASE
                        dbAdapterClass.insertCoinTable(time, shorts, longs, perc, price, volume);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FrontModel>> call, Throwable t) {

            }
        });

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //   shutdownService();
        Toast.makeText(context, "Service stopped", Toast.LENGTH_LONG).show();
        timer.cancel();
        Log.i(TAG, "onCreate() , service stopped...");

    }
}

class BackgroundTask extends AsyncTask<Void, Void, Void>
{
    Context mContext;
    String lineOfJson = "";

    BackgroundTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT = 15000;
        final int CONNECTION_TIMEOUT = 15000;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        saveCoinData();
        return null;
    }

    public void  saveCoinData()
    {

        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(Consts.coincapFullURL);

            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            if(result != null)
            {
                new SharedPrefClass(mContext).setDate(new FormatterClass().simpleDate());
                new SharedPrefClass(mContext).setJsonResponse(result);
           //     saveGraphDayData();
            }
            // saveJsonResponse(result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
 /*   public void saveGraphDayData()
    {

        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(Consts.coincapFullURL);

            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            if(result != null)
            {
                new SharedPrefClass(mContext).setDate(new FormatterClass().simpleDate());
                new SharedPrefClass(mContext).setJsonResponse(result);
                saveGraphWeekData();
            }
            // saveJsonResponse(result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveGraphWeekData()
    {

        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(Consts.coincapFullURL);

            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            if(result != null)
            {
                new SharedPrefClass(mContext).setDate(new FormatterClass().simpleDate());
                new SharedPrefClass(mContext).setJsonResponse(result);
                saveGraphMonthData();
            }
            // saveJsonResponse(result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveGraphMonthData()
    {

        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(Consts.coincapFullURL);

            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            if(result != null)
            {
                new SharedPrefClass(mContext).setDate(new FormatterClass().simpleDate());
                new SharedPrefClass(mContext).setJsonResponse(result);
            }
            // saveJsonResponse(result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/
}
