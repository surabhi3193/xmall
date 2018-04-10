package com.mindinfo.xchangemall.xchangemall.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Bussiness_Service_Main;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.ItemMainFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.JobsMainFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Property_Rental_Fragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Property_Sale_Fragment;
import com.mindinfo.xchangemall.xchangemall.R;

import java.util.HashMap;
import java.util.List;

import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.ismovedfromHome;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.left_nav_view;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class ExpandableListAdapter  extends BaseExpandableListAdapter {
    Bundle bundle;
    Fragment fragment;
    FragmentManager fm;

    private Context _context;
    private List<Integer> _listHeaderImage; // header titles
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, List<Integer> listHeaderImage) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listHeaderImage = listHeaderImage;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
String[] childdata = childText.split("~");
String title = childdata[0];
final String id = childdata[1];

        Typeface face = Typeface.createFromAsset(_context.getAssets(),
                "fonts/estre.ttf");
        txtListChild.setTypeface(face);
        txtListChild.setText(title);
      txtListChild.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
        openCategoryDetails(id,groupPosition);
          }
      });
        return convertView;
    }

    private void openCategoryDetails(String id, int groupPosition) {

        ismovedfromHome=true;
        bundle = new Bundle();
        fm = ((AppCompatActivity)_context).getSupportFragmentManager();
        switch (groupPosition)
        {

            case 1:

                saveData(_context, "fragment_name", "Service");
                saveData(_context, "pcat_id", "101");
                left_nav_view.setVisibility(View.GONE);
                bundle.putString("MainCatType", "101");
                bundle.putString("subCatID", id);
                fragment = new Bussiness_Service_Main();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.allCategeries,fragment).addToBackStack(null)
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit)
                        .setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit)
                        .commit();

                break;


            case 2:
                saveData(_context, "fragment_name", "property rental");
                saveData(_context, "pcat_id", "102");
                left_nav_view.setVisibility(View.GONE);
                bundle.putString("MainCatType", "102");
                bundle.putString("subCatID", id);
                fragment = new Property_Rental_Fragment();
                fragment.setArguments(bundle);

                fm.beginTransaction().replace(R.id.allCategeries,fragment).addToBackStack(null)
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit)
                        .setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit)
                        .commit();

                break;

                case 3:
                    saveData(_context, "fragment_name", "Jobs");
                    saveData(_context, "pcat_id", "103");
                left_nav_view.setVisibility(View.GONE);
                bundle.putString("MainCatType", "103");
                bundle.putString("subCatID", id);
                fragment = new JobsMainFragment();
                fragment.setArguments(bundle);

                fm.beginTransaction().replace(R.id.allCategeries,fragment).addToBackStack(null)
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit)
                        .setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit)
                        .commit();

                break;


            case 4:
                saveData(_context, "fragment_name", "ForSale");
                saveData(_context, "pcat_id", "104");
                left_nav_view.setVisibility(View.GONE);
                bundle.putString("MainCatType", "104");
                bundle.putString("subCatID", id);
                fragment = new ItemMainFragment();
                fragment.setArguments(bundle);

                fm.beginTransaction().replace(R.id.allCategeries,fragment).addToBackStack(null)
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit)
                        .setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit)
                        .commit();

                break;


                case 7:
                    saveData(_context, "fragment_name", "property for sale");
                    saveData(_context, "pcat_id", "272");
                left_nav_view.setVisibility(View.GONE);
                bundle.putString("MainCatType", "272");
                bundle.putString("subCatID", id);
                fragment = new Property_Sale_Fragment();
                fragment.setArguments(bundle);

                fm.beginTransaction().replace(R.id.allCategeries,fragment).addToBackStack(null)
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit)
                        .setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit)
                        .commit();

                break;

        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView header_img = (ImageView) convertView.findViewById(R.id.header_image);

        Typeface face = Typeface.createFromAsset(_context.getAssets(),
                "fonts/estre.ttf");
        lblListHeader.setTypeface(face);
        header_img.setImageResource(_listHeaderImage.get(groupPosition));
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
