package com.example.androidrss.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidrss.Interface.ItemClickListener;
import com.example.androidrss.MainActivity;
import com.example.androidrss.Model.RSSObject;
import com.example.androidrss.Model.Webview;
import com.example.androidrss.R;


/**
 * Created by cowlog on 17-10-31.
 */


class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

    public TextView txtTitle, txtPubDate, txtContent;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView)itemView.findViewById(R.id.txtPubDate);
        txtContent = (TextView)itemView.findViewById(R.id.txtContent);

        // Set Event
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);
        return true;
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{

    private RSSObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;
    public FeedAdapter(RSSObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtContent.setText(Html.fromHtml(rssObject.getItems().get(position).getDescription()).toString());
//        holder.txtContent.setText(rssObject.getItems().get(position).getDescription());


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongCilck) {
                if (!isLongCilck) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(rssObject.getItems().get(position).getLink()));
                    //browserIntent.setClass(mContext, WebView.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("link",rssObject.getItems().get(position).getLink());
                    Intent browserIntent = new Intent(mContext,Webview.class);
                    browserIntent.putExtras(bundle);
                    mContext.startActivity(browserIntent);


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (rssObject.items != null) {
            return rssObject.items.size();
        }
        else{return 0;}
    }
}
