package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.beans.categories;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class CategoryAdapter extends BaseAdapter {
    private  ArrayList<String> idarray=new ArrayList<>();
    private boolean ischeckd=false;
    private ArrayList<categories> arrayList;
    private Activity context;

    public CategoryAdapter(ArrayList<categories> arrayList, Activity context) {
        this.arrayList = arrayList;
        this.context = context;
        boolean[] checkState = new boolean[arrayList.size()];
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(final int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.sublist_text_layout, null, true);
        final MyViewholder holder = new MyViewholder();

        holder.sub_list_text =rowView.findViewById(R.id.sub_list_text);

        holder.textLAy =rowView.findViewById(R.id.textLay);
        final categories cat = arrayList.get(position);
        Typeface face = ResourcesCompat.getFont(context, R.font.estre);
        holder.sub_list_text.setTypeface(face);
        holder.sub_list_text.setTypeface(face);

        holder.sub_list_text.setText(cat.getTitle());


        holder.textLAy.setOnClickListener(view1 -> {
          saveData(context, "cat_id", cat.getId());
            ischeckd=true;

            if (!idarray.contains(cat.getId()))
            {
                holder.sub_list_text.setChecked(true);
                idarray.add(cat.getId());
            }
            else
            {
                holder.sub_list_text.setChecked(false);
                idarray.remove(cat.getId());
            }
        });
        return rowView;
    }

    class MyViewholder {
        CheckedTextView sub_list_text;
        LinearLayout textLAy;
    }
}
