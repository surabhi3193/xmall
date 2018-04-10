package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mindinfo.xchangemall.xchangemall.R;


public class CallFragment extends Fragment {

    ListView msg_listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_call, container, false);
        return v;
    }

}