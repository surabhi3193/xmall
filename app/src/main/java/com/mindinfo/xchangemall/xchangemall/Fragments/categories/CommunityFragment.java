package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.intefaces.OnBackPressed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mind Info- Android on 27-Dec-17.
 */

public class CommunityFragment extends android.support.v4.app.Fragment implements OnBackPressed {

    TabLayout tabLayout;
    private ViewPager mViewPager;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, null);

        mViewPager = v.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        createViewPager(mViewPager);
        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        createTabIcons();


        return v;
    }

    private void createViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getFragmentManager());
        adapter.addFrag(new ChatFragment(), "Chat");
        adapter.addFrag(new CallFragment(), "Call");
        adapter.addFrag(new GroupFragment(), "Group");
        adapter.addFrag(new ContactsFragment(), "Contacts");
        viewPager.setAdapter(adapter);
    }


    @SuppressLint("SetTextI18n")
    private void createTabIcons() {

        @SuppressLint("InflateParams") TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText(R.string.chat);
        tabThree.setTextColor(getActivity().getResources().getColor(R.color.white));

        tabLayout.getTabAt(0).setCustomView(tabThree);

        @SuppressLint("InflateParams") TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

        tabTwo.setText(R.string.call);
        tabTwo.setTextColor(getActivity().getResources().getColor(R.color.white));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        @SuppressLint("InflateParams") TextView tabFour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabFour.setText(R.string.group);
        tabFour.setTextColor(getActivity().getResources().getColor(R.color.white));
        tabLayout.getTabAt(2).setCustomView(tabFour);

        @SuppressLint("InflateParams") TextView tabFive = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabFive.setText(R.string.contacts);
        tabFive.setTextColor(getActivity().getResources().getColor(R.color.white));
        tabLayout.getTabAt(3).setCustomView(tabFive);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getActivity(),
                MainActivity.class).putExtra("EXIT", true));
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}