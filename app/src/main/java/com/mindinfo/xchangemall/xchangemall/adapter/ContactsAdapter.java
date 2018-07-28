package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.beans.Contact;
import com.mindinfo.xchangemall.xchangemall.beans.ItemsMain;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Activity context;
    private String act_name;
    private ArrayList<Contact> dataSet;

    public ContactsAdapter(ArrayList<Contact> data, Activity context) {
        this.dataSet = data;
        this.context = context;
    }


    public ContactsAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_contact, parent, false);

        return new ViewHolder(itemView);
        
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Contact contact = dataSet.get(position);


        holder.textview_name.setText(contact.getFriend_name());
        holder.textview_place.setText(contact.getFriend_place());

        Glide.with(context).load(contact.getfriend_pic()).apply(RequestOptions
                .placeholderOf(R.drawable.profile_bg)).into(holder.iv_friend_pic);

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
        public TextView textview_name, textview_place;
        public CircleImageView iv_friend_pic;
        

        public ViewHolder(View rowView) {
            super(rowView);
            textview_name = rowView.findViewById(R.id.textview_name);
            iv_friend_pic = rowView.findViewById(R.id.iv_friend_pic);
            textview_place = rowView.findViewById(R.id.textview_place);
        }
    }


}
