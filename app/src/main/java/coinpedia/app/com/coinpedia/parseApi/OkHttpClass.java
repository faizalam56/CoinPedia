package coinpedia.app.com.coinpedia.parseApi;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;

/**
 * Created by senzec on 24/7/17.
 */

public class OkHttpClass extends AsyncTask<String, String, String>{

    Context _context;
    OkHttpClient client = new OkHttpClient();

    public OkHttpClass(Context _context)
    {
        this._context = _context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String responseData = null;
        String url = strings[0];
        Request request = new Request.Builder()
                .url(url)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .build();

        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();

            responseData =  response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(String responseJson) {
        super.onPostExecute(responseJson);
        System.out.print(responseJson);
    }

    /*    public OkHttpClass(Context _context)
    {
        this._context = _context;
    }

    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        okhttp3.Response response = client.newCall(request).execute();
        return response.body().string();
    }*/
}
