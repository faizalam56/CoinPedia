package coinpedia.app.com.coinpedia.newsfeed;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.utils.Consts;


public class CustomNewsAdapter extends BaseAdapter {

    Context _context;
    private static final String TAG = "CustomNewsAdapter";
    ArrayList<NewsFeedModel1> newsFeedList;
    private ArrayList<NewsFeedModel1> mFilteredList;
    LayoutInflater inflater;

    CustomNewsAdapter(Context _context, ArrayList<NewsFeedModel1> newsFeedList)
    {
        this._context = _context;
        this.newsFeedList = newsFeedList;
        mFilteredList = newsFeedList;
        inflater = LayoutInflater.from(_context);
    }

    @Override
    public int getCount() {
        return newsFeedList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = inflater.inflate(R.layout.row_notification, viewGroup, false);
        FontChangeCrawler fontChanger = new FontChangeCrawler(_context.getAssets(), Consts.font1, Consts.font2);
        fontChanger.replaceFonts((ViewGroup)itemView);

        ImageView iv = (ImageView)itemView.findViewById(R.id.idNewsIV);
        TextView titleTV = (TextView)itemView.findViewById(R.id.idNewsTitleTV);
        TextView dateTV = (TextView)itemView.findViewById(R.id.idNewsDateTV);
        TextView contentTV = (TextView)itemView.findViewById(R.id.idNewsContentTV);

        try {
            Glide.with(_context)
                    .load(mFilteredList.get(i).getUrl())
                    .error(R.drawable.news1)
                    .into(iv);

            titleTV.setText(Html.fromHtml(mFilteredList.get(i).getTitle()));
            dateTV.setText(mFilteredList.get(i).getDate());
            contentTV.setText(Html.fromHtml(mFilteredList.get(i).getContent()));
        }catch (ArrayIndexOutOfBoundsException aiobe)
        {   Log.e(TAG, "#Error : "+aiobe, aiobe);
        }catch (IndexOutOfBoundsException iobe)
        {   Log.e(TAG, "#Error : "+iobe, iobe);     }
        return itemView;
    }

    // Filter Class
    public void filter(String charText) {
        String charString = charText.toString();
        if (charString.isEmpty()) {
            mFilteredList = newsFeedList;
        } else {
            ArrayList<NewsFeedModel1> filteredList = new ArrayList<>();
            for (NewsFeedModel1 newsFeedModel1 : newsFeedList) {
                if (newsFeedModel1.getTitle().toLowerCase().contains(charString)) {
                    filteredList.add(newsFeedModel1);
                }
            }
            mFilteredList = filteredList;
        }
        notifyDataSetChanged();
    }
}
