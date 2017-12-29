package coinpedia.app.com.coinpedia.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.utils.Navigatior;

public class BaseActivityWithBack extends AppCompatActivity {

    View itemView;
    ImageView ivLogo;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        itemView = (View) getLayoutInflater().inflate(R.layout.activity_base_with_back, null);
        FrameLayout activityContainer = (FrameLayout) itemView.findViewById(R.id.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivLogo = (ImageView) itemView.findViewById(R.id.idLogoIV);

        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(itemView);
        setSupportActionBar(toolbar);

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigatior.getClassInstance().navigateToActivity(BaseActivityWithBack.this, HomeActivity.class);
            }
        });
    }
}
