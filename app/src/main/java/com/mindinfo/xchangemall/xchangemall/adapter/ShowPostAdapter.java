package com.mindinfo.xchangemall.xchangemall.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.intefaces.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Mind Info- Android on 21-Dec-17.
 */

public class ShowPostAdapter extends BaseAdapter {

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    Context context;
    private int selectedPosition = -1;
    public ShowPostAdapter(Context context, ArrayList<String> alName, ArrayList<Integer> alImage) {
        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
    }

    @Override
    public int getCount() {
        System.out.println("** adapter size " + alImage.size());
        return alImage.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_showcase, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        System.out.println("** adapter image " + alImage.get(i));
        System.out.println("** adapter name " + alName.get(i));


        viewHolder.nameTV.setText(alName.get(i));
        viewHolder.imgThumbnail.setImageResource(alImage.get(i));

        Typeface face  = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        viewHolder.nameTV.setTypeface(face);

//        viewHolder.nameTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (viewHolder.nameTV.isChecked())
//                    viewHolder.nameTV.setChecked(false);
//                else
//                    viewHolder.nameTV.setChecked(true);
//            }
//        });
        return v;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView nameTV;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.item_image);
            nameTV = (TextView) itemView.findViewById(R.id.item_text);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
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
