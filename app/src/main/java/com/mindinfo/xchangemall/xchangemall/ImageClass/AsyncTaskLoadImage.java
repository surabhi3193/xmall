package com.mindinfo.xchangemall.xchangemall.ImageClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getResizedBitmap;

public class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
    private final static String TAG = "AsyncTaskLoadImage";
    Context context;
    private ImageView imageView;


    public AsyncTaskLoadImage(ImageView imageView, Context context) {
        this.context = context;
        this.imageView = imageView;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(params[0]);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

             bitmap = BitmapFactory.decodeStream(input);
//            bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return bitmap;
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        Bitmap newbm = getResizedBitmap(bitmap,100,100);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        imageView.setImageBitmap(newbm);
        Glide.with(context)
                .load(newbm)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
} 
