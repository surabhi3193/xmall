package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.MyFavAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.MyfavModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener {

    String userid;
    TextView headerTv;
    ListView lvMyFav;
    MyFavAdapter adapter;
    ArrayList<MyfavModel> itemList = new ArrayList<>();
    ProgressDialog ringProgressDialog;
    LinearLayout back_btn;
    String mehodname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        lvMyFav = (ListView) findViewById(R.id.favlist);
        back_btn = (LinearLayout) findViewById(R.id.back_arrowImage);
        headerTv = (TextView) findViewById(R.id.headerTv);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");
        headerTv.setTypeface(face);
        userid = getData(getApplicationContext(), "user_id", "");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mehodname = bundle.getString("method_name");

        if (mehodname.equals("my_fav")) {
            headerTv.setText("Favorites");
           getUserFav(userid,"get_favorite_post");
        } else if (mehodname.equals("my_post")) {
            headerTv.setText("My Post");
            getUserFav(userid,"get_my_post");
        }
        adapter = new MyFavAdapter(this, itemList);

        lvMyFav.setAdapter(adapter);
        back_btn.setOnClickListener(this);
    }



//    @SuppressLint("StaticFieldLeak")
//    private void getUserFav1(final String user_id) {
//        ringProgressDialog = ProgressDialog.show(FavoritesActivity.this, "", "Please wait ...", true);
//        ringProgressDialog.setCancelable(false);
//        new AsyncTask<String, Void, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//                OkHttpClient client = new OkHttpClient();
//                String response = "";
//                try {
//                    response = NetworkClass.POST_New(client, "show_favorites_by_user", NetworkClass.getMyFav(user_id));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return response;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                ringProgressDialog.cancel();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONArray jsonArray = jsonObject.getJSONArray("result");
//
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        String id = jsonObject1.getString("post_id");
//                        String price = jsonObject1.getString("price");
//
//                        String etitle = jsonObject1.getString("title");
//
//                        String desc = jsonObject1.getString("description");
//                        String image = jsonObject1.getString("featured_img");
//
//                        itemList.add(new MyfavModel(image, etitle, desc, price));
//                        adapter.notifyDataSetChanged();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.execute();
//    }



    public  void getUserFav(final String user_id,  String method_name){

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        ringProgressDialog = ProgressDialog.show(FavoritesActivity.this, "", "Please wait ...", true);
      ringProgressDialog.setCancelable(false);


      String method =method_name;
        params.put("user_id", user_id);
        client.post(BASE_URL_NEW + method, params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Context context = FavoritesActivity.this;
                    ringProgressDialog.dismiss();
                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(FavoritesActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String id = jsonObject1.getString("" +
                                "id");
                        String price = jsonObject1.getString("price");

                        String etitle = jsonObject1.getString("title");

                        String desc = jsonObject1.getString("description");
                        String image = jsonObject1.getString("featured_img");

                        itemList.add(new MyfavModel(image, etitle, desc, price));
                        adapter.notifyDataSetChanged();


                    }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                Toast.makeText(FavoritesActivity.this, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}
