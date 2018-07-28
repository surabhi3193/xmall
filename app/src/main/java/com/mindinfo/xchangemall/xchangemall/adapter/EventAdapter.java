package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities.EventDetailActivity;
import com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities.EventItem;


import java.util.List;
import java.util.Objects;

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
        @SuppressLint({"ViewHolder", "InflateParams"}) View v = inflater.inflate(R.layout.itemlist_event, null, true);
        final ViewHolder holder = new ViewHolder();

        holder.event_owner_nameTV =v.findViewById(R.id.eventowner_name);
        holder.viewsTv =v.findViewById(R.id.viewscount);
        holder.dateTV =v.findViewById(R.id.dateTV);
        holder.favTV =v.findViewById(R.id.fav_count);
        holder.chatTV =v.findViewById(R.id.chat_count);

        holder.item_image =v.findViewById(R.id.item_image);
        holder.mainLay =v.findViewById(R.id.mainLay);

        final EventItem album = albumList.get(position);
        Typeface face = ResourcesCompat.getFont(context, R.font.estre);
        holder.event_owner_nameTV.setTypeface(face);
        holder.viewsTv.setTypeface(face);
        holder.dateTV.setTypeface(face);
        holder.favTV.setTypeface(face);
        holder.chatTV.setTypeface(face);

         Glide.with(context)
                .load(album.getEvent_vdo_thumbnail())
                .into(holder.item_image);

        holder.mainLay.setOnClickListener(view1 -> context.startActivity(new Intent(context,EventDetailActivity.class).
                putExtra("item_image",album.getEvent_vdo_thumbnail())
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        return v;
    }

    class ViewHolder {
        private TextView event_owner_nameTV,viewsTv,dateTV,favTV,chatTV,featuredHeadTV,popTV,newsTV,musicTV;
        public ImageView item_image;

        public Button buy_now_btn;
        LinearLayout mainLay;
    }}