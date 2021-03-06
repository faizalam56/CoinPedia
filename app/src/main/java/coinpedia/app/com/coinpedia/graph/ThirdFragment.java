package coinpedia.app.com.coinpedia.graph;

/**
 * Created by senzec on 8/8/17.
 */


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.model.graph.GraphModel;
import coinpedia.app.com.coinpedia.parseApi.ApiClient;
import coinpedia.app.com.coinpedia.parseApi.ApiInterface;
import coinpedia.app.com.coinpedia.ui.CoinDetailActivity;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.FormatterClass;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import coinpedia.app.com.coinpedia.utils.ProgressClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThirdFragment extends Fragment  implements OnChartGestureListener,
        OnChartValueSelectedListener {

    private static final String TAG = "ThirdFragment";

    @BindView(R.id.idTimeTV)
    TextView timeTV;
    @BindView(R.id.idDateRangeStart)
    TextView dateRangeStartTV;
    @BindView(R.id.idDateRangeEnd)
    TextView dateRangeEndTV;

    // DECLARE VARIABLE
    private LineChart mChart;
    private View viewGraphThree;
    ArrayList<String> xValsList = new ArrayList<>();
    ArrayList<Entry> yValsList = new ArrayList<>();
    String strTime;
    Float yAxisVal;

    public ThirdFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGraphThree = inflater.inflate(R.layout.fragment_third, container, false);
        ButterKnife.bind(this, viewGraphThree);
        return viewGraphThree;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) getActivity().findViewById(android.R.id.content));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart = (LineChart) viewGraphThree.findViewById(R.id.linechart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setBorderColor(getResources().getColor(R.color.White));
        mChart.setDescriptionColor(getResources().getColor(R.color.White));
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getAxisLeft().setTextColor(Color.WHITE);
        timeTV.setText("Last Updated: "+new FormatterClass().simpleDate());
        MyMarkerView mv = new MyMarkerView(viewGraphThree.getContext(), R.layout.graph_custom_marker);
        // set the marker to the chart
        mChart.setMarkerView(mv);
        parseGraphIOData3();

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleXIndex() + ", high: " + mChart.getHighestVisibleXIndex());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


    public void parseGraphIOData3()
    {
        //String coinName = new SharedPrefClass(viewGraphOne.getContext()).getCurrentCoin();
        ProgressClass.getProgressInstance().startProgress(viewGraphThree.getContext());
  //      ProgressClass.getProgressInstance().startTimeProgressbar(viewGraphThree.getContext());
        ApiInterface apiInterface = ApiClient.getClient("https://www.coincap.io/").create(ApiInterface.class);
        apiInterface.getGraphMonthData(new SharedPrefClass(viewGraphThree.getContext()).getCurrentCoin()).enqueue(new Callback<GraphModel>() {
            @Override
            public void onResponse(Call<GraphModel> call, Response<GraphModel> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    System.out.println("------------->THREE");
                    try {
                        ProgressClass.getProgressInstance().stopProgress();
                        ArrayList<Float> yList = new ArrayList<Float>();
                        System.out.println(response);
                        List<List<Float>> responseList = response.body().getPrice();

                        for (int i = 0; i < responseList.size(); i++) {
                            List<Float> responseSubList = responseList.get(i);
                            for (int j = 0; j < responseSubList.size(); j++) {
                                if (j == 0) {
                                    Float xAxisVal = responseSubList.get(j);
                                    strTime = dayMonthConversion(xAxisVal);
                                    if (i == 0) {
                                        setStartTime(xAxisVal);
                                    }
                                    if (i == responseList.size() - 1) {
                                        setLastTime(xAxisVal);
                                    }
                                } else if (j == 1) {
                                    yAxisVal = responseSubList.get(j);
                                    yList.add(responseSubList.get(j));
                                }

                            }
                            xValsList.add(strTime);
                            yValsList.add(new Entry(yAxisVal, i));

                        }
                        setOutputTimeRage();
                        float max = Collections.max(yList);
                        float min = Collections.min(yList);
                        Activity activity = getActivity();
                        if (activity != null) {
                            LineDataSet set1;
                            set1 = new LineDataSet(yValsList, "Price");
                            set1.setFillAlpha(30);
                            set1.setColor(getResources().getColor(R.color.app_bar_header));
                            set1.setCircleColor(getResources().getColor(R.color.app_bar_header));
                            set1.setFillColor(getResources().getColor(R.color.app_bar_header));
                            //   set1.setLineWidth(1f);
                            set1.setCircleRadius(0f);
                            set1.setDrawCircleHole(false);
                            set1.setValueTextSize(9f);
                            set1.setDrawFilled(true);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(set1); // add the datasets
                            LineData data = new LineData(xValsList, dataSets);

                            mChart.setData(data);
                            Legend l = mChart.getLegend();
                            l.setForm(Legend.LegendForm.LINE);
                            mChart.setDescription("MAX - " + max + " MIN - " + min);
                            mChart.setNoDataTextDescription("You need to provide data for the chart.");

                            mChart.setTouchEnabled(true);
                            mChart.setDragEnabled(true);
                            mChart.setScaleEnabled(true);
                            LimitLine upper_limit = new LimitLine(max, "Upper Limit");
                            upper_limit.setLineWidth(2f);
                            upper_limit.enableDashedLine(10f, 10f, 0f);
                            upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                            upper_limit.setTextSize(10f);
                            upper_limit.setLineColor(Color.LTGRAY);
                            LimitLine lower_limit = new LimitLine(min, "Lower Limit");
                            lower_limit.setLineWidth(2f);
                            lower_limit.enableDashedLine(10f, 10f, 0f);
                            lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                            lower_limit.setTextSize(10f);
                            lower_limit.setLineColor(Color.LTGRAY);
                            YAxis leftAxis = mChart.getAxisLeft();
                            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                            leftAxis.addLimitLine(upper_limit);
                            leftAxis.addLimitLine(lower_limit);
                            leftAxis.setAxisMaxValue(max);
                            leftAxis.setAxisMinValue(min);
                            leftAxis.enableGridDashedLine(10f, 10f, 0f);
                            leftAxis.setDrawZeroLine(false);
                            leftAxis.setDrawLimitLinesBehindData(true);
                            mChart.getAxisRight().setEnabled(false);
                            mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
                            mChart.invalidate();
                        }
                    }catch (NullPointerException npe)
                    {
                        ProgressClass.getProgressInstance().stopProgress();
                        new SweetAlertDialog(viewGraphThree.getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops... ")
                                .setContentText("No Data for this coin-"+new SharedPrefClass(viewGraphThree.getContext()).getCurrentCoin())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Navigatior.getClassInstance().navigateToActivityContext(viewGraphThree.getContext(), CoinDetailActivity.class);
                                    }
                                })
                                .show();
                        Log.e(TAG, "Error : "+npe, npe);
                    }
                }
            }

            @Override
            public void onFailure(Call<GraphModel> call, Throwable t) {
                call.cancel();
                ProgressClass.getProgressInstance().stopProgress();
            }
        });
        //   return data;
    }

    public String timeConversion(float time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");

        String formattedDate = dateFormat.format(time).toString();
        System.out.println(formattedDate);
        return formattedDate;
    }

    public String dayMonthConversion(float time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMM");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");

        String formattedDate = dateFormat.format(time).toString();
        System.out.println(formattedDate);
        return formattedDate;
    }

    public void setOutputTimeRage()
    {
        dateRangeStartTV.setText(getStartTime());
        dateRangeEndTV.setText(getLastTime());
    }

    //DATE RANGE
    String startTime;
    String lastTime;

    public void setStartTime(float startTime)
    {
        this.startTime = dateConversion(startTime);
    }
    public String getStartTime()
    {
        return startTime;
    }
    public void setLastTime(float lastTime)
    {
        this.lastTime = dateConversion(lastTime);
    }
    public String getLastTime()
    {
        return lastTime;
    }
    public String getDateRange()
    {
        return getStartTime() + " - "+getLastTime();
    }
    public String dateConversion(float time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMM,yy");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");

        String formattedDate = dateFormat.format(time).toString();
        System.out.println(formattedDate);
        return formattedDate;
    }
}