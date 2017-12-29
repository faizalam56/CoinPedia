package coinpedia.app.com.coinpedia.front;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.util.ArrayList;
import coinpedia.app.com.coinpedia.cache.DownloadImageTask;
import coinpedia.app.com.coinpedia.cache.ImagesCache;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.ui.CoinDetailActivity;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;


public class FrontHolderAdapter extends RecyclerView.Adapter<FrontHolderAdapter.DataViewHolder> implements Filterable{

    Context mContext;
    ArrayList<ApiToViewModel> mFrontList;
    private ArrayList<ApiToViewModel> mFilteredList;
    LayoutInflater inflater;

    public FrontHolderAdapter(Context mContext, ArrayList<ApiToViewModel> mFrontList)
    {
        this.mContext = mContext;
        this.mFrontList = mFrontList;
        mFilteredList = mFrontList;
        inflater = LayoutInflater.from(mContext);
    }
    class DataViewHolder extends RecyclerView.ViewHolder
    {
        TextView mLongTV, mShortTV, mPriceTV, mPercTV;
        ImageView mCoinIV;
        public DataViewHolder(View itemView) {
            super(itemView);
            mCoinIV = (ImageView)itemView.findViewById(R.id.idCoinIC);
            mLongTV = (TextView)itemView.findViewById(R.id.idLongTV);
            mShortTV = (TextView)itemView.findViewById(R.id.idShortTV);
            mPriceTV = (TextView)itemView.findViewById(R.id.idPriceTV);
            mPercTV = (TextView)itemView.findViewById(R.id.idPerTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            //        Toast.makeText(itemView.getContext(), "Test"+getAdapterPosition()+" - - "+mShortTV.getText(), Toast.LENGTH_LONG).show();
                    new SharedPrefClass(itemView.getContext()).setSelectedFront(mShortTV.getText().toString());
                    Navigatior.getClassInstance().navigateToActivityContext(itemView.getContext(), CoinDetailActivity.class);
                }
            });
        }
    }
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.fragment_custom_front, parent, false);
        FontChangeCrawler fontChanger = new FontChangeCrawler(mContext.getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup)itemView);
        DataViewHolder dataViewHolder = new DataViewHolder(itemView);
        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        // GET VALUE
        String percStr = mFilteredList.get(position).getPerc();
        Double priceDbl  = mFilteredList.get(position).getPrice();

        holder.mLongTV.setText(mFilteredList.get(position).getLongs());
        holder.mShortTV.setText(mFilteredList.get(position).getShorts());
        holder.mPriceTV.setText("$"+convertDecimalPrice(priceDbl));
        holder.mPercTV.setText(convertDecimalPerc(percStr)+"%");
        String imgName = mFilteredList.get(position).getLongs().trim().replaceAll("\\s+","");
        String img = "https://coincap.io/images/coins/"+imgName+".png";
        //IMAGE CACHE START
        ImagesCache cache = ImagesCache.getInstance();//Singleton instance handled in ImagesCache class.
        cache.initializeCache();
        Bitmap bm = cache.getImageFromWarehouse(img);
        if(bm != null)
        {
            Glide.with(mContext)
                    .load(bm)
                    .into(holder.mCoinIV);
        }
        else
        {
            Glide.with(mContext)
                    .load("https://coincap.io/images/coins/"+imgName+".png")
                    .into(holder.mCoinIV);

            DownloadImageTask imgTask = new DownloadImageTask(cache, holder.mCoinIV, 300, 300);//Since you are using it from `Activity` call second Constructor.
            imgTask.execute(img);
        }
        //IMAGE CACHE END
        if(Double.parseDouble(percStr)<0)
        {
            holder.mPercTV.setTextColor(Color.parseColor("#FF4444"));
        }
        else {
            holder.mPercTV.setTextColor(mContext.getResources().getColor(R.color.DarkGreen));
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public String convertDecimalPerc(String value)
    {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(Double.parseDouble(value));
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

    //FILTER
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mFrontList;
                } else {
                    ArrayList<ApiToViewModel> filteredList = new ArrayList<>();
                    for (ApiToViewModel apiToViewModel : mFrontList) {
                         if (apiToViewModel.getShorts().toLowerCase().contains(charString)) {
                            filteredList.add(apiToViewModel);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ApiToViewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}