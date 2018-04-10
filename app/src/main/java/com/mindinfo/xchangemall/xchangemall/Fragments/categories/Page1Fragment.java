package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities.EventByCat;
import com.mindinfo.xchangemall.xchangemall.adapter.EventHLVAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class Page1Fragment extends Fragment implements View.OnClickListener {

    ArrayList<Integer> alImage;
    ArrayList<String> alName;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private TextView event_owner_nameTV, viewsTv, dateTV, favTV, chatTV, featuredHeadTV, popTV, newsTV, musicTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page1, container, false);

        initui(v);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        alImage = new ArrayList<>(Arrays.asList(R.drawable.live5, R.drawable.live2, R.drawable.live4, R.drawable.live3, R.drawable.live1));
        alName = new ArrayList<>(Arrays.asList("Emmanuelle", "Emmanuelle", "Emmanuelle", "Emmanuelle", "Emmanuelle"));
        mRecyclerView.setHasFixedSize(true);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.my_custom_divider));

        mRecyclerView.addItemDecoration(divider);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new EventHLVAdapter(getActivity().getApplicationContext(), alName, alImage);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void initui(View v) {

        event_owner_nameTV = (TextView) v.findViewById(R.id.eventowner_name);
        viewsTv = (TextView) v.findViewById(R.id.viewscount);
        dateTV = (TextView) v.findViewById(R.id.dateTV);
        favTV = (TextView) v.findViewById(R.id.fav_count);
        chatTV = (TextView) v.findViewById(R.id.chat_count);
        featuredHeadTV = (TextView) v.findViewById(R.id.featured_head);
        popTV = (TextView) v.findViewById(R.id.poptv);
        newsTV = (TextView) v.findViewById(R.id.newsTV);
        musicTV = (TextView) v.findViewById(R.id.musicTV);

        Typeface face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        event_owner_nameTV.setTypeface(face);
        viewsTv.setTypeface(face);
        dateTV.setTypeface(face);
        favTV.setTypeface(face);
        chatTV.setTypeface(face);
        featuredHeadTV.setTypeface(face);
        popTV.setTypeface(face);
        newsTV.setTypeface(face);
        musicTV.setTypeface(face);

        popTV.setOnClickListener(this);
        newsTV.setOnClickListener(this);
        musicTV.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Context context = getActivity().getApplicationContext();
        switch (v.getId()) {
            case R.id.poptv:

               context.startActivity(new Intent(getActivity().getApplicationContext(),
                        EventByCat.class).putExtra("title_name", "Popular")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_in);
                break;
            case R.id.newsTV:

                getActivity().getApplicationContext().startActivity(new Intent(getActivity().getApplicationContext(),
                        EventByCat.class).putExtra("title_name", "News")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_in);
                break;
            case R.id.musicTV:
                getActivity().getApplicationContext().startActivity(new Intent(getActivity().getApplicationContext(),
                        EventByCat.class).putExtra("title_name", "Music")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_in);
                break;
        }
    }
}