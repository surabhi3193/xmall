package com.mindinfo.xchangemall.xchangemall.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.EnterLogin;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mindinfo.xchangemall.xchangemall.storage.MySharedPref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.iscatChecked;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.isdogChecked;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.NullData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class NetworkClass extends AppCompatActivity {

    static JSONObject responseDEtailsOBJ;
    //POST network request

    public static void getListData(String str, ArrayList<categories> list, Context context) {
        String str_s = getData(context, "categoriesdata_full", "");
        System.out.println("******** response categori *********");
        System.out.println(str_s);
        try {
            JSONArray jsonArray = new JSONArray(str_s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String pcat = jsonObject1.getString("id");
                System.out.println(str);
                System.out.println(pcat);
                if (pcat.equals(str)) {
                    System.out.println("******* respose list cat ***");
                    System.out.println(jsonObject1);

                    JSONArray newjArray = jsonObject1.getJSONArray("data");
                    for (int j = 0; j < newjArray.length(); j++)
                    {
                        JSONObject joboj = newjArray.getJSONObject(j);
                        String title = joboj.getString("title");
                        String id = joboj.getString("id");
                        categories cat = new categories(id, title);
                        list.add(cat);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void send_report(final String user_id, final String post_id,

                                   final ImageView holder, final Activity context) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        params.put("post_id", post_id);

        System.out.println(params);

        if (user_id.length() <= 0)
            OpenWarning(context);


        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Sending report", true);
        ringProgressDialog.setCancelable(false);

        client.post(BASE_URL_NEW + "add_report_post", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println("**** report api ********* " + post_id);
                System.out.println(response);
                try {
                    int response_fav = response.getInt("status");

                    if (response_fav==1)
                        Glide.with(context).load(R.drawable.flag_green).into(holder);

                    else
                        Glide.with(context).load(R.drawable.flag_red).into(holder);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println("**** fave api ****fail***** " + post_id);
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();

                Toast.makeText(context, "Internal Server Error , Try again ! ", Toast.LENGTH_SHORT).show();
            }

        });

    }


    public static void Send_fav(final String user_id, final String post_id,

                                final ImageView holder, final Activity context) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        params.put("post_id", post_id);

        System.out.println(params);

        if (user_id.length() <= 0)
            OpenWarning(context);


        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "adding to favorites", true);
        ringProgressDialog.setCancelable(false);

        client.post(BASE_URL_NEW + "add_favorites", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println("**** fave api ********* " + post_id);
                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1"))
                        Glide.with(context).load(R.drawable.fav).into(holder);
                    else if (response_fav.equals("0"))
                        Glide.with(context).load(R.drawable.favv).into(holder);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println("**** fave api ****fail***** " + post_id);
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
                Toast.makeText(context, "Internal Server Error , Try again ! ", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public static void OpenWarning(final Activity context) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle1);
        //ab.setTitle("Are you shore you want to log out");
        ab.setMessage("Login To Continue");
        ab.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(context.getApplicationContext(), "loginData", "empty");
                context.startActivity(new Intent(context.getApplicationContext(), EnterLogin.class));
                context.finish();
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();

    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static void addpost(final Context context, final String user_id, final String contact_person, final String title, final String description
            , final String price, final String address, final String phone_no
            , final String latitude, final String longitude,
                               final ArrayList<String> category_array, final String parent_category,
                               final String lng, ArrayList<String> imageSet,final String size, final
                               String conditions) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "upload process", true);
        ringProgressDialog.setCancelable(false);

        String category = category_array.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");

        String contact_by = getData(context, "contact_by", "");
        String currency_code = getData(context, "currency_code", "");

        TimeZone timezone = TimeZone.getDefault();
        System.out.println("* timezone ****** " + timezone.getID());
        System.out.println("* currency code ****** " + currency_code);

        params.put("contact_by", contact_by);
        params.put("title", title);
        params.put("user_id", user_id);
        params.put("description", description);
        params.put("price", price);
        params.put("address", address);
        params.put("phone_no", phone_no);
        params.put("latitude", latitude);
        params.put("lng", lng);
        params.put("category", category);
        params.put("parent_category", parent_category);
        params.put("contact_person", contact_person);
        params.put("longitude", longitude);
        params.put("currency_code", currency_code);
        params.put("timezone", timezone.getID());
        params.put("size", size);
        params.put("conditions", conditions);

        System.out.println("*************** featured image data ***********");

        try {
            for (int i = 0; i < imageSet.size(); i++) {
                String result = getData(context, "item_img" + i, "");

                params.put("featured_img" + (i + 1), new File(result));
                NullData(context, "item_img" + i);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        System.out.println(params);

        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "addpost", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println("******** response  add post *******");
                System.out.println(response);

                try {
                    String data = response.getString("status");
                    if (data.equals("1")) {

                        Toast.makeText(context, "Post Added ", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("EXIT", true));

                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("******** response  add post *******");
                System.out.println(errorResponse);
                ringProgressDialog.dismiss();
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println("******** response  add post *******");
                System.out.println(responseString);

            }

        });
    }


    public static void addJobs(final Context context, final String user_id, final String contact_person, final String title, final String description
            , final String address, final String phone_no
            , final String latitude, final String longitude,
                               final ArrayList<String> category_array, final String parent_category,
                               final String lng, ArrayList<String> imageSet
            , String job_responsibilities, String experience_skills, final String job_type, final String job_salary) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "upload process", true);
        ringProgressDialog.setCancelable(false);

        String category = category_array.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");

        String contact_by = getData(context, "contact_by", "");
        TimeZone timezone = TimeZone.getDefault();

        params.put("contact_by", contact_by);
        params.put("title", title);
        params.put("user_id", user_id);
        params.put("description", description);
        params.put("salary_as_per", job_salary);
        params.put("address", address);
        params.put("phone_no", phone_no);
        params.put("latitude", latitude);
        params.put("lng", lng);
        params.put("category", category);
        params.put("parent_category", parent_category);
        params.put("contact_person", contact_person);
        params.put("longitude", longitude);
        params.put("job_responsibilities", job_responsibilities);
        params.put("experience_skills", experience_skills);
        params.put("job_type", job_type);
        params.put("timezone", timezone.getID());


        System.out.println("******* edtails for job post ***** ");
        System.out.println(experience_skills);
        System.out.println(job_responsibilities);
        System.out.println(title);
        System.out.println(job_salary);

        System.out.println("*************** featured image data in jobs***********");

        try {
            for (int i = 0; i < imageSet.size(); i++) {
                String result = getData(context, "item_img" + i, "");

                params.put("featured_img" + (i + 1), new File(result));

                NullData(context, "item_img" + i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        System.out.println(params);

        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "job_post", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println("******** response job post*******");
                System.out.println(response);

                try {
                    String data = response.getString("status");
                    if (data.equals("1")) {
                        Toast.makeText(context, "Post Added ", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("EXIT", true));
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("******** response  job_post *******");
                System.out.println(errorResponse);
                ringProgressDialog.dismiss();
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println("******** response  add post *******");
                System.out.println(responseString);

            }

        });


    }

    public static void addProperty(final Context context, final String user_id,
                                   final String contact_person, final String title,
                                   final String description
            , final String address, final String phone_no
            , final String latitude, final String longitude,
                                   final ArrayList<String> category_array, final String parent_category,
                                   final String lng, ArrayList<String> imageSet
            , String bathroomCount, String room_count, final String price, final String prop_size) {

        String dog_friendly = "", cat_friendly = "";
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "upload process", true);
        ringProgressDialog.setCancelable(false);

        String category = getData(context, "categories", "");


        String contact_by = getData(context, "contact_by", "");
        TimeZone timezone = TimeZone.getDefault();
        String currency_code = getData(context, "currency_code", "");


        if (isdogChecked)
            dog_friendly = "Yes";
        else
            dog_friendly = "No";

        if (iscatChecked)
            cat_friendly = "Yes";
        else
            cat_friendly = "No";

        params.put("title", title);
        params.put("user_id", user_id);
        params.put("price", price);
        params.put("contact_by", contact_by);
        params.put("description", description);
        params.put("cat_friendly", cat_friendly);
        params.put("dog_friendly", dog_friendly);
        params.put("address", address);
        params.put("phone_no", phone_no);
        params.put("latitude", latitude);
        params.put("lng", lng);
        params.put("category", category);
        params.put("parent_category", parent_category);
        params.put("contact_person", contact_person);
        params.put("longitude", longitude);
        params.put("bathrooms", bathroomCount);
        params.put("rooms_bedrooms", room_count);
        params.put("size", prop_size);
        params.put("timezone", timezone.getID());
        params.put("currency_code", currency_code);

        System.out.println("*************** featured image data in jobs***********");

        try {
            for (int i = 0; i < imageSet.size(); i++) {
                String result = getData(context, "item_img" + i, "");

                params.put("featured_img" + (i + 1), new File(result));

                NullData(context, "item_img" + i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        System.out.println(params);
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "add_property", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println("******** response property post*******");
                System.out.println(response);

                try {
                    String data = response.getString("status");
                    if (data.equals("1")) {
                        Toast.makeText(context, "Post Added ", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("EXIT", true));
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("******** response  property_post *******");
                System.out.println(errorResponse);
                ringProgressDialog.dismiss();
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println("******** response  add post *******");
                System.out.println(responseString);

            }

        });


    }



    public static void addBusiness(final Context context, final String user_id, final String bus_name,
                                   final String description, final String about_bus,
                                   final ArrayList<String> category_array, final String address, final String latitude, final String longitude, final String parent_category,
                                   final String lng, ArrayList<String> imageSet, final String social_link, final String web_link, final String hours_of_operation)
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "upload process", true);
        ringProgressDialog.setCancelable(false);


        String category = category_array.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");
        String contact_by = getData(context, "contact_by", "");
        TimeZone timezone = TimeZone.getDefault();
        String currency_code = getData(context, "currency_code", "");
        params.put("user_id", user_id);
        params.put("timezone", timezone.getID());
        params.put("currency_code", currency_code);
        params.put("category", category);
        params.put("address", address);
        params.put("parent_category", parent_category);
     params.put("business_name", bus_name);
        params.put("description", description);
        params.put("about_business", about_bus);
        params.put("social_media_link", social_link);
        params.put("website_link", web_link);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("hours_of_operation", hours_of_operation);


        System.out.println("*************** featured image data in jobs***********");

        try {
            for (int i = 0; i < imageSet.size(); i++) {
                String result = getData(context, "item_img" + i, "");

                params.put("featured_img" + (i + 1), new File(result));

                NullData(context, "item_img" + i);
            }
        } catch (FileNotFoundException e) {
            ringProgressDialog.dismiss();
            e.printStackTrace();

        }
//
//        try {
//            params.put("video_file", vdo);
//        } catch (FileNotFoundException e) {
//            System.out.println(vdo);
//            e.printStackTrace();
//        }
        System.out.println(params);
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        client.post(BASE_URL_NEW + "add_business_post", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println("******** response businesss post*******");
                System.out.println(response);

                try {
                    String data = response.getString("status");
                    if (data.equals("1")) {
                        Toast.makeText(context, "Post Added ", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("EXIT", true));
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("******** response  property_post *******");
                System.out.println(errorResponse);
                ringProgressDialog.dismiss();
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println("******** response  add post *******");
                System.out.println(responseString);

            }

        });


    }






    public static void getPostDetails(final Activity context, String user_id, final String post_id, final
    ArrayList<String> postarr, final Class toAct, final String fragment_name) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        responseDEtailsOBJ = new JSONObject();

        params.put("user_id", user_id);
        params.put("post_id", post_id);
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Loading Post", true);
        ringProgressDialog.setCancelable(false);

        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);

        System.out.println("------ > job detail params ---------- > " + params);
        client.post(BASE_URL_NEW + "get_post_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                responseDEtailsOBJ = response;

                ringProgressDialog.dismiss();

                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONObject obj = response.getJSONObject("result");
                        responseDEtailsOBJ = obj;
                        openNextAct(context, responseDEtailsOBJ, postarr, toAct, fragment_name);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println("**** fav api ****fail***** " + post_id);
                System.out.println(errorResponse);
                responseDEtailsOBJ = errorResponse;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                ringProgressDialog.dismiss();
                System.out.println("**** fave api ****fail***** " + post_id);
                System.out.println(responseString);

            }

        });

    }

    private static void openNextAct(Activity context, JSONObject responseDEtailsOBJ,
                                    ArrayList<String> postarr, Class toact, String fragment_name) {
        if (responseDEtailsOBJ != null) {
            Bundle bundle = new Bundle();
            bundle.putString("fragment_name", fragment_name);
            bundle.putString("productDetails", responseDEtailsOBJ.toString());
            bundle.putStringArrayList("images", postarr);
            context.startActivity(new Intent(context.getApplicationContext(), toact)
                    .putExtras(bundle));
        }


    }

    public static boolean isMatch(String s, String patt) {
        Pattern pat = Pattern.compile(patt);
        Matcher m = pat.matcher(s);
        return m.matches();
    }


    public static Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }


    public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());

        return outtoRight;
    }

    public static void OpenMainActivity(Activity act) {
        Intent i = new Intent(act, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
    }

    public static void hideKeyboard(Activity context) {
        try {
            if (context == null) return;
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(context.getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    public static void resetData(Context context) {
        saveData(context, "first_entry", "true");
        saveData(context, "first_entry_contact", "true");
        saveData(context, "first_entry_cat", "true");
        saveData(context, "phone_data", "");
        saveData(context, "extension_data", "");
        saveData(context, "contact_name_data", "");
        saveData(context, "contact_type_data", "");
        saveData(context, "language", "");
    }


    public static void checkData(String key, EditText editText, Context context) {

        if (getData(context, key, "") != null) {
            String result = getData(context, key, "");
            editText.setText(result);
        }
    }

    public static void checkDataByTV(String key, TextView editText, Context context) {

        if (getData(context, key, "") != null) {
            String result = getData(context, key, "");
            editText.setText(result);
        }
    }

    public static void saveDatatoLocal(String key, EditText editText, Context context) {

        String result = editText.getText().toString();

        MySharedPref.saveData(context, key, result);

    }


    public static void saveDatatoLocalByTV(String key, TextView editText, Context context) {

        String result = editText.getText().toString();

        MySharedPref.saveData(context, key, result);

    }

    /**
     * decodes image and scales it to reduce memory consumption
     **/
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    public static void openReportWarning(final String user_id, final String post_id,

                                         final ImageView holder, final Activity context)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder
                (context, R.style.MyAlertDialogStyle1);
        ab.setTitle("Spam / Report ").setIcon(R.drawable.spam);
        ab.setMessage("Are you sure ? ");
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                send_report(user_id, post_id, holder, context);
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();

    }

    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
