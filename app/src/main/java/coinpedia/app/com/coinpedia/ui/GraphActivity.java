package coinpedia.app.com.coinpedia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.floating_menu.BuilderManager;
import coinpedia.app.com.coinpedia.graph.GraphMainFragment;
import coinpedia.app.com.coinpedia.utils.Navigatior;


public class GraphActivity extends BaseActivityWithBack {

    int latStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GraphMainFragment graphMainFragment = new GraphMainFragment();
        fragmentTransaction.add(R.id.idGraphContainer, graphMainFragment);
        fragmentTransaction.commit();

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
                Navigatior.getClassInstance().navigateToActivity(GraphActivity.this, CoinDetailActivity.class);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        onBackPressed();
    }
    return super.onKeyDown(keyCode, event);
}

    public void onBackPressed() {
        Intent myIntent = new Intent(GraphActivity.this, CoinDetailActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( latStatus == 1)
        {  recreate();    }
    }

    @Override
    protected void onStop() {
        super.onStop();
        latStatus = 1;
 //       finish();
    }
}
