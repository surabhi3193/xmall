package com.mindinfo.xchangemall.xchangemall.Fragments.categories;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.other.Data;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment implements View.OnClickListener {
    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<Data> array;
    private SwipeFlingAdapterView flingContainer;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tinder, null);

        flingContainer = (SwipeFlingAdapterView) v.findViewById(R.id.frame);

        array = new ArrayList<>();
        array.add(new Data(R.drawable.avatar1, "Katrina Kaif","female","Actress","37"));
        array.add(new Data(R.drawable.avatar2, "Emma Watson","female","Actress","37"));
        array.add(new Data(R.drawable.avatar3, "Scarlett Johansson","female","Actress","37"));
        array.add(new Data(R.drawable.avatar4, "Priyanka Chopra","female","Actress","37"));
        array.add(new Data(R.drawable.avatar1, "Deepika Padukone","female","Actress","37"));
        array.add(new Data(R.drawable.avatar2, "Aishwarya Rai","female","Actress","37"));
        myAppAdapter = new MyAppAdapter(array, getActivity().getApplicationContext());
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                array.remove(0);
                myAppAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                array.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                array.add(new Data(R.drawable.avatar1,
                        "Scarlett Johansson","female","Actress","37"));
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                myAppAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                left();
                break;

            case R.id.right:
                right();
                break;

        }
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText,genderText,occupationTExt,ageText,add_friend,joindateTV, locationTV;
        public ImageView cardImage;


    }

    public class MyAppAdapter extends BaseAdapter {


        public List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                viewHolder.occupationTExt = (TextView) rowView.findViewById(R.id.occText);
                viewHolder.ageText = (TextView) rowView.findViewById(R.id.ageText);
                viewHolder.genderText = (TextView) rowView.findViewById(R.id.gendertext);
                viewHolder.add_friend = (TextView) rowView.findViewById(R.id.add_friend);
                viewHolder.joindateTV = (TextView) rowView.findViewById(R.id.joindateTV);
                viewHolder.locationTV = (TextView) rowView.findViewById(R.id.locationTV);

              Typeface  face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                        "fonts/estre.ttf");
              viewHolder.genderText.setTypeface(face);
              viewHolder.DataText.setTypeface(face);
              viewHolder.occupationTExt.setTypeface(face);
              viewHolder.ageText.setTypeface(face);
              viewHolder.add_friend.setTypeface(face);
              viewHolder.joindateTV.setTypeface(face);
              viewHolder.locationTV.setTypeface(face);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getName() + "");
            viewHolder.occupationTExt.setText("Occupation : "+parkingList.get(position).getOccupation());
            viewHolder.ageText.setText("Age : "+parkingList.get(position).getAge());
            viewHolder.genderText.setText("Gender : "+parkingList.get(position).getGender());

            Glide.with(getActivity().getApplicationContext()).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);

            return rowView;
        }
    }
}
