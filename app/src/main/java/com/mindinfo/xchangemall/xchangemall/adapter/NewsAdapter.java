package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.DetailsFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.GamesFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.NewsFragment;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.business_page.Business_postownerProfileActivity;
import com.mindinfo.xchangemall.xchangemall.activities.job_Activities.JobsCatDetailsActivity;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.activities.news.NewsDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getPostDetails;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private String user_id;
    private JSONArray jobj;
    private Activity context;
    private NewsFragment newsfragment;
    private GamesFragment gamesfragment;

    public NewsAdapter(Activity context, JSONArray jobj,NewsFragment newsfragment) {
        this.context = context;
        this.jobj = jobj;
        this.newsfragment = newsfragment;
    }
    public NewsAdapter(Activity context, JSONArray jobj,GamesFragment gamesfragment) {
        this.context = context;
        this.jobj = jobj;
        this.gamesfragment = gamesfragment;
    }

    public NewsAdapter(Activity context,NewsFragment newsfragment) {
        this.context = context;
        this.newsfragment = newsfragment;
    }
    public NewsAdapter(Activity context,GamesFragment gamesfragment) {
        this.context = context;
        this.gamesfragment = gamesfragment;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
        JSONObject responseobj;
        String news_url = "";

            if (newsfragment!=null)
            {
                responseobj = jobj.getJSONObject(position);
                String news_id = responseobj.getString("id");
                String title = responseobj.getString("title");
                String featured_img = responseobj.getString("featured_img");
                String news_desc = responseobj.getString("news_desc");
                 news_url = responseobj.getString("news_url");

                holder.titleTv.setText(title);
                holder.descTv.setText(news_desc);
                holder.itemImageView.setImageResource(R.drawable.no_img_100);

                if (featured_img.length()>0)
                    Glide.with(context).load(featured_img).into(holder.itemImageView);
            }



        if (gamesfragment!=null)
        {
            responseobj = jobj.getJSONObject(position);
            String games_id = responseobj.getString("id");
            String title = responseobj.getString("title");
            String featured_img = responseobj.getString("featured_img");
            String description = responseobj.getString("description");
            news_url = responseobj.getString("games_url");

            holder.titleTv.setText(title);
            holder.descTv.setText(description);
            holder.itemImageView.setImageResource(R.drawable.no_img_100);

            if (featured_img.length()>0)
                Glide.with(context).load(featured_img).into(holder.itemImageView);


              holder.readmoreTv.setBackgroundResource(R.drawable.blue_btn);
              holder.readmoreTv.setTextColor(context.getResources().getColor(R.color.white));
              holder.readmoreTv.setText("Play");

        }
        String finalNews_url = news_url;
        holder.readmoreTv.setOnClickListener(view -> {

            String fragment_name="";
            if (newsfragment!=null) {
                fragment_name = "news";
               newsfragment.openWebView(finalNews_url);
            }

            else if (gamesfragment!=null) {
                fragment_name = "games";
                gamesfragment.openWebView(finalNews_url);
            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jobj.length();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_row, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv,descTv,readmoreTv;
        private ImageView itemImageView;
        private LinearLayout mainLay;
        public ViewHolder(View rowView) {
            super(rowView);
            titleTv = rowView.findViewById(R.id.titleTv);
            descTv = rowView.findViewById(R.id.descTv);
            readmoreTv = rowView.findViewById(R.id.readmoreTv);
            itemImageView = rowView.findViewById(R.id.itemImageView);
        }
    }

}
