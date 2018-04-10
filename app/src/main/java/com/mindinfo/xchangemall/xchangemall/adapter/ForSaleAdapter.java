package com.mindinfo.xchangemall.xchangemall.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.DetailsFragment;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.common.PaymentActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

/**
 * Created by Mind Info- Android on 21-Nov-17.
 */

public class ForSaleAdapter extends BaseAdapter {

    private String str_image_arr[];
    private String response_fav = "";
    private String user_id;
    private JSONArray jobj;
    private String getItem_image = "";
    private String post_id = "";
    private JSONObject responseDEtailsOBJ;
    private Activity context;
    private String act_name;


    public ForSaleAdapter(Activity context, JSONArray jobj,String act_name) {
        this.context = context;
        this.jobj = jobj;
        this.act_name = act_name;
    }

    @Override
    public int getCount() {
        return jobj.length();
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
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.itemlist_card, null, true);
        final ViewHolder holder = new ViewHolder();

        holder.buy_now_btn = (Button) rowView.findViewById(R.id.buyNow);
        holder.ItemPriceText = (TextView) rowView.findViewById(R.id.ItemPriceText);
        holder.ItemTitleText = (TextView) rowView.findViewById(R.id.ItemTitleText);
        holder.ItemSubTitleText = (TextView) rowView.findViewById(R.id.ItemSubTitleText);
        holder.fav_img = (ImageView) rowView.findViewById(R.id.fav_img);
        holder.report_img = (ImageView) rowView.findViewById(R.id.report_img);
        holder.itemImageView = (ImageView) rowView.findViewById(R.id.itemImageView);
        holder.ImageView_fav = (LinearLayout) rowView.findViewById(R.id.ImageView_fav);
        holder.ImageView_report = (LinearLayout) rowView.findViewById(R.id.ImageView_report);
        holder.mainLay = (LinearLayout) rowView.findViewById(R.id.mainLay);
        holder.share_btn = (LinearLayout) rowView.findViewById(R.id.share_btn);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.ItemPriceText.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);
        holder.buy_now_btn.setTypeface(face);


        JSONObject responseobj = new JSONObject();
        try {
            responseobj = jobj.getJSONObject(position);
            String getItem_price,getItem_title;
            if (act_name.equals("sale"))
            {
                getItem_price = responseobj.getString("price");
                 getItem_title = responseobj.getString("title");
            }

            else
            {
                 getItem_price = responseobj.getString("address");

                getItem_title = responseobj.getString("cat_name");
            }
            getItem_image = jobj.getJSONObject(position).getString("featured_img");
            String fav_Status = responseobj.getString("favorite_status");
            user_id = responseobj.getString("user_id");

            post_id = responseobj.getString("id");


            holder.ItemPriceText.setText(getItem_price);
            holder.ItemTitleText.setText(getItem_title);

            if (fav_Status.equals("0"))
                Picasso.with(context).load(R.drawable.favv).into(holder.fav_img);
            else if (fav_Status.equals("1"))
                Picasso.with(context).load(R.drawable.fav).into(holder.fav_img);

            String report_Status = "";

            if (report_Status.equals("0"))
                Picasso.with(context).load(R.drawable.flag_red).into(holder.report_img);
            else if (report_Status.equals("1"))
                Picasso.with(context).load(R.drawable.flag_green).into(holder.report_img);

            Glide.with(context)
                    .load(getItem_image)
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.no_img)
                    .into(holder.itemImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!act_name.equals("business"))
                    {
                        getItem_image = jobj.getJSONObject(position).getString("featured_img");

                        ArrayList<String> postarr = new ArrayList<String>();

                        postarr.add(getItem_image);

                        for (int i = 0; i < postarr.size(); i++) {
                            String image_str = postarr.get(i);
                            str_image_arr = new String[]{image_str};
                        }


                        post_id = jobj.getJSONObject(position).getString("id");
                        user_id = getData(context.getApplicationContext(), "user_id", "");
                        System.out.println("** item at click *****");
                        System.out.println(post_id);
                        getPostDetails(user_id, post_id, postarr);
                    }

                    else
                    {Toast.makeText(context,"Undet Development",Toast.LENGTH_LONG).show();}
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

            }

        });

        holder.ImageView_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getData(context, "user_id", "");
                try {
                    post_id = jobj.getJSONObject(position).getString("id");
                    System.out.println("** item at click *****");
                    System.out.println(post_id);
                    Send_fav(user_id, post_id, holder.fav_img, context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        holder.ImageView_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getData(context, "user_id", "");
                try {
                    post_id = jobj.getJSONObject(position).getString("id");
                    System.out.println("** item at click *****");
                    System.out.println(post_id);
                    openReportWarning(user_id, post_id, holder.report_img, context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getData(context, "user_id", "");

                ArrayList<String> postarr = new ArrayList<String>();

                try {


                    getItem_image = jobj.getJSONObject(position).getString("featured_img");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                postarr.add(getItem_image);

                for (int i = 0; i < postarr.size(); i++) {
                    String image_str = postarr.get(i);
                    str_image_arr = new String[]{image_str};
                }


                String path = "";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hey , This Product is for sale in XChangeMall. view/download this image");
                try {
                    path = MediaStore.Images.Media.insertImage(context.getContentResolver(), getItem_image, "", null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                context.startActivity(Intent.createChooser(intent, "Share image using"));

            }
        });

        final JSONObject finalResponseobj = responseobj;
        holder.buy_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextAct = new Intent(context, PaymentActivity.class);
                nextAct.putExtra("productDetails", finalResponseobj.toString());
                context.startActivity(nextAct);
            }
        });

        return rowView;
    }

    private void getPostDetails(String user_id, final String post_id, final
    ArrayList<String> postarr) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        params.put("post_id", post_id);
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Loading Post", true);
        ringProgressDialog.setCancelable(false);

        client.post(BASE_URL_NEW + "get_post_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                responseDEtailsOBJ = response;

                ringProgressDialog.dismiss();

                System.out.println(response);
                try {
                    response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONObject obj = response.getJSONObject("result");
                        responseDEtailsOBJ = obj;
                        openNextAct(responseDEtailsOBJ, postarr);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
                Toast.makeText(context, "Unable to get details ,Try again", Toast.LENGTH_SHORT).show();
                responseDEtailsOBJ = errorResponse;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                ringProgressDialog.dismiss();
                Toast.makeText(context, "Unable to get details ,Try again", Toast.LENGTH_SHORT).show();
                System.out.println(responseString);

            }

        });

    }

    private void openNextAct(JSONObject responseDEtailsOBJ, ArrayList<String> postarr) {
        if (responseDEtailsOBJ != null) {
            Bundle bundle = new Bundle();
            bundle.putString("responseObj", responseDEtailsOBJ.toString());

            bundle.putStringArrayList("images", postarr);

            context.getApplicationContext().
                    startActivity(new Intent(context.getApplicationContext(), DetailsFragment.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


    }

    class ViewHolder {
        private TextView ItemPriceText, ItemTitleText, ItemSubTitleText;
        private ImageView itemImageView;
        private ImageView fav_img, report_img;
        private LinearLayout ImageView_fav, share_btn, ImageView_report;
        private Button buy_now_btn;
        private LinearLayout mainLay;
    }
}
