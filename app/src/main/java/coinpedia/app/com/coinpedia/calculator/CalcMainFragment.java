package coinpedia.app.com.coinpedia.calculator;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.model.calc.CoinListSpinnerModel;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.model.front.FrontModel;
import coinpedia.app.com.coinpedia.parseApi.ApiClient;
import coinpedia.app.com.coinpedia.parseApi.ApiInterface;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.FormatterClass;
import coinpedia.app.com.coinpedia.utils.ProgressClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by senzec on 9/8/17.
 */

public class CalcMainFragment extends Fragment implements View.OnClickListener{

    // DECLARATION
    private static final String TAG = CalcMainFragment.class.getSimpleName().toString();
    View viewCalc;
    private LinearLayout firstLayout, secondLayout;
    private Spinner spnCoinList;
    private TextView button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonPoint, buttonClear ;
    private TextView tvPreCoin, tvPostCoin;
    private int SELECTED_LAYOUT = 0;
    Double dropDownDataPre = 0.0, dropDownDataPost = 0.0;
    Typeface font;
    private TableLayout mKeypadLayout;

    //INIT
    ArrayList<CoinListSpinnerModel> coinList;
 //   Double tempBTCPrice = 2564.3325;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewCalc = inflater.inflate(R.layout.fragment_main_calc, container, false);
        return viewCalc;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) getActivity().findViewById(android.R.id.content));
        font = Typeface.createFromAsset( viewCalc.getContext().getAssets(), "fontawesome/fonts/fontawesome-webfont.ttf" );
        buttonClear.setTypeface(font);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coinList = new ArrayList<>();
        initView();
        textChangeEvent();
        setHeight();
        handleClick();
        getCoinDataFromCache();
      //  parseIOApi();
       // loadCoinList();

    }

    public void initView() {
        mKeypadLayout = (TableLayout) viewCalc.findViewById(R.id.idKeypadLayout);
        firstLayout = (LinearLayout) viewCalc.findViewById(R.id.idLayoutFirst);
        secondLayout = (LinearLayout) viewCalc.findViewById(R.id.idLayoutSecond);
        spnCoinList = (Spinner) viewCalc.findViewById(R.id.idCoinListSPN);

        button0 = (TextView) viewCalc.findViewById(R.id.button0);
        button1 = (TextView) viewCalc.findViewById(R.id.button1);
        button2 = (TextView) viewCalc.findViewById(R.id.button2);
        button3 = (TextView) viewCalc.findViewById(R.id.button3);
        button4 = (TextView) viewCalc.findViewById(R.id.button4);
        button5 = (TextView) viewCalc.findViewById(R.id.button5);
        button6 = (TextView) viewCalc.findViewById(R.id.button6);
        button7 = (TextView) viewCalc.findViewById(R.id.button7);
        button8 = (TextView) viewCalc.findViewById(R.id.button8);
        button9 = (TextView) viewCalc.findViewById(R.id.button9);
        buttonPoint = (TextView) viewCalc.findViewById(R.id.buttonPoint);
        buttonClear = (Button) viewCalc.findViewById(R.id.buttonClear);

        tvPreCoin = (TextView) viewCalc.findViewById(R.id.idPreCoinValueTV);
        tvPostCoin = (TextView) viewCalc.findViewById(R.id.idPostCoinINRTV);

    }
    public void setHeight()
    {

        //WIDTH
        button0.setWidth(getWidth()/3);
        button1.setWidth(getWidth()/3);
        button2.setWidth(getWidth()/3);
        button3.setWidth(getWidth()/3);
        button4.setWidth(getWidth()/3);
        button5.setWidth(getWidth()/3);
        button6.setWidth(getWidth()/3);
        button7.setWidth(getWidth()/3);
        button8.setWidth(getWidth()/3);
        button9.setWidth(getWidth()/3);
        buttonPoint.setWidth(getWidth()/3);
        buttonClear.setWidth(getWidth()/3);
        //HEIGHT
        button0.setHeight(getWidth()/5);
        button1.setHeight(getWidth()/5);
        button2.setHeight(getWidth()/5);
        button3.setHeight(getWidth()/5);
        button4.setHeight(getWidth()/5);
        button5.setHeight(getWidth()/5);
        button6.setHeight(getWidth()/5);
        button7.setHeight(getWidth()/5);
        button8.setHeight(getWidth()/5);
        button9.setHeight(getWidth()/5);
        buttonPoint.setHeight(getWidth()/5);
        buttonClear.setHeight(getWidth()/5);

    }
    public void handleClick() {
        /*firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);*/
        tvPreCoin.setOnClickListener(this);
        tvPostCoin.setOnClickListener(this);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonClear.setOnClickListener(this);

        tvPreCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //   Toast.makeText(MainActivity.this, editable.toString(), Toast.LENGTH_LONG).show();
                if(SELECTED_LAYOUT == 0 || SELECTED_LAYOUT == 1) {
                    try {
                        String strValue = editable.toString().trim();
                        dropDownDataPre = Double.parseDouble(spnCoinList.getItemAtPosition(spnCoinList.getSelectedItemPosition()).toString());
                        if (!strValue.isEmpty() && !strValue.equals("0")) {
                            Double rawCoinValue = Double.parseDouble(strValue);
                            tvPostCoin.setText(String.valueOf(new FormatterClass().precisionTwo(String.valueOf(rawCoinValue * dropDownDataPre))));
                        }
                    }catch (NullPointerException npe)
                    {
                        Log.e(TAG, "Error : "+npe, npe);
                    }
                }
            }
        });
        tvPostCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(SELECTED_LAYOUT == 2) {
                    try {
                        String strValue = editable.toString().trim();
                        dropDownDataPost = Double.parseDouble(spnCoinList.getItemAtPosition(spnCoinList.getSelectedItemPosition()).toString());
                        if (!strValue.isEmpty() && !strValue.equals("0")) {
                            Double rawCoinValue = Double.parseDouble(strValue);
                            tvPreCoin.setText(String.valueOf(new FormatterClass().precisionSeven(String.valueOf(rawCoinValue / dropDownDataPost))));
                        }
                    }catch (NumberFormatException nfe)
                    {
                        Log.e(TAG, "Error : "+nfe, nfe);
                    }catch (NullPointerException npe)
                    {
                        Log.e(TAG, "Error : "+npe, npe);
                    }

                }
            }
        });


    }
    public void getCoinDataFromCache()
    {

        ArrayList<ApiToViewModel> arrayListProduct = new ArrayList<>();
        String jsonResponse = new SharedPrefClass(viewCalc.getContext()).getJsonResponse();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for(int i = 0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String shorts = jsonObject.getString("short");
                //   String longs = jsonObject.getString("long");
                //   String perc = jsonObject.getString("perc");
                Double price = jsonObject.getDouble("price");
                Double volume = jsonObject.getDouble("volume");

                CoinListSpinnerModel coinListSpinnerModel = new CoinListSpinnerModel(shorts, price);
                coinList.add(coinListSpinnerModel);

                loadCoinList(coinList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void loadCoinList(ArrayList<CoinListSpinnerModel> coinList)
    {
        spnCoinList.setAdapter(new CustomCalcSpinnerAdapter(viewCalc.getContext(), coinList));

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spnCoinList);
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        spnCoinList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    //
                    String spnData = spnCoinList.getItemAtPosition(i).toString();
           //         Toast.makeText(getActivity(), "Output : " + spnData, Toast.LENGTH_LONG).show();
                    performCalculation();

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
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.idPreCoinValueTV:
                SELECTED_LAYOUT = 1;
                resetLayout(SELECTED_LAYOUT);
                break;
            case R.id.idPostCoinINRTV:
                SELECTED_LAYOUT = 2;
                resetLayout(SELECTED_LAYOUT);
                break;
            case R.id.button0:
                setButtonOutput(ConstsCalculator.ZERO_SELECTED);
                break;
            case R.id.button1:
                setButtonOutput(ConstsCalculator.ONE_SELECTED);
                break;
            case R.id.button2:
                setButtonOutput(ConstsCalculator.TWO_SELECTED);
                break;
            case R.id.button3:
                setButtonOutput(ConstsCalculator.THREE_SELECTED);
                break;
            case R.id.button4:
                setButtonOutput(ConstsCalculator.FOUR_SELECTED);
                break;
            case R.id.button5:
                setButtonOutput(ConstsCalculator.FIVE_SELECTED);
                break;
            case R.id.button6:
                setButtonOutput(ConstsCalculator.SIX_SELECTED);
                break;
            case R.id.button7:
                setButtonOutput(ConstsCalculator.SEVEN_SELECTED);
                break;
            case R.id.button8:
                setButtonOutput(ConstsCalculator.EIGHT_SELECTED);
                break;
            case R.id.button9:
                setButtonOutput(ConstsCalculator.NINE_SELECTED);
                break;
            case R.id.buttonPoint:
                setButtonOutput(ConstsCalculator.POINT_SELECTED);
                break;
            case R.id.buttonClear:
                tvPreCoin.setText("0");
                tvPostCoin.setText("0");
                break;

        }
    }
    public void setButtonOutput(String currentValue)
    {

        if(SELECTED_LAYOUT == 0)
        {resetTV(SELECTED_LAYOUT); tvPreCoin.append(currentValue); }
        else if(SELECTED_LAYOUT == 1)
        {resetTV(SELECTED_LAYOUT); tvPreCoin.append(currentValue); }
        else if(SELECTED_LAYOUT == 2)
        { resetTV(SELECTED_LAYOUT); tvPostCoin.append(currentValue);   }
    }
    public void performCalculation(){
        if(SELECTED_LAYOUT == 0 || SELECTED_LAYOUT == 1) {
            String strValue = tvPreCoin.getText().toString().trim();
            dropDownDataPre = Double.parseDouble(spnCoinList.getItemAtPosition(spnCoinList.getSelectedItemPosition()).toString());
            if (!strValue.isEmpty() && !strValue.equals("0")) {
                Double rawCoinValue = Double.parseDouble(strValue);
                tvPostCoin.setText(String.valueOf(new FormatterClass().precisionTwo(String.valueOf(rawCoinValue * dropDownDataPre))));
            }
        }
        else  if(SELECTED_LAYOUT == 2) {
            String strValue = tvPostCoin.getText().toString().trim();
            dropDownDataPost = Double.parseDouble(spnCoinList.getItemAtPosition(spnCoinList.getSelectedItemPosition()).toString());
            if (!strValue.isEmpty() && !strValue.equals("0")) {
                Double rawCoinValue = Double.parseDouble(strValue);
                tvPreCoin.setText(String.valueOf(new FormatterClass().precisionSeven(String.valueOf(rawCoinValue / dropDownDataPost))));
            }
        }
    }
    public void textChangeEvent(){
        tvPreCoin.addTextChangedListener(preCoinWatcher);
        tvPostCoin.addTextChangedListener(postCoinWatcher);

    }

    public void resetTV(int i)
    {
        if( i ==0 || i == 1) {
            String tmp = tvPreCoin.getText().toString().trim();
            if (tmp.length() == 1 && tmp.equals("0")) {
                tvPreCoin.setText("");
            }
        }if( i == 2) {
        String tmp = tvPostCoin.getText().toString().trim();
        if(tmp.length()==1 && tmp.equals("0"))
        { tvPostCoin.setText("");}
    }
    }
    public void resetLayout(int i)
    {
        if( i == 1) {
            tvPreCoin.setTextColor(getResources().getColor(R.color.White));
            tvPostCoin.setTextColor(getResources().getColor(R.color.dark_grey));
            tvPreCoin.setBackgroundColor(getResources().getColor(R.color.app_bar_header));
            tvPostCoin.setBackgroundColor(getResources().getColor(R.color.White));
            tvPreCoin.setText("0");
            tvPostCoin.setText("0");
        }if( i == 2) {
        tvPreCoin.setTextColor(getResources().getColor(R.color.dark_grey));
        tvPostCoin.setTextColor(getResources().getColor(R.color.White));
        tvPreCoin.setBackgroundColor(getResources().getColor(R.color.White));
        tvPostCoin.setBackgroundColor(getResources().getColor(R.color.app_bar_header));
        tvPreCoin.setText("0");
        tvPostCoin.setText("0");
    }
    }
    private int getWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return width;
    }
    private int getHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }

    private final TextWatcher preCoinWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
           // textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {

            if (s.length() >=15 && s.length() <=17
                    ) {
                tvPreCoin.setTextSize(20);
            } else  if (s.length() >=18 ) {
                tvPreCoin.setTextSize(16);
            }else{
                tvPreCoin.setTextSize(24);
            }


        }
    };
    private final TextWatcher postCoinWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() >=15 && s.length() <=17
                    ) {
               tvPostCoin.setTextSize(20);
            } else  if (s.length() >=18 ) {
                tvPostCoin.setTextSize(16);
            }else{
                tvPostCoin.setTextSize(24);
            }
        }
    };
}
