package coinpedia.app.com.coinpedia.notifications;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.model.calc.CoinListSpinnerModel;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.model.front.FrontModel;
import coinpedia.app.com.coinpedia.parseApi.ApiClient;
import coinpedia.app.com.coinpedia.parseApi.ApiInterface;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.ProgressClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ravi on 8/8/17.
 */

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    View view;
    TextView tv_coin_value, tv_max_value,tv_min_value;
    Spinner spnr_currency;
    DiscreteSeekBar discrete_seekbar_min,discrete_seekbar_max;
    int minInt,maxInt;
    ArrayList<CoinListSpinnerModel> coinList;
    Button btn_submit;
    NotificationFragmentCommunicator communicator;
    SwitchCompat switch_notifcation;
    SharedPrefClass pref;
    public void setNotificationFragmentCommunicator(NotificationFragmentCommunicator communicator){
        this.communicator=communicator;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications,container,false);
        pref = new SharedPrefClass(getContext());
        init();
        return view;
    }


    private void init(){
        discrete_seekbar_max = view.findViewById(R.id.discrete_seekbar_max);
        discrete_seekbar_min = view.findViewById(R.id.discrete_seekbar_min);
        spnr_currency = view.findViewById(R.id.spnr_currency);
        tv_coin_value = view.findViewById(R.id.tv_coin_value);
        tv_max_value = view.findViewById(R.id.tv_max_value);
        tv_min_value = view.findViewById(R.id.tv_min_value);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        switch_notifcation = (SwitchCompat) view.findViewById(R.id.switch_notification);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) getActivity().findViewById(android.R.id.content));

        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coinList = new ArrayList<>();
        //parseIOApi();
        getCoinDataFromCache();
    }

    public void getCoinDataFromCache()
    {

        String jsonResponse = new SharedPrefClass(view.getContext()).getJsonResponse();
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

                CoinListSpinnerModel coinListSpinnerModel = new CoinListSpinnerModel(shorts,price);
                coinList.add(coinListSpinnerModel);

                loadCoinList1(coinList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void parseIOApi()
    {
        ProgressClass.getProgressInstance().startProgress(view.getContext());
        ApiInterface apiInterface = ApiClient.getClient(Consts.coincapURL).create(ApiInterface.class);
        apiInterface.getForntData().enqueue(new Callback<List<FrontModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<FrontModel>> call, Response<List<FrontModel>> response) {
                if(response.isSuccessful())
                {

                    ArrayList<ApiToViewModel> frontAL = new ArrayList<ApiToViewModel>();
        //            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i<response.body().size(); i++) {
                      /*  Boolean published = response.body().get(i).getPublished();
                        long time = response.body().get(i).getTime();
                        Double vwapDataBTC = response.body().get(i).getVwapDataBTC();
                        String vwapData = response.body().get(i).getVwapData();
                        String cap24hrChange = response.body().get(i).getCap24hrChange();*/
                        String shorts = response.body().get(i).getShort();
                   /*     String position24 = response.body().get(i).getPosition24();
                        String longs = response.body().get(i).getLong();
                        String perc = response.body().get(i).getPerc();
                        String supply = response.body().get(i).getSupply();*/
                        Double price = Double.parseDouble(response.body().get(i).getPrice().toString());
                       /* Double volume = Double.parseDouble(response.body().get(i).getVolume());
                        String usdVolume = response.body().get(i).getUsdVolume();
                        String mktcap = response.body().get(i).getMktcap().toString();
                        String cap24hrChangePercent = response.body().get(i).getCap24hrChangePercent();
                        String capPercent = response.body().get(i).getCapPercent();
                        Boolean isBuy = response.body().get(i).getIsBuy();
                        Boolean shapeshift = response.body().get(i).getShapeshift();*/

                        CoinListSpinnerModel coinListSpinnerModel = new CoinListSpinnerModel(shorts,price);
                        coinList.add(coinListSpinnerModel);

                        loadCoinList1(coinList);

                    }
                    ProgressClass.getProgressInstance().stopProgress();
                }

            }

            @Override
            public void onFailure(Call<List<FrontModel>> call, Throwable t) {
                call.cancel();
            }
        });

    }
    String[] coinName;
    double[] coinPrice;
    private void loadCoinList1(ArrayList<CoinListSpinnerModel> coinList){
         coinName = new String[coinList.size()];
         coinPrice = new double[coinList.size()];
        for(int i=0;i<coinList.size();i++){
             coinName[i] = coinList.get(i).getCoinName();
            coinPrice[i] = coinList.get(i).getCoinPrice();
        }
        ArrayAdapter coinAdapter = new ArrayAdapter(getContext(),R.layout.spn_view,R.id.idRowSPN,coinName);
        spnr_currency.setAdapter(coinAdapter);

        setCoinValue(coinPrice[0]);

        spnr_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                double coinSelectPrice = coinList.get(i).getCoinPrice();
                setCoinValue(coinSelectPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setCoinValue(double coinPrice){
        tv_coin_value.setText(convertDecimalPrice(coinPrice));

        double minDouble = Double.parseDouble(convertDecimalPrice(coinPrice));
        double maxDouble = Double.parseDouble(convertDecimalPrice(coinPrice));
        maxInt = (int) maxDouble;
        minInt = (int) minDouble;

        tv_max_value.setText(convertDecimalPrice(coinPrice));
        tv_min_value.setText(convertDecimalPrice(coinPrice));
        setSeekbarValue();
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

    private void setSeekbarValue(){
        discrete_seekbar_max.setMax(maxInt*2);
        discrete_seekbar_max.setProgress(minInt);
        discrete_seekbar_max.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if (value < minInt) {
                 //   discrete_seekbar_max.setEnabled(false);
                    tv_max_value.setText(value+".00");
                } else
                    tv_max_value.setText(value+".00");
                    discrete_seekbar_max.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        /*discrete_seekbar_max.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value;
            }
        });*/

        discrete_seekbar_min.setMax(maxInt);
        discrete_seekbar_min.setProgress(minInt);
        discrete_seekbar_min.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if (value > minInt) {
                    discrete_seekbar_min.setEnabled(false);
                } else
                    tv_min_value.setText(value + ".00");
                    discrete_seekbar_min.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_submit:
                if(switch_notifcation.isChecked() == true)
                { communicator.submitNotificationRange(getNotificationRangeRequest());}
                else {
                    new SweetAlertDialog(view.getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText("Alert Not Enabled")
                            .setContentText("Press OK to enable and again save")
                            .setCustomImage(R.drawable.ic_switch_on)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    switch_notifcation.setChecked(true);
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            default:
                break;
        }
    }

    private NotificationSettingRequest getNotificationRangeRequest(){

        NotificationSettingRequest notificationSettingRequest = new NotificationSettingRequest();
        notificationSettingRequest.max = tv_max_value.getText().toString();
        notificationSettingRequest.min = tv_min_value.getText().toString();
        notificationSettingRequest.status = notificationStatus();
        notificationSettingRequest.symbol = spnr_currency.getSelectedItem().toString();
        notificationSettingRequest.udid = pref.getUdId();
        notificationSettingRequest.token = "xyxyxyxyxyxyxyxy";
        return notificationSettingRequest;
    }

    private String notificationStatus(){
        if(switch_notifcation.isChecked()){
            return  "on";
        }else if(!switch_notifcation.isChecked()){
            return  "off";
        }
        return null;
    }

    public interface NotificationFragmentCommunicator{
         void submitNotificationRange(NotificationSettingRequest notificationSettingRequest);
    }


}
