package coinpedia.app.com.coinpedia.notifications;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import coinpedia.app.com.coinpedia.R;

/**
 * Created by ravi on 9/8/17.
 */

public class NotificationListFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_notification);
        NotificationAdapter adapter = new NotificationAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
            for(int i=0;i<10;i++){
                recyclerView.setAdapter(adapter);
            }
    }
}

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    Context context;
    public NotificationAdapter(Context context){
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#f0f0f0"));
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_profile_pic;
        TextView notificationTV,timeTV,tv_notification_detail;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_profile_pic = (ImageView) itemView.findViewById(R.id.iv_profile_pic);
//            notificationTV = (TextView) itemView.findViewById(R.id.tv_notification_massage);
            timeTV = (TextView) itemView.findViewById(R.id.tv_time_notification);
            tv_notification_detail = (TextView) itemView.findViewById(R.id.tv_notification_detail);


        }
    }
}