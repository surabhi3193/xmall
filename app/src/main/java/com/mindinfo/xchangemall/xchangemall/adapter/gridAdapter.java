package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.common.PaymentActivity;
import com.mindinfo.xchangemall.xchangemall.activities.job_Activities.JobsCatDetailsActivity;
import com.mindinfo.xchangemall.xchangemall.activities.property.ApplyForPropertySale;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getPostDetails;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;


public class gridAdapter extends BaseAdapter {

    @SuppressLint("StaticFieldLeak")
    public static View rowView;
    public String str_image_arr[];
    private String fragment_name;
    private JSONArray jobj;
    private String getItem_price, getItem_title, getItem_subtitle, getItem_image, fav_Status, post_id, user_id, report_status;
    private Activity context;


    public gridAdapter(FragmentActivity context, JSONArray jobj, String fragment_name) {
        this.context = context;
        this.jobj = jobj;
        this.fragment_name = fragment_name;
    }

    @Override
    public int getCount() {
        return jobj.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final ViewHolder holder = new ViewHolder();

        rowView = view;
        if (rowView != null) {
            parent = (ViewGroup) rowView.getParent();
            if (parent != null)
                parent.removeView(rowView);
        }
        try {
            rowView = inflater.inflate(R.layout.grid_item_list_card, parent, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }


        JSONObject responseobj;


        holder.buy_now_btn = rowView.findViewById(R.id.buyNow);
        holder.ItemPriceText = rowView.findViewById(R.id.ItemPriceText);
        holder.ItemTitleText = rowView.findViewById(R.id.ItemTitleText);
        holder.ItemPriceText_head = rowView.findViewById(R.id.ItemPriceText_head);
        holder.Item_addess_head = rowView.findViewById(R.id.Item_Address_head);
        holder.ItemTitleText_head = rowView.findViewById(R.id.ItemTitleText_head);
        holder.Item_address = rowView.findViewById(R.id.Item_Address);
        holder.itemImageView = rowView.findViewById(R.id.itemImageView);
        holder.fav_img = rowView.findViewById(R.id.fav_img);
        holder.report_img = rowView.findViewById(R.id.report_img);
        holder.ImageView_fav = rowView.findViewById(R.id.ImageView_fav);
        holder.ImageView_report = rowView.findViewById(R.id.ImageView_report);
        holder.mainLay = rowView.findViewById(R.id.mainLay);


        try {
            responseobj = jobj.getJSONObject(position);

            getItem_image = jobj.getJSONObject(position).getString("featured_img");

            fav_Status = responseobj.getString("favorite_status");
            report_status = responseobj.getString("report_status");
            user_id = responseobj.getString("user_id");
            getItem_title = responseobj.getString("title");
            getItem_subtitle = responseobj.getString("address");
            post_id = responseobj.getString("id");


            if (fragment_name.equals("rental")) {
                holder.buy_now_btn.setVisibility(View.GONE);
                getItem_price = responseobj.getString("price");
            } else if (fragment_name.equals("property_sale")) {

                getItem_price = responseobj.getString("price").split("/")[0];
                holder.buy_now_btn.setVisibility(View.VISIBLE);
                holder.buy_now_btn.setText("Contact Seller");
            }


            System.out.println("********** item position *******");
            System.out.println(position);
            System.out.println("** item at position *****");
            System.out.println(post_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Typeface face = ResourcesCompat.getFont(context, R.font.estre);
        holder.ItemPriceText.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);
        holder.ItemTitleText_head.setTypeface(face);
        holder.Item_addess_head.setTypeface(face);
        holder.ItemPriceText_head.setTypeface(face);
        holder.Item_address.setTypeface(face);


        if (fav_Status.equals("0"))
            holder.fav_img.setImageResource(R.drawable.favv);
        else if (fav_Status.equals("1"))
            holder.fav_img.setImageResource(R.drawable.fav);

        if (report_status.equals("0"))
            holder.report_img.setImageResource(R.drawable.flag_red);
        else if (report_status.equals("1"))
            holder.fav_img.setImageResource(R.drawable.flag_green);

        holder.ItemPriceText.setText(getItem_price);
        holder.ItemTitleText.setText(getItem_title);
        holder.Item_address.setText(getItem_subtitle);

         Glide.with(context)
                .load(getItem_image).apply(RequestOptions.placeholderOf(R.drawable.no_img_100))
                .into(holder.itemImageView);


        holder.mainLay.setOnClickListener(v -> {

            try {
                getItem_image = jobj.getJSONObject(position).getString("featured_img");

                ArrayList<String> postarr = new ArrayList<>();

                postarr.add(getItem_image);

                for (int i = 0; i < postarr.size(); i++) {
                    String image_str = postarr.get(i);
                    str_image_arr = new String[]{image_str};
                }


                post_id = jobj.getJSONObject(position).getString("id");
                user_id = getData(context.getApplicationContext(), "user_id", "");
                System.out.println("** item at click *****");
                System.out.println(post_id);
                getPostDetails(context, user_id, post_id, postarr, JobsCatDetailsActivity.class, fragment_name);

            } catch (JSONException e) {
                e.printStackTrace();

            }

        });
        holder.ImageView_fav.setOnClickListener(view1 -> {
            user_id = getData(context, "user_id", "");
            try {
                post_id = jobj.getJSONObject(position).getString("id");
                System.out.println("** item at click *****");
                System.out.println(post_id);
                Send_fav(user_id, post_id, holder.fav_img, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });


        holder.ImageView_report.setOnClickListener(view12 -> {
            user_id = getData(context, "user_id", "");
            try {
                post_id = jobj.getJSONObject(position).getString("id");
                System.out.println("** item at click *****");
                System.out.println(post_id);
                openReportWarning(user_id, post_id, holder.report_img, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });


        holder.buy_now_btn.setOnClickListener(view13 -> {

            if (fragment_name.equals("property_sale")) {
                try {
                    post_id = jobj.getJSONObject(position).getString("id");
                    Intent applyjob = new Intent(context, ApplyForPropertySale.class);
                    applyjob.putExtra("post_id", post_id);
                    context.startActivity(applyjob);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Intent nextAct = new Intent(context, PaymentActivity.class);
                context.startActivity(nextAct);
            }
        });
        return rowView;
    }


    class ViewHolder {
        public ImageView itemImageView, fav_img, report_img;
        public LinearLayout ImageView_fav, ImageView_report;
        TextView ItemPriceText, ItemTitleText, ItemPriceText_head, ItemTitleText_head, Item_addess_head, Item_address;
        Button buy_now_btn;
        LinearLayout mainLay;
    }
}
