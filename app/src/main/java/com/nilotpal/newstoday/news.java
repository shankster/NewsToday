package com.nilotpal.newstoday;


public class news {
    private String mNewsTitle;

    private String mUrl;

    private String mDate;

    public news(String newsTitle, String date, String url) {
        mNewsTitle = newsTitle;
        mDate = date;
        mUrl = url;
    }

    public String getDate() {
        return mDate;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getUrl() {
        return mUrl;
    }
}
