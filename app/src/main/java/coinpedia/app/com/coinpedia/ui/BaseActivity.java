package coinpedia.app.com.coinpedia.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    View itemView;
    ImageView ivLogo, ivChat;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        itemView = (View) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) itemView.findViewById(R.id.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivLogo = (ImageView) itemView.findViewById(R.id.idLogoIV);
        ivChat = (ImageView) itemView.findViewById(R.id.idChatMenu);


        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(itemView);
        setSupportActionBar(toolbar);

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigatior.getClassInstance().navigateToActivity(BaseActivity.this, HomeActivity.class);
            }
        });

        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Navigatior.getClassInstance().navigateToActivity(BaseActivity.this, ChatActivity.class);
            }
        });



    }


}
