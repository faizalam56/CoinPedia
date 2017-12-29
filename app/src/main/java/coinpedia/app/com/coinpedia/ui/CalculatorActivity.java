package coinpedia.app.com.coinpedia.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.calculator.CalcMainFragment;
import coinpedia.app.com.coinpedia.utils.Navigatior;

public class CalculatorActivity extends BaseActivityWithBack {

    int latStatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CalcMainFragment calcMainFragment = new CalcMainFragment();
        fragmentTransaction.add(R.id.idCalcContainer, calcMainFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Navigatior.getClassInstance().navigateToActivity(CalculatorActivity.this, HomeActivity.class);
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
                Navigatior.getClassInstance().navigateToActivity(CalculatorActivity.this, HomeActivity.class);
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        latStatus = 1;
     //   finish();
    }
}
