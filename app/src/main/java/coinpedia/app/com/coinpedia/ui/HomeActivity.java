package coinpedia.app.com.coinpedia.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.floating_menu.BuilderManager;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.front.FrontHolderAdapter;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.utils.ComparatorClass;
import coinpedia.app.com.coinpedia.utils.ComparatorVolumeClass;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;

public class HomeActivity extends BaseActivity {

    int latStatus = 0;
    private static final String TAG = HomeActivity.class.getSimpleName();
    ArrayList<ApiToViewModel> frontDbAL = new ArrayList<ApiToViewModel>();

    @BindView(R.id.idTimeTV)
    TextView timeTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.idFrontRC)
    RecyclerView rcFront;
    @BindView(R.id.filter)
    EditText mFilterText;
    FrontHolderAdapter frontHolderAdapter;
    @BindView(R.id.idSPNFilter)
    Spinner mFilterSPN;

    // DAta
    String[] count = {"Market Cap", "Price", "Volume"};
   // View frontView;
    Typeface font;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) findViewById(android.R.id.content));
        font = Typeface.createFromAsset( getAssets(), "fontawesome/fonts/fontawesome-webfont.ttf" );
        mFilterText.setTypeface(font);
    //    Toast.makeText(HomeActivity.this, "Oncreate", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onStart() {
        super.onStart();
    //    Toast.makeText(HomeActivity.this, "OnStart", Toast.LENGTH_LONG).show();
        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        for (int i = 0; i < bmb4.getPiecePlaceEnum().pieceNumber(); i++)
            bmb4.addBuilder(BuilderManager.getPieceCornerRadiusHamButtonBuilder());
        //
        mFilterText.setHint(R.string.text_search);
        loadFilterSPN();
        getCoinDataFromCache();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

       }

    void refreshItems() {
        refreshLayout();
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
    }

    public void refreshLayout()
    {
        if(mFilterText.getText().toString().trim().length()> 0)
        {
            parseIODatabase("NoFilter");
            frontHolderAdapter.getFilter().filter(mFilterText.getText().toString());
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            parseIODatabase(mFilterSPN.getItemAtPosition(mFilterSPN.getSelectedItemPosition()).toString());
        }
    }
    public void parseIODatabase(String filter)
    {
        mSwipeRefreshLayout.setRefreshing(true);
        timeTV.setText("Last Update - "+new SharedPrefClass(HomeActivity.this).getDate());

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(filter.equalsIgnoreCase("Price")) {
            mSwipeRefreshLayout.setRefreshing(false);
            frontDbAL = getCoinDataFromCache();
            Collections.sort(frontDbAL, new ComparatorClass());
            setOutputFrontList(frontDbAL);
        }else if(filter.equalsIgnoreCase("Volume")) {
            mSwipeRefreshLayout.setRefreshing(false);
            frontDbAL = getCoinDataFromCache();
            Collections.sort(frontDbAL, new ComparatorVolumeClass());
            setOutputFrontList(frontDbAL);
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
            frontDbAL = getCoinDataFromCache();
            setOutputFrontList(frontDbAL);
        }
        setOutputFrontList(frontDbAL);

        //FILTER
        mFilterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String strFilter = mFilterText.getText().toString();
                frontHolderAdapter.getFilter().filter(strFilter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

               /* String strFilter = editable.toString();
                frontHolderAdapter.getFilter().filter(strFilter);*/

            }
        });
    }
    public void setOutputFrontList(ArrayList<ApiToViewModel> frontList)
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        rcFront.setLayoutManager(mLayoutManager);
        frontHolderAdapter = new FrontHolderAdapter(HomeActivity.this, frontList);
        rcFront.setAdapter(frontHolderAdapter);
        Animation animation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.bottomtotopfront);
        rcFront.startAnimation(animation);
    }

    public void loadFilterSPN()
    {
        ArrayAdapter filterAdapter = new ArrayAdapter(HomeActivity.this, R.layout.row_spn, count);
        mFilterSPN.setAdapter(filterAdapter);

        mFilterSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    TextView textView = (TextView)adapterView.findViewById(R.id.idRowSPN) ;
                    textView.setTypeface(font);
                    textView.setTextSize(12);
                    mFilterText.setTextSize(12);
                    String spnData = mFilterSPN.getItemAtPosition(i).toString();
                    if (spnData.equalsIgnoreCase("Market Cap")) {
                        parseIODatabase("NOFILTER");
                    }
                    if (spnData.equalsIgnoreCase("Price")) {
                        parseIODatabase(spnData);
                    }
                    if (spnData.equalsIgnoreCase("Volume")) {
                        parseIODatabase(spnData);
                    }
                }catch (NullPointerException npe)
                {
                    Log.e(TAG, "Err : "+npe, npe);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public ArrayList<ApiToViewModel> getCoinDataFromCache()
    {

        ArrayList<ApiToViewModel> arrayListProduct = new ArrayList<>();
        String jsonResponse = new SharedPrefClass(HomeActivity.this).getJsonResponse();
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
    public void onBackPressed() {
        if(mFilterText.getText().toString().length()>0)
        {
            mFilterText.setText("");
        }else {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("CoinPedia")
                    .setMessage("are you want to close app!")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (int j = 0; j < 10; j++) {
                              //  System.exit(0);
                                finishAffinity();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    })
                    .show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if ( latStatus == 1)
        {
         // recreate();
            restartActivity();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        latStatus = 1;
    //    finish();

    }

    public void restartActivity(){
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }
    private void QuitApplication(){

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

    }

}
