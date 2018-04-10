package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.MessageAdapter;


public  class ChatFragment extends Fragment {

    ListView msg_listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

        msg_listview = (ListView)v.findViewById(R.id.msg_listview);
        MessageAdapter adapter = new MessageAdapter(getActivity());
        msg_listview.setAdapter(adapter);

        return v;
    }

}