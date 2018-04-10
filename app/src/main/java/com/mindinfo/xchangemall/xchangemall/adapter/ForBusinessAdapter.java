package com.mindinfo.xchangemall.xchangemall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.job_Activities.JobsCatDetailsActivity;
import com.mindinfo.xchangemall.xchangemall.beans.ItemsMain;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;


/**
 * Created by Mind Info- Android on 09-Nov-17.
 */

public class ForBusinessAdapter extends BaseAdapter
{

    public String str_image_arr[];
    FragmentManager fm;
    String user_id;
    JSONArray jobj;
    private Activity context;
    private List<ItemsMain> albumList;
    public ForBusinessAdapter(Activity context, List<ItemsMain> albumList, JSONArray jobj) {
        this.context = context;
        this.albumList = albumList;
        this.jobj = jobj;
    }

    @Override
    public int getCount() {
        return albumList.size();
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
        View rowView= inflater.inflate(R.layout.itemlist_business, null, true);
        final ViewHolder holder = new ViewHolder();

JSONObject responseobj = new JSONObject();
        try {
            responseobj = jobj.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.ItemPriceText = (TextView) rowView.findViewById(R.id.ItemPriceText);
        holder.ItemTitleText = (TextView) rowView.findViewById(R.id.ItemTitleText);
        holder.ItemSubTitleText = (TextView) rowView.findViewById(R.id.ItemSubTitleText);
        holder.jobtype_textview = (TextView) rowView.findViewById(R.id.jobtypeTv);
        holder.salary_tv = (TextView) rowView.findViewById(R.id.salaryTV);
//            ItemReviewText = (TextView) view.findViewById(R.id.ItemReviewText);
        holder.itemImageView = (ImageView) rowView.findViewById(R.id.itemImageView);
        holder.ImageView_fav = (LinearLayout) rowView.findViewById(R.id.ImageView_fav);
        holder.mainLay = (LinearLayout)rowView.findViewById(R.id.mainLay);

        final ItemsMain album = albumList.get(position);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
        holder.ItemPriceText.setTypeface(face);
        holder.ItemTitleText.setTypeface(face);
        holder.jobtype_textview.setTypeface(face);
        holder.salary_tv.setTypeface(face);
        String address="";
        try {
            double lat = Double.parseDouble(responseobj.getString("latitude"));
            double lng = Double.parseDouble(responseobj.getString("longitude"));

             address = getAddressFromLatlng(new LatLng(lat,lng),context,0);

             String jobtype = responseobj.getString("job_type");
             String salary = responseobj.getString("salary_as_per");
             String job_cat = responseobj.getString("category");
             JSONObject titlename = responseobj.getJSONObject("description");


            holder.ItemPriceText.setText(address);
            holder.ItemTitleText.setText(job_cat);
            holder.jobtype_textview.setText(jobtype);
            holder.salary_tv.setText(salary);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Picasso.with(context)
                .load(album.getItem_image())
                .placeholder(R.drawable.no_img)
                .into(holder.itemImageView);

        final JSONObject finalResponseobj = responseobj;

        holder.ImageView_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = getData(context,"user_id","");
//                Send_fav(user_id,album.getMain_cat());
            }
        });


        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextAct = new Intent(context,JobsCatDetailsActivity.class);
                nextAct.putExtra("productDetails",finalResponseobj.toString());
                context.startActivity(nextAct);
            }
        });
        return rowView;
    }

//    @SuppressLint("StaticFieldLeak")
//    private void Send_fav(final String user_id, final String post_id)
//    {
//        new AsyncTask<String, Void, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//                OkHttpClient client = new OkHttpClient();
//                String res_str  = "";
//                try {
//                    res_str = NetworkClass.POST_New(client,"add_favorites",NetworkClass.postMyFav(user_id,post_id));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return res_str;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                Toast.makeText(context,"Add Favorites",Toast.LENGTH_SHORT).show();
//            }
//        }.execute();
//    }

    class ViewHolder {
        public TextView ItemPriceText, ItemTitleText,ItemSubTitleText,jobtype_textview,salary_tv;
        public ImageView itemImageView, overflow;
        public LinearLayout ImageView_fav;
        LinearLayout mainLay;
    }
}
