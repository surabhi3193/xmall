package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;

/**
 * Created by surbhi on 3/27/2018.
 */

public class HoursAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] days;
    private final String[] startTime;
    private final String[] endTime;

    public HoursAdapter(Activity context,
                        String[] days, String[] startTime, String[] endTime) {
        super(context, R.layout.list_single, days);
        this.context = context;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.hours_operation_list, null, true);

        TextView day_txt = rowView.findViewById(R.id.day_txt);
        TextView start_txt = rowView.findViewById(R.id.start_txt);
        TextView end_txt = rowView.findViewById(R.id.end_txt);


        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        start_txt.setTypeface(face);
        day_txt.setTypeface(face);
        end_txt.setTypeface(face);

        day_txt.setText(days[position]);
        start_txt.setText(startTime[position]);
        end_txt.setText(endTime[position]);

        return rowView;
    }
}