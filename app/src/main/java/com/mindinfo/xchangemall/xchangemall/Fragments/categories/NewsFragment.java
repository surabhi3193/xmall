package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY;
import com.mindinfo.xchangemall.xchangemall.adapter.NewsAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.NewsLocation;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mindinfo.xchangemall.xchangemall.intefaces.OnBackPressed;
import com.mindinfo.xchangemall.xchangemall.other.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.hideKeyboard;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class NewsFragment extends Fragment implements OnBackPressed, View.OnClickListener {
    final ArrayList<categories> arrayList = new ArrayList<>();
    final ArrayList<NewsLocation> newsLocList = new ArrayList<>();
    final ArrayList<NewsLocation> newssubLocList = new ArrayList<>();
    RecyclerView recyclerViewItem;
    TextView noPostTv;
    Spinner currentLocation;
    Spinner countryTV;
    private FragmentManager fm;
    private String csv = "";
    private PullRefreshLayout refreshLayout;
    private RelativeLayout catlog;
    private Button cancel_button;
    private Button confirm_btn;
    private ListView cat_sub_list_view;
    private TextView title_cat;
    private LinearLayout Post_camera_icon;
    private LinearLayout for_sale;
    private LinearLayout property_rental;
    private LinearLayout property_rentalsale;
    private LinearLayout jobs;
    private LinearLayout games_top;
    private LinearLayout buisness;
    private LinearLayout personel;
    private LinearLayout community;
    private LinearLayout showcase;
    private  EditText searchbox;
    private Bundle bundle;
    private TextView cat_TextView ;
    private String loc ="";
    private String sub_loc_id ="";
    private String search_key ="";
    private WebView webView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, null);

        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        bundle = new Bundle();
        recyclerViewItem = v.findViewById(R.id.recyclerViewItem);
        webView = v.findViewById(R.id.webView);
        LinearLayout news_top = v.findViewById(R.id.news_top);
        TextView addFavTv = v.findViewById(R.id.addFavTv);
        currentLocation = v.findViewById(R.id.currentLocation);
        countryTV = v.findViewById(R.id.countryTV);
        news_top.getParent().requestChildFocus(news_top, news_top);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItem.setLayoutManager(llm);

        addFavTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAddFavDialog();
            }
        });




        if (idarray!=null && idarray.size()>0)
            csv = idarray.toString().replace("[", "").replace("]", "")
                    .replace(", ", ",");


        findbyview(v);
        addClickListner(v);

        getNewsLocation(newsLocList);
        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
           getNewsList(csv, loc,sub_loc_id,search_key);

                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        currentLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NewsLocation cat = (NewsLocation) currentLocation.getItemAtPosition(position);
                System.out.println("------------ selected location---");
                System.out.println(cat.getId());
                System.out.println(cat.getname());
                loc=cat.getId();
                get_sublocation(newssubLocList,loc);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        countryTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NewsLocation cat = (NewsLocation) countryTV.getItemAtPosition(position);
                System.out.println("------------ selected country --");
                System.out.println(cat.getId());
                System.out.println(cat.getname());
                sub_loc_id=cat.getId();
                getNewsList(csv,cat.getId(),sub_loc_id,search_key);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }
    private void getNewsList(String cat, String loc_id,String sub_loc_id,String search_key) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(getActivity(), "Loading...", "", true);
        ringProgressDialog.setCancelable(false);

        String user = getData(getActivity(),"user_id","");


        params.put("cat_id",cat);
        params.put("location_id",loc);
        params.put("sub_loc_id",sub_loc_id);
        params.put("news_link_id","");
        params.put("user_id","");
        params.put("check_status","2");
        params.put("val",search_key);
        params.put("pcat_id","309");

        System.out.println("**** news_list api ********* " );
        System.out.println(params);

        client.post(BASE_URL_NEW + "news",params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();

                System.out.println(response);
                try {
                    int response_fav = response.getInt("status");

                    if (response_fav==1)
                    {
                        JSONArray responseArray = response.getJSONArray("result");
                        if (responseArray.length()>0)
                        {
                            recyclerViewItem.setVisibility(View.VISIBLE);
                            noPostTv.setVisibility(View.GONE);
                            webView.setVisibility(View.GONE);
                            NewsAdapter newsAdapter = new NewsAdapter(getActivity(), responseArray,NewsFragment.this);
                            recyclerViewItem.setAdapter(newsAdapter);
                            newsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            recyclerViewItem.setVisibility(View.GONE);
                            noPostTv.setVisibility(View.VISIBLE);
                            webView.setVisibility(View.GONE);
                        }
                    }

                    else {
                        recyclerViewItem.setVisibility(View.GONE);
                        noPostTv.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println("**** news_list api ****fail***** " );
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                }

        });

    }

    private void openAddFavDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setContentView(R.layout.addfav_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        TextView save_btn = dialog.findViewById(R.id.save_btn);
        EditText userCatEt = dialog.findViewById(R.id.userCatEt);
        EditText urlEt = dialog.findViewById(R.id.urlEt);
        Button add_btn = dialog.findViewById(R.id.add_btn);
        CheckBox cbCat = dialog.findViewById(R.id.CbCat);
        Spinner newsCatSpinner = dialog.findViewById(R.id.newsCatSpinner);

        cancel_btn.setOnClickListener(v -> dialog.dismiss());

        save_btn.setOnClickListener(v -> dialog.dismiss());

        add_btn.setOnClickListener(v -> dialog.dismiss());


        NetworkClass.getListData("309", arrayList, getActivity());

        ArrayAdapter<categories> dataAdapter = new ArrayAdapter<categories>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newsCatSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        cbCat.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                newsCatSpinner.setVisibility(View.GONE);
                userCatEt.setVisibility(View.VISIBLE);
            }
            else
            {
                newsCatSpinner.setVisibility(View.VISIBLE);
                userCatEt.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility()== View.VISIBLE)
        {
            if (Build.VERSION.SDK_INT < 18) {
                webView.clearView();
            } else {
                webView.loadUrl("about:blank");
            }
            webView.setVisibility(View.GONE);
            recyclerViewItem.setVisibility(View.VISIBLE);
            return;
        }
        System.out.println("===============else  on bk pr======");
        startActivity(new Intent(getActivity(),
                MainActivity.class).putExtra("EXIT", true));
    }

    private void findbyview(View v) {
        refreshLayout =v.findViewById(R.id.refreshLay);
        noPostTv = v.findViewById(R.id.noPostTv);

        catlog =v.findViewById(R.id.catlog);
        cancel_button =v.findViewById(R.id.cancel_button);
        confirm_btn =v.findViewById(R.id.confirm_btn);
        title_cat = v.findViewById(R.id.title_cat);
        cat_TextView = v.findViewById(R.id.cat_TextView);

        for_sale =v.findViewById(R.id.forsale_top);
        property_rental =v.findViewById(R.id.property_rental_top);
        property_rentalsale =v.findViewById(R.id.property_rentalsale_top);
        jobs =v.findViewById(R.id.jobs_top);
        games_top =v.findViewById(R.id.games_top);

        buisness =v.findViewById(R.id.buisness_top);
        personel =v.findViewById(R.id.personal_top);
        community =v.findViewById(R.id.community_top);
        showcase =v.findViewById(R.id.showcase_top);

        cat_sub_list_view =v.findViewById(R.id.cat_sub_list_view);
        searchbox =v.findViewById(R.id.msearch);
        catlog.setVisibility(View.GONE);

       saveData(getActivity(), "MainCatType", "309");

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void addClickListner(View view) {
        jobs.setOnClickListener(this);
        games_top.setOnClickListener(this);
        buisness.setOnClickListener(this);
        property_rental.setOnClickListener(this);
        property_rentalsale.setOnClickListener(this);
        showcase.setOnClickListener(this);
        personel.setOnClickListener(this);
        community.setOnClickListener(this);
        for_sale.setOnClickListener(this);

        cat_TextView.setOnClickListener(this);

        searchbox.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

              search_key = searchbox.getText().toString();
              getNewsList(csv,loc,sub_loc_id,search_key);
                hideKeyboard(getActivity());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cat_TextView:
                ShowCategeriesSnak();
                break;
            case R.id.forsale_top:


                bundle.putString("MainCatType", "104");
                ItemMainFragment forsale_main = new ItemMainFragment();
                forsale_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, forsale_main).addToBackStack(null).commit();

                break;



            case R.id.personal_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();

//                bundle.putString("MainCatType", "100");
//                SocialFragment personal_main = new SocialFragment();
//                personal_main.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, personal_main).addToBackStack(null).commit();

                break;

            case R.id.buisness_top:
                catlog.setVisibility(View.GONE);
                bundle.putString("MainCatType", "101");
                Bussiness_Service_Main business_main = new Bussiness_Service_Main();
                business_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, business_main).addToBackStack(null).commit();

                break;

            case R.id.property_rental_top:
                property_rental.requestFocus();
                catlog.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


                bundle.putString("MainCatType", "102");
                Property_Rental_Fragment jobsMAin = new Property_Rental_Fragment();
                jobsMAin.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobsMAin).addToBackStack(null).commit();
                break;


            case R.id.jobs_top:
                catlog.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();

                bundle.putString("MainCatType", "103");
                JobsMainFragment jobs_main = new JobsMainFragment();
                jobs_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobs_main).addToBackStack(null).commit();

                break;


            case R.id.showcase_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "105");
//                ShowCaseFragment showcase_top = new ShowCaseFragment();
//                showcase_top.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, showcase_top).addToBackStack(null).commit();

                break;

            case R.id.community_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "106");
//                CommunityFragment communityFragment = new CommunityFragment();
//                communityFragment.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, communityFragment).addToBackStack(null).commit();

                break;

            case R.id.property_rentalsale_top:
                catlog.setVisibility(View.GONE);

                bundle.putString("MainCatType", "272");
                Property_Sale_Fragment property_sale = new Property_Sale_Fragment();
                property_sale.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, property_sale).addToBackStack(null).commit();

                break;

            case R.id.games_top:
                bundle.putString("MainCatType", "373");
                GamesFragment games = new GamesFragment();
                games.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, games).addToBackStack(null).commit();
                break;
        }
    }

    public void ShowCategeriesSnak() {
        catlog.setVisibility(View.VISIBLE);
        title_cat.setText(getResources().getString(R.string.news));


        MULTIPLEsELECTIONcATEGORY postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cat_sub_list_view.setAdapter(postadapter);

        NetworkClass.getListData("309", arrayList, getActivity());
        postadapter.notifyDataSetChanged();


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catlog.setVisibility(View.GONE);
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catlog.setVisibility(View.GONE);

                csv = idarray.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
              getNewsList(csv, loc,sub_loc_id,search_key);
               String cat_id = "";

            }
        });
    }

    private   void getNewsLocation(ArrayList<NewsLocation> list)  {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        System.out.println(" News Location ------------ > ");

        client.post(BASE_URL_NEW + "news_location", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {


                        JSONArray jsonArray = response.getJSONArray("location");
                        if (jsonArray.length()>0)
                        {
                            if (newsLocList.size()>0)
                                newsLocList.clear();

                            newsLocList.add(new NewsLocation("0","Select Location"));

                            for (int i = 0;i<jsonArray.length();i++)
                            {
                                final JSONObject responseOBJ = jsonArray.getJSONObject(i);
                                String name = responseOBJ.getString("location_name");
                                String location_id = responseOBJ.getString("location_id");
                                NewsLocation news = new NewsLocation(location_id,name);
                                list.add(news);
                            }
                        }

                        ArrayAdapter<NewsLocation> dataAdapter = new ArrayAdapter<NewsLocation>(getActivity(),
                                android.R.layout.simple_spinner_item, newsLocList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        currentLocation.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("****news_location  api ****fail***** " );
                System.out.println(errorResponse);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println("**** news_location api ****fail***** " );
                System.out.println(responseString);

            }

        });

    }

    private   void get_sublocation(ArrayList<NewsLocation> list,String location_id)  {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("location_id",location_id);
        System.out.println(" News get_sublocation Location ------------ > ");

        client.post(BASE_URL_NEW + "get_sublocation", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                System.out.println(response);
                try {
                    String response_fav = response.getString("status");

                    if (list.size()>0)
                        list.clear();

                    list.add(new NewsLocation("0","Select Country"));


                    if (response_fav.equals("1")) {


                        JSONArray jsonArray = response.getJSONArray("sublocation");
                        if (jsonArray.length()>0)
                        {

                            for (int i = 0;i<jsonArray.length();i++)
                            {
                                final JSONObject responseOBJ = jsonArray.getJSONObject(i);
                                String name = responseOBJ.getString("country_name");
                                String location_id = responseOBJ.getString("sub_loc_id");
                                NewsLocation news = new NewsLocation(location_id,name);
                                list.add(news);
                            }
                        }



                    }

                    ArrayAdapter<NewsLocation> dataAdapter = new ArrayAdapter<NewsLocation>(getActivity(),
                            android.R.layout.simple_spinner_item, newssubLocList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    countryTV.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("****news_location  api ****fail***** " );
                System.out.println(errorResponse);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println("**** news_location api ****fail***** " );
                System.out.println(responseString);

            }

        });

    }

    public void openWebView(String url)
    {
        recyclerViewItem.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);

        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        System.out.println("-------- url to load ----------- " );
        System.out.println(url);
        webView.loadUrl(url);
    }

}