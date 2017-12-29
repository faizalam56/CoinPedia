package coinpedia.app.com.coinpedia.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.tapadoo.alerter.Alerter;

import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.floating_menu.BuilderManager;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.notifications.NotificationListFragment;
import coinpedia.app.com.coinpedia.notifications.NotificationSettingRequest;
import coinpedia.app.com.coinpedia.notifications.NotificationSettingResponce;
import coinpedia.app.com.coinpedia.notifications.NotificationsFragment;
import coinpedia.app.com.coinpedia.parseApi.ApiClient;
import coinpedia.app.com.coinpedia.parseApi.ApiInterface;
import coinpedia.app.com.coinpedia.utils.ConnectivityManagerClass;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import coinpedia.app.com.coinpedia.utils.ProgressClass;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends BaseActivityWithBack implements View.OnClickListener ,NotificationsFragment.NotificationFragmentCommunicator{

    Toolbar toolbar;
    ImageView iv_notification,iv_back;
    TextView tv_title;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private String android_id;
    SharedPrefClass pref;
    int latStatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        pref = new SharedPrefClass(this);

        //FLOAT MENU
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        for (int i = 0; i < bmb4.getPiecePlaceEnum().pieceNumber(); i++)
            bmb4.addBuilder(BuilderManager.getPieceCornerRadiusHamButtonBuilder());
        getUdId();

        init();
        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState==null){
            NotificationsFragment notificationsFragment = new NotificationsFragment();
            setFragment(notificationsFragment,"notificationHome");
            notificationsFragment.setNotificationFragmentCommunicator(this);
            tv_title.setText("Notification Alert");
        }

        iv_notification.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) findViewById(android.R.id.content));
    }
    private void getUdId(){
        android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
        pref.setUdId(android_id);
    }
    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_notification=(ImageView)findViewById(R.id.iv_notification);
        tv_title=(TextView) findViewById(R.id.tv_title);
        iv_back=(ImageView)findViewById(R.id.iv_back);
    }
    private void setFragment(Fragment fragment,String tagName){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.notification_container,fragment,tagName);
        fragmentTransaction.addToBackStack(tagName);
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_notification:
                notificationListFragment();
                break;
            case R.id.iv_back:
                fragmentManager.popBackStack("notificationList",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                tv_title.setText("Notification Alert");
                iv_back.setVisibility(View.GONE);
                iv_notification.setVisibility(View.VISIBLE);
        }
    }
    private  void notificationListFragment(){
        NotificationListFragment listFragment = new NotificationListFragment();
        setFragment(listFragment,"notificationList");
        tv_title.setText("Notifications");
        iv_back.setVisibility(View.VISIBLE);
        iv_notification.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        int fragmentBackStack = fragmentManager.getBackStackEntryCount();
        if(fragmentBackStack>1){
            fragmentManager.popBackStack();
            tv_title.setText("Notification Alert");
            iv_back.setVisibility(View.GONE);
            iv_notification.setVisibility(View.VISIBLE);
        }else{
           // NotificationsActivity.this.finish();
        }
        Navigatior.getClassInstance().navigateToActivity(NotificationsActivity.this, HomeActivity.class);
    }
    @Override
    public void submitNotificationRange(NotificationSettingRequest notificationSettingRequest) {

        if(ConnectivityManagerClass.getInstance().isNetworkAvailable(NotificationsActivity.this) == true) {
            ProgressClass.getProgressInstance().startProgress(NotificationsActivity.this);
            ApiInterface apiInterface = ApiClient.getClient(Consts.coinpediaURL).create(ApiInterface.class);
            Call<NotificationSettingResponce> call10 = apiInterface.updateNotificationSetting(notificationSettingRequest);
            call10.enqueue(new Callback<NotificationSettingResponce>() {
                @Override
                public void onResponse(Call<NotificationSettingResponce> call, Response<NotificationSettingResponce> response) {

                    ProgressClass.getProgressInstance().stopProgress();
                    NotificationSettingResponce resource = response.body();
                    if (resource != null) {
                        if (resource.status.equalsIgnoreCase("true")) {
                            //           Toast.makeText(NotificationsActivity.this, "NotificationSettingUpdate successfully", Toast.LENGTH_SHORT).show();
                            showAlertDialog(1);
                        }
                    } else {
                        showAlertDialog(2);
                        // showErrorDialog("something went wrong!");
                    }

                }

                @Override
                public void onFailure(Call<NotificationSettingResponce> call, Throwable t) {
                    ProgressClass.getProgressInstance().stopProgress();
                    call.cancel();
                    // showErrorDialog(t.getMessage());
                    showAlertDialog(2);

                }
            });
        }else {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Oops!")
                    .setContentText("Network Failed! Check your Internet")
                    .setCustomImage(R.drawable.ic_disconnected)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Navigatior.getClassInstance().navigateToActivity(NotificationsActivity.this, HomeActivity.class);
                        }
                    })
                    .show();
        }
    }
    private void showAlertDialog( int status){
        if(status == 1)
        { Alerter.create(NotificationsActivity.this)
                .setTitle("\n Notification Alert")
                .setText("Notification Alert succesfully updated! " +
                        "\nWe will automatically inform you, when price is in range " +
                        "\n")
                .setBackgroundResource(R.color.DarkGreen)
                .enableVibration(true)
                .enableInfiniteDuration(true)
                .enableSwipeToDismiss()
                .show(); }
        else if(status == 2)
        {
            Alerter.create(NotificationsActivity.this)
                    .setTitle("\n Notification Alert")
                    .setText("Oops! Something went wrong. " +
                            "\nWe will correct it soon, Sorry for inconvenience " +
                            "\n")
                    .setBackgroundColorInt(Color.RED)
                    .enableInfiniteDuration(true)
                    .enableSwipeToDismiss()
                    .show();
        }
    }
    private void showErrorDialog(String message){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("ALERT");
        dialog.setMessage(message);

        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if ( latStatus == 1)
        {
            recreate();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //      Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                Navigatior.getClassInstance().navigateToActivity(NotificationsActivity.this, HomeActivity.class);
                break;
        }
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        latStatus = 1;
 //       finish();
    }
}
