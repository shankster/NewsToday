package com.nilotpal.newstoday;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class newsAdapter extends ArrayAdapter<news> {
    private static final String LOG_TAG=newsAdapter.class.getName();

    public newsAdapter(Context context, List<news> books) {
        super(context, 0, books);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_newslist, parent, false);
        }

        news newNews=getItem(position);

        TextView newsName=(TextView)listItemView.findViewById(R.id.date);
        newsName.setText(newNews.getDate());

        TextView articleDate=(TextView)listItemView.findViewById(R.id.newsListing);
        articleDate.setText(newNews.getNewsTitle());
        return listItemView;
    }
}
