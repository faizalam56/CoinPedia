package coinpedia.app.com.coinpedia.graph;


import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import coinpedia.app.com.coinpedia.R;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(" " + String.valueOf(ce.getHigh()));
        } else {
            tvContent.setText(" " + String.valueOf(e.getVal()));
        }
    }
    @Override
    public int getXOffset(float xpos) {
        if (xpos < 400) {
            return -(getWidth() / 4);
        } else {
            return -(getWidth());
        }
    }
    @Override
    public int getYOffset(float ypos) {
        if (ypos > 320.78235) {
            return getHeight() - 150;
        } else
            return getHeight() / 2;
    }
}