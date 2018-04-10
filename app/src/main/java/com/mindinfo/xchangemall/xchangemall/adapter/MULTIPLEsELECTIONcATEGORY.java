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

public class MULTIPLEsELECTIONcATEGORY extends BaseAdapter
{
    public static ArrayList<String> idarray=new ArrayList<>();
    public static ArrayList<Integer> pos=new ArrayList<>();
    boolean ischeckd=false;
    private ArrayList<categories> arrayList;
    private Activity context;


    public MULTIPLEsELECTIONcATEGORY(ArrayList<categories> arrayList, Activity context) {
        this.arrayList = arrayList;
        this.context = context;}

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
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.sublist_add_post, null, true);
        final MyViewholder holder = new MyViewholder();
        holder.sub_list_text = (CheckedTextView) rowView.findViewById(R.id.sub_list_text);

        holder.textLAy = (LinearLayout) rowView.findViewById(R.id.textLay);
        final categories cat = arrayList.get(position);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.sub_list_text.setTypeface(face);
        holder.sub_list_text.setTypeface(face);

        holder.sub_list_text.setText(cat.getTitle());
if (idarray.size()>0)
{
    if (idarray.contains(cat.getId()))
        holder.sub_list_text.setChecked(true);
    else
        holder.sub_list_text.setChecked(false);
}


        holder.textLAy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData(context,"cat_id",cat.getId());

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

    class MyViewholder
    {
        public CheckedTextView sub_list_text;
        LinearLayout textLAy;

    }
}
