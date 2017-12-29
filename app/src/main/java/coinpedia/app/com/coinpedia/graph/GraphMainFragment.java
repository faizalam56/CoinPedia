package coinpedia.app.com.coinpedia.graph;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.utils.Consts;

/**
 * Created by senzec on 8/8/17.
 */

public class GraphMainFragment extends Fragment{

    // DECLARE SENTION
    private View viewGraphMain;
    ViewPager simpleViewPager;
    TabLayout tabLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        viewGraphMain = inflater.inflate(R.layout.fragment_main_graph, container, false);
        return viewGraphMain;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), Consts.font1,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) getActivity().findViewById(android.R.id.content));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the reference of ViewPager and TabLayout
        simpleViewPager = (ViewPager) viewGraphMain.findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) viewGraphMain.findViewById(R.id.simpleTabLayout);
// Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("DAY"); // set the Text for the first Tab
    //    firstTab.setIcon(R.drawable.ic_month); // set an icon for the
// first tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
// Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("WEEK"); // set the Text for the second Tab
     //   secondTab.setIcon(R.drawable.ic_month); // set an icon for the second tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
// Create a new Tab named "Third"
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("MONTH"); // set the Text for the first Tab
       // thirdTab.setIcon(R.drawable.ic_month); // set an icon for the first tab
        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

        PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);
// addOnPageChangeListener event change the tab on slide
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        //TAB SELECT LISTNER
        // perform setOnTabSelectedListener event on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        simpleViewPager.setCurrentItem(0);
                  //      tabLayout.setTabTextColors(Color.RED,Color.WHITE);
                        break;
                    case 1:
                        simpleViewPager.setCurrentItem(1);

                        break;
                    case 2:
                        simpleViewPager.setCurrentItem(2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
