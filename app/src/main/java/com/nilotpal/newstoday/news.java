package com.nilotpal.newstoday;


public class news {
    private String mNewsTitle;

    private String mUrl;

    private String mDate;

    private String mSection;
    public news(String newsTitle, String date, String url,String section) {
        mNewsTitle = newsTitle;
        mDate = date;
        mUrl = url;
        mSection=section;
    }

    public String getSection() {
        return mSection;
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
