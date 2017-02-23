package com.nilotpal.newstoday;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<news>>
{

    private static final String LOG_TAG = NewsActivity.class.getName();
    private static String link = "http://content.guardianapis.com/search?q=";
    private static String keyword = "";
    private newsAdapter mAdapter;
    private static final int BOOKS_LOADER_ID = 1;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Button button=(Button) findViewById(R.id.searchButton);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.searchText);
                String replace = String.valueOf(editText.getText());
                StringBuilder formatter = new StringBuilder();
                formatter.append(replace);
                for (int i = 0; i < formatter.length(); i++) {
                    if (formatter.charAt(i) == ' ') {
                        formatter.deleteCharAt(i);
                    }
                }
                keyword = formatter.toString();

                ListView booksListView = (ListView) findViewById(R.id.list);
                booksListView.setEmptyView(mEmptyStateTextView);
                mAdapter = new newsAdapter(NewsActivity.this, new ArrayList<news>());
                booksListView.setAdapter(mAdapter);

                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOKS_LOADER_ID, null, NewsActivity.this);
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        ListView booksListView = (ListView) findViewById(R.id.list);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                news currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Log.e(LOG_TAG, "Loader created");
        return new newsLoader(this, link, keyword);
    }

    @Override
    public void onLoadFinished(Loader<List<news>> loader, List<news> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<news>> loader) {
        Log.e(LOG_TAG, "Loader Reset");
        mAdapter.clear();
    }
}
