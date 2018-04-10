package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.ContactsAdapter;

/**
 * Created by Mind Info- Android on 27-Dec-17.
 */

public class ContactsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
       ListView msg_listview = (ListView)rootView.findViewById(R.id.listview);

        TextView     groupTV =(TextView)rootView.findViewById(R.id.groupTV);
        TextView  inviteTV =(TextView)rootView.findViewById(R.id.inviteTV);

        ContactsAdapter adapter = new ContactsAdapter(getActivity());
        msg_listview.setAdapter(adapter);

        Typeface face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");

        groupTV.setTypeface(face);
        inviteTV.setTypeface(face);
        return rootView;
    }
}