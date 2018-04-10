package com.mindinfo.xchangemall.xchangemall.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SlideImageAdapter extends PagerAdapter {

    private ArrayList<Integer> images;
    private ArrayList<Uri> uris;
    private LayoutInflater inflater;
    private Context context;

    public SlideImageAdapter(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    public SlideImageAdapter(ArrayList<Uri> uris, Context context) {
        this.uris = uris;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
//        return images.size();
        return uris.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
//        myImage.setImageResource(images.get(position));
      Picasso.with(context).load(uris.get(position)).placeholder(R.drawable.profile_bg).into(myImage);
//   myImage.setImageURI(uris.get(position));

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
