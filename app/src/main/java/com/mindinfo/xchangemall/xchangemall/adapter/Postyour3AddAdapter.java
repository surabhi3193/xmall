package com.mindinfo.xchangemall.xchangemall.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.beans.categories;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class Postyour3AddAdapter extends RecyclerView.Adapter<Postyour3AddAdapter.MyViewholder> {

    private ArrayList<categories> arrayList;
    private Context context;

    public Postyour3AddAdapter(ArrayList<categories> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sublist_text_layout, parent, false);
        return new MyViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewholder holder, int position) {
     final categories cat = arrayList.get(position);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.sub_list_text.setTypeface(face);
        holder.sub_list_text.setTypeface(face);

     holder.sub_list_text.setText(cat.getTitle());

        holder.textLAy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Toast.makeText(context,cat.getTitle()+"is selected ", Toast.LENGTH_SHORT).show();
           saveData(context,"cat_id",cat.getId());
           if (holder.sub_list_text.isChecked())
               holder.sub_list_text.setChecked(false);
                else
               holder.sub_list_text.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class MyViewholder extends RecyclerView.ViewHolder
    {
        public CheckedTextView sub_list_text;
        LinearLayout textLAy;
        public MyViewholder(View itemView)
        {
            super(itemView);
            sub_list_text = (CheckedTextView) itemView.findViewById(R.id.sub_list_text);
            textLAy = (LinearLayout) itemView.findViewById(R.id.textLay);
        }
    }



}

