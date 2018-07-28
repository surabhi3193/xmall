package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.common.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

/**
 * Created by surbhi on 3/27/2018.
 */

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {

    private final Activity context;
    private final JSONArray jsonArray;
    

    public HoursAdapter(Activity context,JSONArray jsonArray) {
        this.context = context;
  
        this.jsonArray = jsonArray;

    }


    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hours_operation_list, parent, false);
        

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JSONObject responseobj = new JSONObject();

        try {
            responseobj = jsonArray.getJSONObject(position);
            String office_status = responseobj.getString("office_status");
            String open_time2 = responseobj.getString("open_time2");
            String close_time2 = responseobj.getString("close_time2");
            String open_time = responseobj.getString("open_time");
            String close_time = responseobj.getString("close_time");
            String days = responseobj.getString("days");

            holder.day_txt.setText(days);
            holder.start_txt.setText(open_time);
            holder.end_txt.setText(close_time);
            holder.start_txt2.setText(open_time2);
            holder.end_txt2.setText(close_time2);


            if (office_status.equalsIgnoreCase("closed"))
            {
                holder.toTv.setVisibility(View.GONE);
                holder.toTv2.setVisibility(View.GONE);
                holder.end_txt.setVisibility(View.GONE);
                holder.start_txt2.setVisibility(View.GONE);
                holder.end_txt2.setVisibility(View.GONE);

                holder.start_txt.setText(R.string.closed);
                holder.start_txt.setTextColor(context.getResources().getColor(R.color.light_red));
                return;
            }

            if (open_time2.contains("close") || open_time2.length()==0)
            {
                holder.toTv2.setVisibility(View.GONE);
                holder.end_txt2.setVisibility(View.GONE);
                holder.start_txt2.setVisibility(View.GONE);
                holder.toTv.setVisibility(View.VISIBLE);
                holder.end_txt.setVisibility(View.VISIBLE);
            }
            else {
                holder.toTv2.setVisibility(View.VISIBLE);
                holder.end_txt2.setVisibility(View.VISIBLE);
                holder.start_txt2.setVisibility(View.VISIBLE);
                holder.toTv.setVisibility(View.VISIBLE);
                holder.end_txt.setVisibility(View.VISIBLE);

            }

            holder.start_txt.setTextColor(context.getResources().getColor(R.color.black));
            holder.end_txt.setTextColor(context.getResources().getColor(R.color.black));
            holder.start_txt2.setTextColor(context.getResources().getColor(R.color.black));
            holder.end_txt2.setTextColor(context.getResources().getColor(R.color.black));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        
    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView toTv,toTv2, day_txt,start_txt,start_txt2,end_txt,end_txt2;

        public ViewHolder(View rowView) {
            super(rowView);

             toTv = rowView.findViewById(R.id.toTv);
             toTv2 = rowView.findViewById(R.id.toTv2);
             day_txt = rowView.findViewById(R.id.day_txt);
             start_txt = rowView.findViewById(R.id.start_txt);
             end_txt = rowView.findViewById(R.id.end_txt); 
             start_txt2 = rowView.findViewById(R.id.start_txt2);
             end_txt2 = rowView.findViewById(R.id.end_txt2);


        }
    }

}