package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.content.Context;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mind Info- Android on 27-Dec-17.
 */

public class CommunityFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    TabLayout tabLayout;
    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, null);

        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        createViewPager(mViewPager);

        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
         tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

createTabIcons();
    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */

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


    private void createTabIcons() {

        Typeface face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");

        TextView tabThree = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Chat");
        tabThree.setTypeface(face);
        tabLayout.getTabAt(0).setCustomView(tabThree);
        TextView tabTwo = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Call");
        tabTwo.setTypeface(face);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabFour = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("Group");
        tabFour.setTypeface(face);
        tabLayout.getTabAt(2).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabFive.setText("Contacts");
        tabFive.setTypeface(face);
        tabLayout.getTabAt(3).setCustomView(tabFive);
    }
    @Override
    public void onClick(View view) {
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