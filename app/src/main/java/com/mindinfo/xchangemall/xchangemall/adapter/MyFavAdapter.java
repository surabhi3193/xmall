package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.beans.MyfavModel;


import java.util.List;

public class MyFavAdapter extends BaseAdapter {

    private Activity mContext;
    private List<MyfavModel> albumList;

    public MyFavAdapter(Activity mContext, List<MyfavModel> albumList) {
        this.mContext = mContext;
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.myfav_card, null, true);
        final ViewHolder holder = new ViewHolder();
        MyfavModel album = albumList.get(position);

          holder.ItemTitleText = (TextView)rowView.findViewById(R.id.itemTitle);
         holder.itemDescription = (TextView)rowView.findViewById(R.id.ItemDescription);
         holder.ItemPriceText = (TextView)rowView.findViewById(R.id.ItemPriceText);
        holder.itemImageView = (ImageView)rowView.findViewById(R.id.itemImageView);

        // ItemPriceText, ItemTitleText,ItemSubTitleText,ItemReviewText;
        holder.itemDescription.setText(album.getItem_description());
        holder.ItemTitleText.setText(album.getItem_title());
        holder.ItemPriceText.setText(album.getItem_price());
        Typeface face = ResourcesCompat.getFont(mContext, R.font.estre);

        holder.ItemTitleText.setTypeface(face);
        holder.itemDescription.setTypeface(face);
        holder.ItemPriceText.setTypeface(face);


        Glide.with(mContext)
                .load(album.getItem_image())
                .into(holder.itemImageView);

        return rowView;
    }

    class ViewHolder {
        public TextView ItemPriceText, ItemTitleText,itemDescription;
        public ImageView itemImageView;

    }


}
