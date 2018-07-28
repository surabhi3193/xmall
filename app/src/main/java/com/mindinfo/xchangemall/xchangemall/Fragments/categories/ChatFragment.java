package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.ChatAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.Chat;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    RecyclerView msg_listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

        msg_listview = v.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        msg_listview.setLayoutManager(mLayoutManager);
        msg_listview.setItemAnimator(new DefaultItemAnimator());


        ArrayList<Chat> developerdata = new ArrayList<>();
        Chat developer = new Chat("1", "Hi", "Ailsa Roy", R.drawable.profile_bg, "3:05 PM");
        Chat developer2 = new Chat("2", "Ok", "Russel ", R.drawable.ee_1, "1:19 PM");
        Chat developer3 = new Chat("3", "Bye", "Joy", R.drawable.flag_myr, "11:57 AM");
        Chat developer4 = new Chat("4", "Good Night", " Kelvin", R.drawable.flag_mad, "08:13 AM");
        developerdata.add(developer);
        developerdata.add(developer2);
        developerdata.add(developer3);
        developerdata.add(developer4);


        ChatAdapter mAdapter = new ChatAdapter(developerdata, getActivity());

        msg_listview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        return v;
    }

}