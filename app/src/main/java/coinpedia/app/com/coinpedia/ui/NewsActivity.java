package coinpedia.app.com.coinpedia.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nightonke.boommenu.BoomMenuButton;

import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.floating_menu.BuilderManager;
import coinpedia.app.com.coinpedia.newsfeed.NewsMainFragment;
import coinpedia.app.com.coinpedia.utils.ConnectivityManagerClass;
import coinpedia.app.com.coinpedia.utils.Navigatior;

public class NewsActivity extends AppCompatActivity {

    int latStatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(ConnectivityManagerClass.getInstance().isNetworkAvailable(NewsActivity.this) == true) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsMainFragment newsMainFragment = new NewsMainFragment();
        fragmentTransaction.add(R.id.idNewsContainer, newsMainFragment);
        fragmentTransaction.commit();

        }else {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Oops!")
                    .setContentText("Network Failed! Check your Internet")
                    .setCustomImage(R.drawable.ic_disconnected)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Navigatior.getClassInstance().navigateToActivity(NewsActivity.this, HomeActivity.class);
                        }
                    })
                    .show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        for (int i = 0; i < bmb4.getPiecePlaceEnum().pieceNumber(); i++)
            bmb4.addBuilder(BuilderManager.getPieceCornerRadiusHamButtonBuilder());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //      Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                Navigatior.getClassInstance().navigateToActivity(NewsActivity.this, HomeActivity.class);
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Navigatior.getClassInstance().navigateToActivity(NewsActivity.this, HomeActivity.class);
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
    protected void onStop() {
        super.onStop();
        latStatus = 1;
   //     finish();
    }

}
