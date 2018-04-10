package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.business_page.Business_postownerProfileActivity;
import com.mindinfo.xchangemall.xchangemall.activities.job_Activities.JobsCatDetailsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getPostDetails;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;


/**
 * Created by Mind Info- Android on 09-Nov-17.
 */

public class ForJobAdapter extends RecyclerView.Adapter<ForJobAdapter.ViewHolder> {


    private String user_id;
    private String post_id;
    private String fragment_name;
    private String getItem_image;
    private JSONArray jobj;
    private Activity context;

    public ForJobAdapter(Activity context, JSONArray jobj, String fragment_name) {
        this.context = context;
        this.jobj = jobj;
        this.fragment_name = fragment_name;
    }

    private void applyJob(int position,String frag_name) {

        try {
            getItem_image = jobj.getJSONObject(position).getString("featured_img");

            ArrayList<String> postarr = new ArrayList<String>();

            postarr.add(getItem_image);

            post_id = jobj.getJSONObject(position).getString("id");
            user_id = getData(context.getApplicationContext(), "user_id", "");
            System.out.println("** item at click *****");
            System.out.println(post_id);
            if (frag_name.equalsIgnoreCase("job"))
            getPostDetails(context, user_id, post_id, postarr, JobsCatDetailsActivity.class, fragment_name);

            if (frag_name.equalsIgnoreCase("business"))
                getPostDetails(context, user_id, post_id, postarr, Business_postownerProfileActivity.class, fragment_name);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openNextAct(JSONObject responseDEtailsOBJ) {

        Intent nextAct = new Intent(context, JobsCatDetailsActivity.class);
        nextAct.putExtra("productDetails", responseDEtailsOBJ.toString());
        nextAct.putExtra("fragment_name", fragment_name);

        context.startActivity(nextAct);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.item_location.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);
        holder.jobtype_textview.setTypeface(face);
        holder.salary_tv.setTypeface(face);


        JSONObject responseobj = new JSONObject();
        try {
            responseobj = jobj.getJSONObject(position);
            double lat = Double.parseDouble(responseobj.getString("latitude"));
            double lng = Double.parseDouble(responseobj.getString("longitude"));

            String address = getAddressFromLatlng(new LatLng(lat, lng), context, 0);

            String jobtype = responseobj.getString("job_type");
            String salary = responseobj.getString("salary_as_per");
            String fav_Status = responseobj.getString("favorite_status");
            String report_Status = responseobj.getString("report_status");
            user_id = responseobj.getString("user_id");
            String getPostTitle = responseobj.getString("title");
            post_id = responseobj.getString("id");
            getItem_image = responseobj.getString("featured_img");

            if (fragment_name.equalsIgnoreCase("business"))
            {
                String subtitle = responseobj.getString("cat_name");
                holder.Itemsub_titleTv.setVisibility(View.VISIBLE);
                holder.Itemsub_titleTv.setText(subtitle);
                holder.item_location.setText(address);
                holder.ItemTitleText.setText(getPostTitle);
                holder.jobtype_textview.setVisibility(View.INVISIBLE);
                holder.salary_tv.setVisibility(View.INVISIBLE);
            }
            else if (fragment_name.equalsIgnoreCase("job"))
            {
                holder.Itemsub_titleTv.setVisibility(View.GONE);
                holder.item_location.setText(address);
                holder.ItemTitleText.setText(getPostTitle);
                holder.jobtype_textview.setText(jobtype);
                holder.salary_tv.setText(salary);

            }
            if (fav_Status.equals("0"))
                holder.fav_img.setImageResource(R.drawable.favv);
            else if (fav_Status.equals("1"))
                holder.fav_img.setImageResource(R.drawable.fav);
            if (report_Status.equals("0"))
                holder.report_img.setImageResource(R.drawable.flag_red);
            else if (report_Status.equals("1"))
                holder.report_img.setImageResource(R.drawable.flag_green);
            if (getItem_image.length() > 3)
                Picasso.with(context).load(getItem_image)
                        .placeholder(R.drawable.no_img)
                        .into(holder.itemImageView);
            else {
                holder.itemImageView.setImageResource(R.drawable.no_img);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.ImageView_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getData(context, "user_id", "");
                try {
                    post_id = jobj.getJSONObject(position).getString("id");
                    System.out.println("** item at click *****");
                    System.out.println(post_id);
                    Send_fav(user_id, post_id, holder.fav_img, context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        holder.ImageView_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getData(context, "user_id", "");
                user_id = getData(context, "user_id", "");
                try {
                    post_id = jobj.getJSONObject(position).getString("id");
                    System.out.println("** item at click *****");
                    System.out.println(post_id);
                    openReportWarning(user_id, post_id, holder.report_img, context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        if (fragment_name.equals("business")) {
            holder.buy_now_btn.setText(R.string.view);
            holder.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applyJob(position,fragment_name);
                }
            });
        } else if (fragment_name.equals("job"))
        {
            holder.buy_now_btn.setText(R.string.apply_now);
            holder.mainLay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    applyJob(position,fragment_name);

                }


            });

            holder.buy_now_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (fragment_name.equals("job"))
                        applyJob(position,fragment_name);
                    if (fragment_name.equalsIgnoreCase("business"))
                        applyJob(position,fragment_name);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return jobj.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_jobs, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_location, ItemTitleText, ItemSubTitleText, Itemsub_titleTv,jobtype_textview, salary_tv;
        private ImageView itemImageView, fav_img, report_img;
        private LinearLayout ImageView_fav, ImageView_report;
        private Button buy_now_btn;
        private LinearLayout mainLay;

        public ViewHolder(View rowView) {
            super(rowView);

            Itemsub_titleTv = rowView.findViewById(R.id.Itemsub_titleTv);
            buy_now_btn = rowView.findViewById(R.id.buyNow);
            item_location = rowView.findViewById(R.id.item_location);
            ItemTitleText = rowView.findViewById(R.id.ItemTitleText);
            ItemSubTitleText = rowView.findViewById(R.id.ItemSubTitleText);
            jobtype_textview = rowView.findViewById(R.id.jobtypeTv);
            salary_tv = rowView.findViewById(R.id.salaryTV);
            itemImageView = rowView.findViewById(R.id.itemImageView);
            ImageView_fav = rowView.findViewById(R.id.ImageView_fav);
            ImageView_report = rowView.findViewById(R.id.ImageView_report);
            mainLay = rowView.findViewById(R.id.mainLay);
            fav_img = rowView.findViewById(R.id.fav_img);
            report_img = rowView.findViewById(R.id.report_img);

        }
    }

}
