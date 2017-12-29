package coinpedia.app.com.coinpedia.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;

/**
 * Created by ravi on 13/9/17.
 */

public class CurrencySelectorAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    Context context;
    String[] name;
    int globalInc = 0;
    ArrayList<ApiToViewModel> frontList;
    public ArrayList<ApiToViewModel> getFrontList() {
        return frontList;
    }

    public void setFrontList(ArrayList<ApiToViewModel> frontList) {
        this.frontList = frontList;
    }


    public CurrencySelectorAdapter(Context context,ArrayList<ApiToViewModel> frontList){
        this.context = context;
        this.frontList = frontList;


    }
    @Override
    public int getCount() {
        return frontList.size();
    }

    @Override
    public ApiToViewModel getItem(int i) {
        return frontList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ApiToViewModel apiToViewModel = frontList.get(position);

        ViewHolder holder;
        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.currency_selector_list_item,null,false);
            holder = new ViewHolder();
            holder.currencyName = (TextView) view.findViewById(R.id.currency_name);
            holder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton checkboxView, boolean isChecked)
            {
                ApiToViewModel c = (ApiToViewModel) checkboxView.getTag();

                if(isChecked)
                {
                    globalInc++;
                    apiToViewModel.setChkStatus("1");
                }
                else if(!isChecked)
                {
                    globalInc--;
                    apiToViewModel.setChkStatus("0");
                }
                if(globalInc >= 5)// it will allow 3 checkboxes only
                {
                    Toast.makeText(context, "Your selectiom limit is execed! " , Toast.LENGTH_LONG).show();
                    checkboxView.setChecked(false);
                    globalInc--;
                }
                System.out.println(" ---------------    "+globalInc);
            }
        });

        if(apiToViewModel.getChkStatus().equals("1")){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.currencyName.setText(frontList.get(position).getLongs());
        return view;
    }
    class ViewHolder {
        TextView currencyName;
        CheckBox checkBox;
    }
}
