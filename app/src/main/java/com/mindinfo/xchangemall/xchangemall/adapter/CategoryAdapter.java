package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
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
    public static ArrayList<String> idarray=new ArrayList<>();
    boolean ischeckd=false;
    private boolean checkState[];
    private ArrayList<categories> arrayList;
    private Activity context;

    public CategoryAdapter(ArrayList<categories> arrayList, Activity context) {
        this.arrayList = arrayList;
        this.context = context;
        checkState = new boolean[arrayList.size()];
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
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.sublist_text_layout, null, true);
        final MyViewholder holder = new MyViewholder();

        holder.sub_list_text = (CheckedTextView) rowView.findViewById(R.id.sub_list_text);

        holder.textLAy = (LinearLayout) rowView.findViewById(R.id.textLay);
        final categories cat = arrayList.get(position);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.sub_list_text.setTypeface(face);
        holder.sub_list_text.setTypeface(face);

        holder.sub_list_text.setText(cat.getTitle());


        holder.textLAy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, cat.getTitle() + "is selected ", Toast.LENGTH_SHORT).show();
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
            }
        });
        return rowView;
    }

    class MyViewholder {
        CheckedTextView sub_list_text;
        LinearLayout textLAy;
    }
}
