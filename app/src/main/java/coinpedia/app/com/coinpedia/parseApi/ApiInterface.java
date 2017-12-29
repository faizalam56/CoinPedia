package coinpedia.app.com.coinpedia.parseApi;

import java.util.List;

import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatInit;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatInitModel;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatOptions;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatOptionsModel;
import coinpedia.app.com.coinpedia.model.coindetail.CoinDetailModel;
import coinpedia.app.com.coinpedia.model.front.FrontModel;
import coinpedia.app.com.coinpedia.model.graph.GraphModel;
import coinpedia.app.com.coinpedia.model.news.NewsfeedModel;
import coinpedia.app.com.coinpedia.notifications.NotificationSettingRequest;
import coinpedia.app.com.coinpedia.notifications.NotificationSettingResponce;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by senzec on 18/7/17.
 */

public interface ApiInterface {

    @GET("front")
    Call<List<FrontModel>> getForntData();

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("get/today/prices/BTC")
    Call<CoinDetailModel> getCoinDetail(@Header("Authorization") String authorization);

    @GET("wp-json/wp/v2/posts?page=1")
    Call<List<NewsfeedModel>> getNewsfeedData();

    @GET("history/1day/{coinName}")
    Call<GraphModel> getGraphDayData(@Path("coinName") String coinName);

    @GET("history/7day/{coinName}")
    Call<GraphModel> getGraphWeekData(@Path("coinName") String coinName);

    @GET("history/30day/{coinName}")
    Call<GraphModel> getGraphMonthData(@Path("coinName") String coinName);

    @POST("set/notification")
    Call<NotificationSettingResponce> updateNotificationSetting(@Body NotificationSettingRequest NotificationSettingRequest);

    @POST("chat_api/chat_bot")
    Call<ChatInitModel> getChatInitResponse(@Body ChatInit chatInit);

    @POST("chat_api/chat_bot")
    Call<ChatOptionsModel> getChatOptionsResponse(@Body ChatOptions chatOptions);



    /*@GET("get/today/prices/BTC")
    Call<CoinDetailModel> getCoinDetail();
*/
/*    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);*/
}
