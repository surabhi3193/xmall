package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.mindinfo.xchangemall.xchangemall.beans.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Activity mContext;
    private String act_name;
    private ArrayList<Message> dataSet;

    public MessageAdapter(ArrayList<Message> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final Message message = dataSet.get(position);

        System.out.println("---- data at position ---  " + position);
        System.out.println("----msg ---  " + message.getmessage_text());
        System.out.println("----id---  " + message.getId());
        System.out.println("----sender---  " + message.isMine());

//        System.out.println(message.isMine());

        if (message.isMine()) {
            viewHolder.view_mine.setVisibility(View.VISIBLE);
            viewHolder.view_incoming.setVisibility(View.GONE);
            viewHolder.textview_mine.setText(message.getmessage_text());
            Glide.with(mContext).load(message.getsender_pic()).apply(RequestOptions
                    .placeholderOf(R.drawable.profile_bg)).into(viewHolder.iv_mine);

        } else if (!message.isMine()) {
            viewHolder.view_incoming.setVisibility(View.VISIBLE);
            viewHolder.view_mine.setVisibility(View.GONE);
            viewHolder.textview_incoming.setText(message.getmessage_text());

            Glide.with(mContext).load(message.getsender_pic()).apply(RequestOptions
                    .placeholderOf(R.drawable.car_image)).into(viewHolder.iv_incoming);
        }


    }

    @Override
    public int getItemCount() {
        System.out.println("--- msg length --- " + dataSet.size());
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textview_mine, textview_incoming;
        private ImageView iv_mine, iv_incoming;
        private View view_mine, view_incoming;


        private ViewHolder(View convertView) {
            super(convertView);

            view_mine = convertView.findViewById(R.id.layout_mine);
            view_incoming = convertView.findViewById(R.id.layout_incoming);

            textview_mine = view_mine.findViewById(R.id.textview_mine);
            iv_mine = view_mine.findViewById(R.id.iv_mine);
            textview_incoming = view_incoming.findViewById(R.id.textview_incoming);
            iv_incoming = view_incoming.findViewById(R.id.iv_incoming);

        }
    }
}
