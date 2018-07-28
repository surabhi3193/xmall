package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.DetailsFragment;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.common.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

public class ForSaleAdapter extends RecyclerView.Adapter<ForSaleAdapter.ViewHolder> {
    private String response_fav = "";
    private String user_id;
    private JSONArray jobj;
    private String getItem_image = "http://xchange.world/xchange_mall2/uploads/users/";
    private String post_id = "";
    private JSONObject responseDEtailsOBJ;
    private Activity context;
    private String act_name;

    public ForSaleAdapter(Activity context, JSONArray jobj, String act_name) {
        this.context = context;
        this.jobj = jobj;
        this.act_name = act_name;
    }

    private void getPostDetails(String user_id, final String post_id, final ArrayList<String> postarr) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        params.put("post_id", post_id);
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Loading Post", true);
        ringProgressDialog.setCancelable(false);

        System.out.println("------ > sale detail params ---------- > " + params);
        client.post(BASE_URL_NEW + "get_post_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                responseDEtailsOBJ = response;

                ringProgressDialog.dismiss();

                System.out.println(response);
                try {
                    response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        responseDEtailsOBJ = response.getJSONObject("result");
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
                    startActivity(new Intent(context.getApplicationContext(), DetailsFragment.class)
                            .putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject responseobj = new JSONObject();
        try {
            responseobj = jobj.getJSONObject(position);
            String getItem_price, getItem_title;
            if (act_name.equals("sale")) {
                getItem_price = responseobj.getString("price");
                getItem_title = responseobj.getString("title");
            } else {

                getItem_price = responseobj.getString("address");
                getItem_title = responseobj.getString("cat_name");

            }

            if (jobj.getJSONObject(position).getString("featured_img").length() > 0)
                getItem_image = jobj.getJSONObject(position).getString("featured_img");

            String fav_Status = responseobj.getString("favorite_status");
            user_id = responseobj.getString("user_id");

            post_id = responseobj.getString("id");


            holder.ItemPriceText.setText(getItem_price);
            holder.ItemTitleText.setText(getItem_title);

            if (fav_Status.equals("0"))
                Glide.with(context).load(R.drawable.favv).into(holder.fav_img);
            else if (fav_Status.equals("1"))
                Glide.with(context).load(R.drawable.fav).into(holder.fav_img);


            Glide.with(context)
                    .load(getItem_image)
                    .into(holder.itemImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.mainLay.setOnClickListener(v -> {

            try {
                if (!act_name.equals("business")) {
                    getItem_image = jobj.getJSONObject(position).getString("featured_img");

                    ArrayList<String> postarr = new ArrayList<>();

                    postarr.add(getItem_image);
                    post_id = jobj.getJSONObject(position).getString("id");
                    user_id = getData(context.getApplicationContext(), "user_id", "");
                    System.out.println("** item at click *****");
                    System.out.println(post_id);
                    getPostDetails(user_id, post_id, postarr);
                } else {
                    Toast.makeText(context, "Undet Development", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        holder.ImageView_fav.setOnClickListener(view1 -> {
            user_id = getData(context, "user_id", "");
            try {
                post_id = jobj.getJSONObject(position).getString("id");
                System.out.println("** item at click *****");
                System.out.println(post_id);
                Send_fav(user_id, post_id, holder.fav_img, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        holder.ImageView_report.setOnClickListener(view12 -> {
            user_id = getData(context, "user_id", "");
            try {
                post_id = jobj.getJSONObject(position).getString("id");
                System.out.println("** item at click *****");
                System.out.println(post_id);
                openReportWarning(user_id, post_id, holder.report_img, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });


        holder.share_btn.setOnClickListener(view13 -> {
            user_id = getData(context, "user_id", "");

            try {
                getItem_image = jobj.getJSONObject(position).getString("featured_img");
            } catch (Exception e) {
                e.printStackTrace();
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

        });

        final JSONObject finalResponseobj = responseobj;

        holder.buy_now_btn.setOnClickListener(view14 -> {
            Intent nextAct = new Intent(context, PaymentActivity.class);
            nextAct.putExtra("productDetails", finalResponseobj.toString());
            context.startActivity(nextAct);
        });
    }

    @Override
    public int getItemCount() {
        return jobj.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ItemPriceText, ItemTitleText;
        private ImageView itemImageView;
        private ImageView fav_img, report_img;
        private LinearLayout ImageView_fav, share_btn, ImageView_report;
        private Button buy_now_btn;
        private LinearLayout mainLay;

        public ViewHolder(View rowView) {
            super(rowView);

            buy_now_btn = rowView.findViewById(R.id.buyNow);
            ItemPriceText = rowView.findViewById(R.id.ItemPriceText);
            ItemTitleText = rowView.findViewById(R.id.ItemTitleText);
//        holder.ItemSubTitleText =rowView.findViewById(R.id.ItemSubTitleText);
            fav_img = rowView.findViewById(R.id.fav_img);
            report_img = rowView.findViewById(R.id.report_img);
            itemImageView = rowView.findViewById(R.id.itemImageView);
            ImageView_fav = rowView.findViewById(R.id.ImageView_fav);
            ImageView_report = rowView.findViewById(R.id.ImageView_report);
            mainLay = rowView.findViewById(R.id.mainLay);
            share_btn = rowView.findViewById(R.id.share_btn);

            Typeface face =  ResourcesCompat.getFont(context, R.font.estre);
            ItemPriceText.setTypeface(face);
            ItemTitleText.setTypeface(face);
            buy_now_btn.setTypeface(face);

        }
    }
}