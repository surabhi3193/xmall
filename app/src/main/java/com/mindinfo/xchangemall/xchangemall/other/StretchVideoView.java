package com.mindinfo.xchangemall.xchangemall.other;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.VideoView;

public class StretchVideoView extends VideoView {
    Context context;
    public StretchVideoView(Context context) {
        super(context);
        this.context=context;
    }

    public StretchVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public StretchVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //Log.i("@@@@", "onMeasure");

        int mVideoWidth =Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int mVideoHeight=Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = getDefaultSize(width, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if ( mVideoWidth * height  > width * mVideoHeight ) {
                //Log.i("@@@", "image too tall, correcting");
                height = width * mVideoHeight / mVideoWidth;
            } else if ( mVideoWidth * height  < width * mVideoHeight ) {
                //Log.i("@@@", "image too wide, correcting");
                width = height * mVideoWidth / mVideoHeight;
            } else {
                //Log.i("@@@", "aspect ratio is correct: " +
                //width+"/"+height+"="+
                //mVideoWidth+"/"+mVideoHeight);
            }
        }
        //Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
        setMeasuredDimension(width, height);
    }
}