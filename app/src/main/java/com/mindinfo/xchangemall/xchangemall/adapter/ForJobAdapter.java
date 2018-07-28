package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.business_page.Business_postownerProfileActivity;
import com.mindinfo.xchangemall.xchangemall.activities.job_Activities.JobsCatDetailsActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getPostDetails;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;


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

    private void applyJob(int position, String frag_name) {

        try {
            getItem_image = jobj.getJSONObject(position).getString("featured_img");

            ArrayList<String> postarr = new ArrayList<>();

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


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        Typeface face = ResourcesCompat.getFont(context, R.font.estre);
        holder.item_location.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);
        holder.jobtype_textview.setTypeface(face);
        holder.salary_tv.setTypeface(face);

        JSONObject responseobj;
        try {
            responseobj = jobj.getJSONObject(position);

            String address = responseobj.getString("address");

            String jobtype = responseobj.getString("job_type");
            String salary = responseobj.getString("salary_as_per");
            String fav_Status = responseobj.getString("favorite_status");
            String report_Status = responseobj.getString("report_status");
            user_id = responseobj.getString("user_id");
            String getPostTitle = responseobj.getString("title");
            post_id = responseobj.getString("id");
            getItem_image = responseobj.getString("featured_img");

            if (fragment_name.equalsIgnoreCase("business")) {
                String subtitle = responseobj.getString("cat_name");
                holder.Itemsub_titleTv.setVisibility(View.VISIBLE);
                holder.Itemsub_titleTv.setText(subtitle);
                holder.item_location.setText(address);
                holder.ItemTitleText.setText(getPostTitle);
                holder.jobtype_textview.setVisibility(View.INVISIBLE);
                holder.salary_tv.setVisibility(View.INVISIBLE);
            } else if (fragment_name.equalsIgnoreCase("job")) {
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
            if (getItem_image.length() != 0)
                 Glide.with(context).load(getItem_image)
                        .into(holder.itemImageView);
            else {
                holder.itemImageView.setImageResource(R.drawable.no_img_100);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.ImageView_fav.setOnClickListener(view -> {
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


        holder.ImageView_report.setOnClickListener(view -> {
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

        });


        if (fragment_name.equals("business")) {
            holder.buy_now_btn.setText(R.string.view);
        } else if (fragment_name.equals("job")) {
            holder.buy_now_btn.setText(R.string.apply_now);
        }

        holder.mainLay.setOnClickListener(v -> applyJob(position, fragment_name));
        holder.buy_now_btn.setOnClickListener(view -> {

            applyJob(position, fragment_name);
            applyJob(position, fragment_name);

        });


    }

    @Override
    public int getItemCount() {
        return jobj.length();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_jobs, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_location, ItemTitleText, Itemsub_titleTv, jobtype_textview, salary_tv;
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
//            ItemSubTitleText = rowView.findViewById(R.id.ItemSubTitleText);
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
