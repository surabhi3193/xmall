package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.ContactsAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.Contact;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        RecyclerView msg_listview = rootView.findViewById(R.id.listview);

        TextView groupTV = rootView.findViewById(R.id.groupTV);
        TextView inviteTV = rootView.findViewById(R.id.inviteTV);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        msg_listview.setLayoutManager(mLayoutManager);
        msg_listview.setItemAnimator(new DefaultItemAnimator());


        ArrayList<Contact> contactData = new ArrayList<>();

        contactData.add(new Contact("1", "Ailsa Roy", R.drawable.profile_bg, "123456987", "USA", "7855"));
        contactData.add(new Contact("2", "Russel ", R.drawable.ee_1, "987654654", "India", "6325"));
        contactData.add(new Contact("3", "Joy", R.drawable.flag_myr, "32464313", "UK", "1255"));
        contactData.add(new Contact("4", " Kelvin", R.drawable.flag_mad, "465787465", "CANADA", "3698"));
        contactData.add(new Contact("5", "Russel", R.drawable.ee_1, "546546456", "PAKISTAN", "12578"));

        ContactsAdapter mAdapter = new ContactsAdapter(contactData, getActivity());

        msg_listview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        return rootView;
    }
}