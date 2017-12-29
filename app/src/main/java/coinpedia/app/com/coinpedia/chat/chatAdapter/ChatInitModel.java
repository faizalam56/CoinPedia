package coinpedia.app.com.coinpedia.chat.chatAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatInitModel {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("initialOptions")
    @Expose
    private List<String> initialOptions = null;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getInitialOptions() {
        return initialOptions;
    }

    public void setInitialOptions(List<String> initialOptions) {
        this.initialOptions = initialOptions;
    }

}
/*Use this tool offline: Maven plugin Gradle plugin Ant task CLI Java API
        © 2012-2016 Joe Littlejohn Bugs?*/

