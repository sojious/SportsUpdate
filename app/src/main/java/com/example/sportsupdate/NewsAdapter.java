package com.example.sportsupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsEntry> {
    private final Context mContext;
    private TextView currentItemTitle;
    private TextView currentItemSection;
    private TextView currentItempubDate;

    public NewsAdapter(Context context, int resource, ArrayList<NewsEntry> objects) {
        super(context, resource, objects);
        mContext=context;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_items_res,parent,false);
        }
        NewsEntry currentNewsEntry=MainActivity.newsEntriesList.get(position);
        currentItemTitle = convertView.findViewById(R.id.news_item_title);
        currentItemSection = convertView.findViewById(R.id.news_item_section);
        currentItempubDate = convertView.findViewById(R.id.news_item_pubdate);
        currentItemTitle.setText(currentNewsEntry.getItemtitle());
        currentItemSection.setText(currentNewsEntry.getItemSection());
        currentItempubDate.setText(currentNewsEntry.getItemPublishedDate());
        return convertView;
    }

    @Override
    public int getCount() {
        return MainActivity.newsEntriesList.size();
    }
}
