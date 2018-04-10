package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.communityActivities.GroupChatScreen;
import com.stfalcon.multiimageview.MultiImageView;


public class GroupAdapter extends BaseAdapter {

    private Activity context;


    public GroupAdapter(Activity context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.grid_group, null, true);
        ViewHolder holder = new ViewHolder();

        holder.buy_now_btn = (Button) rowView.findViewById(R.id.buyNow);
        holder.ItemPriceText = (TextView) rowView.findViewById(R.id.ItemPriceText);
        holder.ItemTitleText = (TextView) rowView.findViewById(R.id.ItemTitleText);
        holder.ItemPriceText_head = (TextView) rowView.findViewById(R.id.ItemPriceText_head);
        holder.Item_addess_head = (TextView) rowView.findViewById(R.id.Item_Address_head);
        holder.ItemTitleText_head = (TextView) rowView.findViewById(R.id.ItemTitleText_head);
        holder.Item_address = (TextView) rowView.findViewById(R.id.Item_Address);
        holder.ImageView_fav = (LinearLayout) rowView.findViewById(R.id.ImageView_fav);
        holder.mainLay = (RelativeLayout) rowView.findViewById(R.id.mainLay);
//        holder.itemImageView = (ImageView) rowView.findViewById(R.id.itemImageView);

        final MultiImageView multiImageView = (MultiImageView)rowView.findViewById(R.id.itemImageView);

        multiImageView.addImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_bg));
        multiImageView.addImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar2));

        multiImageView.setShape(MultiImageView.Shape.CIRCLE);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");



        holder.ItemPriceText.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);
        holder.ItemTitleText_head.setTypeface(face);
        holder.Item_addess_head.setTypeface(face);
        holder.ItemPriceText_head.setTypeface(face);
        holder.Item_address.setTypeface(face);

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {context.startActivity(new Intent(context, GroupChatScreen.class));}
        });

//        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar2);
//        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar3);
//        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar1);
//
//
//        Bitmap[] listBmp = {bitmap1, bitmap2,bitmap3};
//
//        Bitmap merged_img = mergeMultiple(listBmp);
//      Bitmap circle_img= getRoundedShape(merged_img);
//
//        holder.itemImageView.setImageBitmap(circle_img);

        return rowView;

    }
    private Bitmap mergeMultiple(Bitmap[] parts) {

        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        }
        return result;
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 150;
        int targetHeight = 180;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

    class ViewHolder {
        public ImageView itemImageView;
        public LinearLayout ImageView_fav;
        TextView ItemPriceText, ItemTitleText, ItemPriceText_head, ItemTitleText_head, Item_addess_head, Item_address;
        Button buy_now_btn;
        RelativeLayout mainLay;

    }

}