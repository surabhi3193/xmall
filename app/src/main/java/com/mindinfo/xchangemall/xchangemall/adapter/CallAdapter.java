package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MessageBoxActivity;
import com.mindinfo.xchangemall.xchangemall.beans.Call;
import com.mindinfo.xchangemall.xchangemall.beans.Chat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Mind Info- Android on 09-Nov-17.
 */

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    private Activity context;
    private String act_name;
    private ArrayList<Call> dataSet;

    public CallAdapter(ArrayList<Call> data, Activity context) {
        this.dataSet = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_call, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Call call = dataSet.get(position);

        holder.textview_sender_name.setText(call.getSender_name());
        holder.textview_time.setText(call.getdate());

        Glide.with(context).load(call.getsender_pic()).apply(RequestOptions
                .placeholderOf(R.drawable.profile_bg)).into(holder.iv_pic);

        if (call.isAudio())
            holder.iv_call.setImageResource(R.drawable.ic_audio);
        else if (!call.isAudio())
            holder.iv_call.setImageResource(R.drawable.ic_video);


//        holder.itemView.setOnClickListener(view1 -> {
//            Intent i =new Intent(context, MessageBoxActivity.class);
//            i.putExtra("sender_name",call.getSender_name());
//            i.putExtra("sender_pic",call.getsender_pic());
//            context.startActivity(i);
//
//        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textview_sender_name, textview_time;
        public CircleImageView iv_pic;
        public ImageView iv_call;


        public ViewHolder(View rowView) {
            super(rowView);
            textview_sender_name = rowView.findViewById(R.id.textview_sender_name);
            iv_call = rowView.findViewById(R.id.iv_call);
            textview_time = rowView.findViewById(R.id.textview_time);
            iv_pic = rowView.findViewById(R.id.iv_pic);

        }
    }

}
