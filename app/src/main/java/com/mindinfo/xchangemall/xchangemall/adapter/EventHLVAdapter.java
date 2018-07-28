package com.mindinfo.xchangemall.xchangemall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.intefaces.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Mind Info- Android on 21-Dec-17.
 */

public class EventHLVAdapter extends RecyclerView.Adapter<EventHLVAdapter.ViewHolder> {

    private ArrayList<String> alName;
    private ArrayList<Integer> alImage;
    Context context;

    public EventHLVAdapter(Context context, ArrayList<String> alName, ArrayList<Integer> alImage) {
        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_grid, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nameTV.setText(alName.get(i));
        viewHolder.imgThumbnail.setImageResource(alImage.get(i));

        Typeface face  = ResourcesCompat.getFont(context, R.font.estre);
        viewHolder.nameTV.setTypeface(face);
        viewHolder.eventPriceTV.setTypeface(face);
        viewHolder.eventdateTV.setTypeface(face);
        viewHolder.setClickListener((view, position, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainActivity.class));
            } else {
                Toast.makeText(context, "#" + position + " - " + alName.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alImage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView imgThumbnail;
        private TextView nameTV,eventdateTV,eventPriceTV;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail =itemView.findViewById(R.id.img_thumbnail);
            nameTV =itemView.findViewById(R.id.nameTV);
            eventdateTV =itemView.findViewById(R.id.eventdateTV);
            eventPriceTV =itemView.findViewById(R.id.eventpriceTV);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

}
