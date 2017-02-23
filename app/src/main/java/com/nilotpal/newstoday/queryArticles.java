package com.nilotpal.newstoday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class queryArticles extends AppCompatActivity {
    private static final String LOG_TAG = queryArticles.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
    }

    public static URL createUrl(String stringUrl, String keyword) throws MalformedURLException {
        Log.e(LOG_TAG, "Program reaches createUrl");
        String finalLink = stringUrl + keyword+"&api-key=test";
        URL url = null;
        url = new URL(finalLink);
        Log.e(LOG_TAG,"The final URL is "+finalLink);
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.e(LOG_TAG, "Program reaches makeHttpRequest");
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamRead = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader read = new BufferedReader(inputStreamRead);
            String line = read.readLine();
            while (line != null) {
                output.append(line);
                line = read.readLine();
            }
        }
        Log.e(LOG_TAG, "Program reaches readFromStream");
        return output.toString();
    }

    private static List<news> extractInfoFromJson(String newsJson) throws JSONException {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        Log.e(LOG_TAG, "extractFromJson started");

        List<news> news = new ArrayList<>();

        JSONObject rootdoc=new JSONObject(newsJson);
        JSONObject getResponse=rootdoc.getJSONObject("response");
        JSONArray getResults=getResponse.getJSONArray("results");
        for (int i=0;i<getResults.length();i++){
            JSONObject currentElement = getResults.getJSONObject(i);
            String newsTitle=currentElement.getString("webTitle");
            String newsDate=currentElement.getString("webPublicationDate").substring(0,10);
            String newsUri=currentElement.getString("webUrl");
            String sectionInfo=currentElement.getString("sectionName");

            Log.e(LOG_TAG,newsTitle);
            news newNews=new news(newsTitle,newsDate,newsUri,sectionInfo);
            news.add(newNews);

        }
        return news;
    }

    public static List<news> fetchBooksData(String requestUrl, String keyword) throws IOException, JSONException {
        URL url = createUrl(requestUrl, keyword);
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url);

        List<news> books = extractInfoFromJson(jsonResponse);
        Log.e(LOG_TAG, "Program reaches fetchBooksData");
        return books;

    }
}
