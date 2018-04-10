package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities.EventDetailActivity;
import com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities.EventItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mind Info- Android on 21-Dec-17.
 */

public class EventAdapter extends BaseAdapter {

    public String str_image_arr[];
    FragmentManager fm;
    String user_id;

    private Activity context;
    private List<EventItem> albumList;

    public EventAdapter(Activity context, List<EventItem> albumList) {
        this.context = context;
        this.albumList = albumList;

    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.itemlist_event, null, true);
        final ViewHolder holder = new ViewHolder();

        holder.event_owner_nameTV = (TextView) v.findViewById(R.id.eventowner_name);
        holder.viewsTv = (TextView) v.findViewById(R.id.viewscount);
        holder.dateTV = (TextView) v.findViewById(R.id.dateTV);
        holder.favTV = (TextView) v.findViewById(R.id.fav_count);
        holder.chatTV = (TextView) v.findViewById(R.id.chat_count);

        holder.item_image = (ImageView) v.findViewById(R.id.item_image);
        holder.mainLay = (LinearLayout) v.findViewById(R.id.mainLay);

        final EventItem album = albumList.get(position);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.event_owner_nameTV.setTypeface(face);
        holder.viewsTv.setTypeface(face);
        holder.dateTV.setTypeface(face);
        holder.favTV.setTypeface(face);
        holder.chatTV.setTypeface(face);

        Picasso.with(context)
                .load(album.getEvent_vdo_thumbnail())
                .placeholder(R.drawable.no_img)
                .into(holder.item_image);

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,EventDetailActivity.class).
                        putExtra("item_image",album.getEvent_vdo_thumbnail())
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return v;
    }

    class ViewHolder {
        public TextView event_owner_nameTV,viewsTv,dateTV,favTV,chatTV,featuredHeadTV,popTV,newsTV,musicTV;
        public ImageView item_image;

        public Button buy_now_btn;
        LinearLayout mainLay;
    }}