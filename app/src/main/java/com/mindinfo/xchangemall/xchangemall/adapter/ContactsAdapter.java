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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.communityActivities.MessageBoxActivity;
import com.mindinfo.xchangemall.xchangemall.beans.ItemsMain;

import org.json.JSONArray;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Mind Info- Android on 09-Nov-17.
 */

public class ContactsAdapter extends BaseAdapter {

    FragmentManager fm;
    String user_id;
    private Activity context;

    public ContactsAdapter(Activity context, List<ItemsMain> albumList, JSONArray jobj, String fragment_name) {
        this.context = context;
    }


    public ContactsAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
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
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.itemlist_contact, null, true);
        final ViewHolder holder = new ViewHolder();


        holder.ItemPriceText = (TextView) rowView.findViewById(R.id.ItemPriceText);
        holder.ItemTitleText = (TextView) rowView.findViewById(R.id.ItemTitleText);

        holder.itemImageView = (CircleImageView) rowView.findViewById(R.id.itemImageView);
        holder.mainLay = (LinearLayout) rowView.findViewById(R.id.mainLay);


        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.ItemPriceText.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);

//        String address = "";
//        try {
//            double lat = Double.parseDouble(responseobj.getString("latitude"));
//            double lng = Double.parseDouble(responseobj.getString("longitude"));
//
//            address = getAddressFromLatlng(new LatLng(lat, lng), context, 0);
//
//            String jobtype = responseobj.getString("job_type");
//            String salary = responseobj.getString("salary_as_per");
//            String job_cat = responseobj.getString("category_name");
//
//            holder.ItemPriceText.setText(address);
//            holder.ItemTitleText.setText(job_cat);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String[] image_array = album.getItem_image().split("~");
//        System.out.println("********* item image *******");
//        System.out.println(image_array[0]);
//
//        Picasso.with(context)
//                .load(image_array[0])
//                .placeholder(R.drawable.no_img)
//                .into(holder.itemImageView);


        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MessageBoxActivity.class));

            }
        });

        return rowView;
    }


    class ViewHolder {
        public TextView ItemPriceText, ItemTitleText;
        public CircleImageView itemImageView;
        LinearLayout mainLay;
    }

}
