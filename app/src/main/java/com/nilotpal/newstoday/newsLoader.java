package com.nilotpal.newstoday;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.nilotpal.newstoday.news;
import com.nilotpal.newstoday.queryArticles;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class newsLoader extends AsyncTaskLoader<List<news>> {

    private static final String LOG_TAG = newsLoader.class.getName();
    private String mUrl;
    private String mKeyword;

    public newsLoader(Context context, String url, String keyword) {
        super(context);
        mUrl = url;
        mKeyword = keyword;
    }

    protected void onStartLoading() {
        Log.e(LOG_TAG, "Program reaches onStartLoading in booksLoader");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<news> loadInBackground() {
        Log.e(LOG_TAG, "url and keyword in load in background is " + mUrl + mKeyword);
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<news> news = null;
        try {
            news = queryArticles.fetchBooksData(mUrl, mKeyword);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }
}