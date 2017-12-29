package coinpedia.app.com.coinpedia.calculator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.model.calc.CoinListSpinnerModel;
import coinpedia.app.com.coinpedia.utils.Consts;

/**
 * Created by senzec on 9/8/17.
 */

public class CustomCalcSpinnerAdapter extends BaseAdapter {

    Context _context;
    ArrayList<CoinListSpinnerModel> listCoin;
    Typeface font;

    public CustomCalcSpinnerAdapter(Context context, ArrayList<CoinListSpinnerModel> listCoin) {
        super();
        this._context = context;
        this.listCoin = listCoin;
    }

    @Override
    public int getCount() {
        return listCoin.size();
    }

    @Override
    public Object getItem(int position) {
        return listCoin.get(position).getCoinPrice();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      //  ListObject cur_obj = listCoin.get(position);
        LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
        View row = inflater.inflate(R.layout.spn_view_calc, parent, false);
        FontChangeCrawler fontChanger = new FontChangeCrawler(_context.getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup)row);

        //SET FONT
        font = Typeface.createFromAsset( _context.getAssets(), "fontawesome/fonts/fontawesome-webfont.ttf" );

        TextView label = (TextView) row.findViewById(R.id.idRowSPN);
        label.setText(listCoin.get(position).getCoinName());
        TextView downArrow = (TextView) row.findViewById(R.id.idArrowDown);
        downArrow.setText(R.string.down_arrow);
        downArrow.setTypeface(font);



        return row;
    }
}

