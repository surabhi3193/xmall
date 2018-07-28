package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.CallAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.Call;

import java.util.ArrayList;


public class CallFragment extends Fragment {

    RecyclerView msg_listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

        msg_listview = v.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        msg_listview.setLayoutManager(mLayoutManager);
        msg_listview.setItemAnimator(new DefaultItemAnimator());


        ArrayList<Call> developerdata = new ArrayList<>();
        Call developer = new Call("1", "Ailsa Roy", R.drawable.profile_bg, "3:05 PM",false);
        Call developer2 = new Call("2", "Russel ", R.drawable.ee_1, "1:19 PM",true);
        Call developer3 = new Call("3",  "Joy", R.drawable.flag_myr, "11:57 AM",false);
        Call developer4 = new Call("4", " Kelvin", R.drawable.flag_mad, "08:13 AM",true);
        Call developer6 = new Call("5",  "Russel", R.drawable.ee_1, "21 July",false);

        Call developer5 = new Call("6",  "Joy", R.drawable.flag_myr, "20 july",false);

        developerdata.add(developer);
        developerdata.add(developer2);
        developerdata.add(developer3);
        developerdata.add(developer4);
        developerdata.add(developer5);
        developerdata.add(developer6);

        CallAdapter mAdapter = new CallAdapter(developerdata, getActivity());

        msg_listview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return v;
    }

}