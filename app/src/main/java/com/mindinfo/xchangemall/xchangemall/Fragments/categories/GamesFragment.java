package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class GamesFragment extends Fragment implements OnBackPressed, View.OnClickListener {
    final ArrayList<categories> arrayList = new ArrayList<>();
    final ArrayList<NewsLocation> newsLocList = new ArrayList<>();
    RecyclerView recyclerViewItem;
    private FragmentManager fm;
    private String csv = "";

    private TextView noPostTv, title_cat, cat_TextView;
    private Button cancel_button;
    private Button confirm_btn;
    private ListView cat_sub_list_view;
    private PullRefreshLayout refreshLayout;
    private RelativeLayout catlog;
    private LinearLayout for_sale;
    private LinearLayout property_rental;
    private LinearLayout property_rentalsale;
    private LinearLayout jobs;
    private LinearLayout buisness;
    private LinearLayout personel;
    private LinearLayout community;
    private LinearLayout showcase;
    private LinearLayout news;
    private Bundle bundle;
    private String search_key ="";


    private WebView webView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_games, null);

        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        bundle = new Bundle();
        recyclerViewItem = v.findViewById(R.id.recyclerViewItem);
        webView = v.findViewById(R.id.webView);
        LinearLayout games_top = v.findViewById(R.id.games_top);
        EditText searchbox = v.findViewById(R.id.msearch);


        games_top.getParent().requestChildFocus(games_top, games_top);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItem.setLayoutManager(llm);


        findbyview(v);
        addClickListner(v);

        getGamesList(csv, search_key);
        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGamesList(csv, search_key);

                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        searchbox.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                 search_key = searchbox.getText().toString();
                getGamesList(csv,search_key);
                hideKeyboard(getActivity());
                return true;
            }
            return false;
        });


        return v;
    }

    private void getGamesList(String csv, String search_key) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(getActivity(), "Loading...", "", true);
        ringProgressDialog.setCancelable(false);
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        double currentLatitude = gpsTracker.getLatitude();
        double currentLongitude = gpsTracker.getLongitude();
        String address = getAddressFromLatlng(new LatLng(currentLatitude, currentLongitude), getActivity(), 2);
        params.put("cat_id",csv);
        params.put("val",search_key);
        params.put("pcat_id","373");

        System.out.println("**** games_list api ********* ");
        System.out.println(params);



        client.post(BASE_URL_NEW + "games_list",params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    int response_fav = response.getInt("status");

                    if (response_fav == 1) {
                        JSONArray responseArray = response.getJSONArray("result");
                        if (responseArray.length() > 0) {
                            recyclerViewItem.setVisibility(View.VISIBLE);
                            noPostTv.setVisibility(View.GONE);
                            webView.setVisibility(View.GONE);
                            NewsAdapter newsAdapter = new NewsAdapter(getActivity(), responseArray, GamesFragment.this);
                            recyclerViewItem.setAdapter(newsAdapter);
                            newsAdapter.notifyDataSetChanged();
                        }
                    } else {
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
                System.out.println("**** games api ****fail***** ");
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println("**** games api ****fail***** ");
                System.out.println(responseString);
            }

        });

    }


    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE) {
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
        refreshLayout = v.findViewById(R.id.refreshLay);
        noPostTv = v.findViewById(R.id.noPostTv);

        catlog = v.findViewById(R.id.catlog);
        title_cat = v.findViewById(R.id.title_cat);
        cat_TextView = v.findViewById(R.id.cat_TextView);
        cat_sub_list_view = v.findViewById(R.id.cat_sub_list_view);
        cancel_button = v.findViewById(R.id.cancel_button);
        confirm_btn = v.findViewById(R.id.confirm_btn);

        for_sale = v.findViewById(R.id.forsale_top);
        news = v.findViewById(R.id.news_top);
        property_rental = v.findViewById(R.id.property_rental_top);
        property_rentalsale = v.findViewById(R.id.property_rentalsale_top);
        jobs = v.findViewById(R.id.jobs_top);

        buisness = v.findViewById(R.id.buisness_top);
        personel = v.findViewById(R.id.personal_top);
        community = v.findViewById(R.id.community_top);
        showcase = v.findViewById(R.id.showcase_top);


        saveData(getActivity(), "MainCatType", "373");

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void addClickListner(View view) {
        jobs.setOnClickListener(this);
        buisness.setOnClickListener(this);
        property_rental.setOnClickListener(this);
        property_rentalsale.setOnClickListener(this);
        showcase.setOnClickListener(this);
        personel.setOnClickListener(this);
        community.setOnClickListener(this);
        for_sale.setOnClickListener(this);
        news.setOnClickListener(this);
        cat_TextView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forsale_top:


                bundle.putString("MainCatType", "104");
                ItemMainFragment forsale_main = new ItemMainFragment();
                forsale_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, forsale_main).addToBackStack(null).commit();

                break;


            case R.id.personal_top:

                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "100");
//                SocialFragment personal_main = new SocialFragment();
//                personal_main.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, personal_main).addToBackStack(null).commit();

                break;

            case R.id.buisness_top:
                bundle.putString("MainCatType", "101");
                Bussiness_Service_Main business_main = new Bussiness_Service_Main();
                business_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, business_main).addToBackStack(null).commit();

                break;

            case R.id.property_rental_top:
                property_rental.requestFocus();


                bundle.putString("MainCatType", "102");
                Property_Rental_Fragment jobsMAin = new Property_Rental_Fragment();
                jobsMAin.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobsMAin).addToBackStack(null).commit();
                break;


            case R.id.jobs_top:

                bundle.putString("MainCatType", "103");
                JobsMainFragment jobs_main = new JobsMainFragment();
                jobs_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobs_main).addToBackStack(null).commit();

                break;


            case R.id.showcase_top:

                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "105");
//                ShowCaseFragment showcase_top = new ShowCaseFragment();
//                showcase_top.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, showcase_top).addToBackStack(null).commit();

                break;

            case R.id.community_top:

                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "106");
//                CommunityFragment communityFragment = new CommunityFragment();
//                communityFragment.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, communityFragment).addToBackStack(null).commit();

                break;

            case R.id.property_rentalsale_top:


                bundle.putString("MainCatType", "272");
                Property_Sale_Fragment property_sale = new Property_Sale_Fragment();
                property_sale.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, property_sale).addToBackStack(null).commit();

                break;
            case R.id.news_top:
                bundle.putString("MainCatType", "309");
                NewsFragment news_top = new NewsFragment();
                news_top.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, news_top).addToBackStack(null).commit();
                break;

            case R.id.cat_TextView:
                ShowCategeriesSnak();
                break;

        }
    }


    public void openWebView(String url) {
        recyclerViewItem.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);

        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        System.out.println("-------- url to load ----------- ");
        System.out.println(url);
        webView.loadUrl(url);
    }


    public void ShowCategeriesSnak() {
        catlog.setVisibility(View.VISIBLE);
        title_cat.setText(getResources().getString(R.string.games));

        MULTIPLEsELECTIONcATEGORY postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cat_sub_list_view.setAdapter(postadapter);

        NetworkClass.getListData("373", arrayList, getActivity());
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
                getGamesList(csv,search_key);


            }
        });

    }

}