package coinpedia.app.com.coinpedia.floating_menu;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;

import coinpedia.app.com.coinpedia.R;

public class BuilderManager {

    private BuilderManager() {
    }

    private static int[] imageResources = new int[]{
            R.drawable.ic_home,
            R.drawable.ic_news,
            R.drawable.ic_calculator,
            R.drawable.ic_notification
         /*   R.drawable.cat,
            R.drawable.deer,
            R.drawable.dolphin,
            R.drawable.eagle,
            R.drawable.horse,
            R.drawable.elephant,
            R.drawable.owl,
            R.drawable.peacock,
            R.drawable.pig,
            R.drawable.rat,
            R.drawable.snake,
            R.drawable.squirrel*/
    };
    private static int[] titleResources = new int[]{
            R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification
     /*       R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification,
            R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification,
            R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification*/



    };
    private static int[] descResources = new int[]{
            R.string.str_home_desc,
            R.string.str_news_desc,
            R.string.str_calculaor_desc,
            R.string.str_notification_desc
     /*       R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification,
            R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification,
            R.string.str_home,
            R.string.str_news,
            R.string.str_calculaor,
            R.string.str_notification*/



    };

    private static int imageResourceIndex = 0;
    private static int titleResourceIndex = 0;
    private static int descResourceIndex = 0;


    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }
    static int getTitleResource() {
        if (titleResourceIndex >= titleResources.length) titleResourceIndex = 0;
        return titleResources[titleResourceIndex++];
    }
    static int getDescResource() {
        if (descResourceIndex >= descResources.length) descResourceIndex = 0;
        return descResources[descResourceIndex++];
    }


    public static HamButton.Builder getPieceCornerRadiusHamButtonBuilder() {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(getTitleResource())
                .subNormalTextRes(getDescResource());
        /*        .normalTextRes(R.string.text_ham_button_text_normal)
                .subNormalTextRes(R.string.text_ham_button_sub_text_normal);*/

    }


}
