package com.mindinfo.xchangemall.xchangemall.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.intefaces.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Mind Info- Android on 18-Nov-17.
 */

public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

    Context context;
    private ArrayList<String> alName;
    private ArrayList<Integer> alImage;
    private ArrayList<Uri> alImageUrl;

    public HLVAdapter(Context context, ArrayList<String> alName, ArrayList<Integer> alImage) {
        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
    }

    public HLVAdapter(Context context,ArrayList<Uri> alImageUrl, String name) {
        super();
        this.context = context;

        this.alImageUrl = alImageUrl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (alName!=null)
        viewHolder.tvSpecies.setText(alName.get(i));

        if (alImageUrl!=null)
            Glide.with(context).load(alImageUrl.get(i)).into(viewHolder.imgThumbnail);

//        viewHolder.imgThumbnail.setImageResource(alImage.get(i));

//        viewHolder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if (isLongClick) {
//                    Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
//                    context.startActivity(new Intent(context, MainActivity.class));
//                } else {
//                    Toast.makeText(context, "#" + position + " - " + alName.get(position), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {

        int count = 0;
        if (alImageUrl!=null)
            count=alImageUrl.size();
        else if (alImage!=null)
            count= alImage.size();

        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail;
        private TextView tvSpecies;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail =itemView.findViewById(R.id.img_thumbnail);
            tvSpecies =itemView.findViewById(R.id.nameTV);

        }

    }

}
