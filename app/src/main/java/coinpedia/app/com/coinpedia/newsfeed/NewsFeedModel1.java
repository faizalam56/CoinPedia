package coinpedia.app.com.coinpedia.newsfeed;

/**
 * Created by senzec on 21/7/17.
 */

class NewsFeedModel1 {

    public NewsFeedModel1(String url, String date, String title, String content) {
        this.url = url;
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String url, date, title, content;
}
