package coinpedia.app.com.coinpedia.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nightonke.boommenu.BoomMenuButton;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.coindetail.CoinDetailMainFreagment;
import coinpedia.app.com.coinpedia.floating_menu.BuilderManager;
import coinpedia.app.com.coinpedia.utils.Navigatior;

public class CoinDetailActivity extends BaseActivityWithBack {

    private static final String TAG = "CoinDetailActivity";
    int latStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CoinDetailMainFreagment coinDetailMainFreagment = new CoinDetailMainFreagment();
        fragmentTransaction.add(R.id.idDetailContainer, coinDetailMainFreagment);
        fragmentTransaction.commit();

        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        for (int i = 0; i < bmb4.getPiecePlaceEnum().pieceNumber(); i++)
            bmb4.addBuilder(BuilderManager.getPieceCornerRadiusHamButtonBuilder());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //      Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                Navigatior.getClassInstance().navigateToActivity(CoinDetailActivity.this, HomeActivity.class);
                break;
        }
        return true;
    }

    public void onBackPressed() {

        Navigatior.getClassInstance().navigateToActivity(CoinDetailActivity.this, HomeActivity.class);
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
 //       finish();
    }


}
