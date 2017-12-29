package coinpedia.app.com.coinpedia.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import coinpedia.app.com.coinpedia.CoinpediaAppWidget;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.front.FrontHolderAdapter;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import coinpedia.app.com.coinpedia.widget.CurrencySelectorAdapter;

public class CurrencySelectorListActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    CurrencySelectorAdapter currencySelectorAdapter;
    ArrayList<ApiToViewModel> frontDbAL = new ArrayList<ApiToViewModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_selector_list);
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.btn_select);

        frontDbAL = getCoinDataFromCache();
        currencySelectorAdapter = new CurrencySelectorAdapter(getApplicationContext(),frontDbAL);
        listView.setAdapter(currencySelectorAdapter);

//        setOutputFrontList();

        button.setOnClickListener(this);
    }

 //************Use this when we use simple Array Adapter*********************************
    public void setOutputFrontList()
    {
        String[] Currencyname = new String[frontDbAL.size()];
        for(int i=0;i<frontDbAL.size();i++){
            Currencyname[i] =frontDbAL.get(i).getLongs().toString();
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, Currencyname);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

    }


    public ArrayList<ApiToViewModel> getCoinDataFromCache()
    {

        ArrayList<ApiToViewModel> arrayListProduct = new ArrayList<>();
        String jsonResponse = new SharedPrefClass(CurrencySelectorListActivity.this).getJsonResponse();
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayListProduct;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_select:
                ArrayList<ApiToViewModel> get_checkValue = currencySelectorAdapter.getFrontList();
                ArrayList<ApiToViewModel>apiToViewModels=new ArrayList<>();

                for(ApiToViewModel model:get_checkValue){
                    if (model.getChkStatus().equals("1")){
                        apiToViewModels.add(model);
                    }
                }


//************Use this when we use simple Array Adapter*********************************
               /* SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i)){
                        apiToViewModels.add(frontDbAL.get(position));
                    }
                        selectedItems.add(adapter.getItem(position));
                }

                String[] outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = selectedItems.get(i);
                }*/



                Intent intent = new Intent(this,CoinpediaAppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), CoinpediaAppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                intent.putExtra("selected_currency",apiToViewModels);

                getApplicationContext().sendBroadcast(intent);
                CurrencySelectorListActivity.this.finish();
                break;
        }
    }
}
