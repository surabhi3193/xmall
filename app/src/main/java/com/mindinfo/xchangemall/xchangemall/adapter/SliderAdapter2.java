package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.common.FullImageActivity;


import java.util.ArrayList;

public class SliderAdapter2 extends PagerAdapter {

    private ArrayList<Uri> images;

    private int size=0;
    private LayoutInflater inflater;
    private Activity context;
    private boolean clickable=false;

    public SliderAdapter2(Activity context, ArrayList<Uri> images) {
        this.context = context;
        this.images=images;
        this.size=images.size();
        inflater = LayoutInflater.from(context);
        clickable=true;
    }

    public SliderAdapter2(Activity context, ArrayList<Uri> images, int size) {
        this.context = context;
        this.images=images;
        this.size=size;
        inflater = LayoutInflater.from(context);
        clickable=false;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return size;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);

        VideoView videoView =myImageLayout.findViewById(R.id.videoView1);

        if (images.get(position).toString().endsWith("mp4")|| images.get(position).toString().endsWith("mkv")||
        images.get(position).toString().endsWith("avi"))
        {
            System.out.println("------ video url -----");
            System.out.println(images.get(position));
            //Creating MediaController
            MediaController mediaController= new MediaController(context);
            mediaController.setAnchorView(videoView);
            //specify the location of media file

            //Setting MediaController and URI, then starting the videoView
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(images.get(position));
            videoView.requestFocus();
            videoView.seekTo(2000);
            videoView.pause();
        }
        else {
            Glide.with(context).load(images.get(position)).into(myImage);
        }
            if (clickable) {
                myImage.setOnClickListener(view1 -> context.startActivity(new Intent(context, FullImageActivity.class)
                        .putParcelableArrayListExtra("imageArray", images)
                ));
            }
            else
            {
                if (images.get(position).toString().endsWith("mp4")|| images.get(position).toString().endsWith("mkv")||
                        images.get(position).toString().endsWith("avi"))
                {
                    System.out.println("------ video url -----");
                    System.out.println(images.get(position));
                    //Creating MediaController
                    MediaController mediaController= new MediaController(context);
                    mediaController.setAnchorView(videoView);

                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(images.get(position));
                    videoView.requestFocus();
                    videoView.start();
                }
            }

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}