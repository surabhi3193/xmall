package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MessageBoxActivity;
import com.mindinfo.xchangemall.xchangemall.beans.Chat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Mind Info- Android on 09-Nov-17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Activity context;
    private String act_name;
    private ArrayList<Chat> dataSet;

    public ChatAdapter(ArrayList<Chat> data, Activity context) {
        this.dataSet = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_message, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Chat chat = dataSet.get(position);

        holder.textview_sender_name.setText(chat.getSender_name());
        holder.textview_lastmsg.setText(chat.getmessage_text());
        holder.textview_time.setText(chat.getdate());

        Glide.with(context).load(chat.getsender_pic()).apply(RequestOptions
                .placeholderOf(R.drawable.profile_bg)).into(holder.iv_pic);

        holder.itemView.setOnClickListener(view1 -> {
            Intent i =new Intent(context, MessageBoxActivity.class);
            i.putExtra("sender_name",chat.getSender_name());
            i.putExtra("sender_pic",chat.getsender_pic());
            context.startActivity(i);

        });
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
        public TextView textview_sender_name, textview_lastmsg, textview_time;
        public CircleImageView iv_pic;


        public ViewHolder(View rowView) {
            super(rowView);
            textview_sender_name = rowView.findViewById(R.id.textview_sender_name);
            textview_lastmsg = rowView.findViewById(R.id.textview_lastmsg);
            textview_time = rowView.findViewById(R.id.textview_time);
            iv_pic = rowView.findViewById(R.id.iv_pic);

        }
    }

}
