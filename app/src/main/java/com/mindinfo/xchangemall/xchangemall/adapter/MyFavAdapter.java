package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.beans.MyfavModel;
import com.squareup.picasso.Picasso;

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
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/estre.ttf");

        holder.ItemTitleText.setTypeface(face);
        holder.itemDescription.setTypeface(face);
        holder.ItemPriceText.setTypeface(face);
        //   holder.ItemSubTitleText.setText(album.getItem_subtitle());
        //holder.ItemReviewText.setText(album.getItem_review_text());
//        holder.itemImageView.setImageResource(Integer.parseInt(album.getItem_image()));
        Picasso.with(mContext)
                .load(album.getItem_image())
                .placeholder(R.drawable.no_img)
                .into(holder.itemImageView);

        return rowView;
    }

    class ViewHolder {
        public TextView ItemPriceText, ItemTitleText,itemDescription;
        public ImageView itemImageView;

    }
}
