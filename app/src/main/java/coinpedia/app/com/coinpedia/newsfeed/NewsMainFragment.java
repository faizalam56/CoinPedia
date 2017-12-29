package coinpedia.app.com.coinpedia.newsfeed;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.parseApi.OkHttpClass;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.ProgressClass;

/**
 * Created by senzec on 21/7/17.
 */

public class NewsMainFragment extends Fragment {

    View newsView;
    ListView newsFeedLV;
    ArrayList<NewsFeedModel1> newsFeedList = new ArrayList<>();
   String url1 = "";

    // NAV
    private JellyToolbar toolbar;
    private AppCompatEditText editsearch;
    CustomNewsAdapter customNewsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        newsView = inflater.inflate(R.layout.fragment_news_main, container, false);

        return newsView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), Consts.font3, Consts.font2);
        fontChanger.replaceFonts((ViewGroup) getActivity().findViewById(android.R.id.content));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsFeedLV = (ListView)newsView.findViewById(R.id.idNewsFeedBox);

        toolbarWithNav();

        ProgressClass.getProgressInstance().startProgress(newsView.getContext());
        for(int i  = 0; i<2; i++) {
            parseIOApi2(i);
        }

    }

    public void parseIOApi2(int i)
    {
        new OkHttpClass(newsView.getContext()) {
            @Override
            protected void onPostExecute(String responseJson) {
                super.onPostExecute(responseJson);
                if (responseJson != null) {
//                    ArrayList<NewsFeedModel1> newsFeedList = new ArrayList<>();

                    try {
                        JSONArray jsonArray= null;
                        jsonArray = new JSONArray(responseJson);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String rawTitle= c.getString("title");
                            String actualTitle = new JSONObject(rawTitle).getString("rendered");
                            String dateStr = c.getString("date");
                            String actualDate = UTCtoGMTConversion(dateStr);
                            String rawContent = c.getString("content");
                            String actuaLContent = new JSONObject(rawContent).getString("rendered");
                            //IMAGE LINK
                            String link = c.getString("_links");
                            String rawAttachments = new JSONObject(link).getString("wp:attachment");
                            String attachmentUrl = new JSONArray(rawAttachments).getJSONObject(0).getString("href").toString();

                            new OkHttpClass(newsView.getContext()) {
                                @Override
                                protected void onPostExecute(String responseJson) {
                                    super.onPostExecute(responseJson);
                                    if (responseJson != null) {

                                        JSONArray imageJSONArray= null;
                                        try {
                                            imageJSONArray = new JSONArray(responseJson);

                                        for (int j = 0; j < imageJSONArray.length(); j++) {
                                            JSONObject imageObject = imageJSONArray.getJSONObject(j);
                                            String rawGuid = imageObject.getString("guid");
                                            String actualImageUrl = new JSONObject(rawGuid).getString("rendered");
                                            NewsFeedModel1 newsFeedModel11 = new NewsFeedModel1(actualImageUrl, actualDate,actualTitle, actuaLContent);
                                            newsFeedList.add(newsFeedModel11);

                                            ProgressClass.getProgressInstance().stopProgress();

                                            customNewsAdapter = new CustomNewsAdapter(newsView.getContext(), newsFeedList);
                                            newsFeedLV.setAdapter(customNewsAdapter);
                                        }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }}.execute(attachmentUrl);

                           // String actualAttachments = new JSONObject(rawAttachments).getString("href");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute("http://coinpedia.net/wp-json/wp/v2/posts?page="+i);
        //}.execute(Consts.coinpediaNewsFullURL);

    }

    public String UTCtoGMTConversion(String rawDate)
    {
        Date localDateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            localDateTime = simpleDateFormat.parse(rawDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String actualDate = localDateTime.toString();
        return  actualDate;

    }

    public void toolbarWithNav()
    {
        toolbar = (JellyToolbar) newsView.findViewById(R.id.toolbar);
    //    toolbar.getToolbar().setNavigationIcon(R.drawable.ic_menu);
        toolbar.setJellyListener(jellyListener);

        editsearch = (AppCompatEditText) LayoutInflater.from(newsView.getContext()).inflate(R.layout.edit_text_jelly, null);
        editsearch.setBackgroundResource(R.color.transparent);
        toolbar.setContentView(editsearch);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                customNewsAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(editsearch.getText())) {
                toolbar.collapse();
            } else {
                editsearch.getText().clear();
            }
        }
    };
}
