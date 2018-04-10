package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.GroupAdapter;

/**
 * Created by Mind Info- Android on 27-Dec-17.
 */

public  class GroupFragment extends Fragment {

    TextView groupTV,inviteTV;
    GridView recyclerViewItem;
    GroupAdapter itemlistAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        groupTV =(TextView)v.findViewById(R.id.groupTV);
        inviteTV =(TextView)v.findViewById(R.id.inviteTV);

       Typeface face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");

       groupTV.setTypeface(face);
       inviteTV.setTypeface(face);

        itemlistAdapter=new GroupAdapter(getActivity());
        recyclerViewItem = (GridView) v.findViewById(R.id.recyclerViewItem);
        recyclerViewItem.setAdapter(itemlistAdapter);
        return v;

    }

}