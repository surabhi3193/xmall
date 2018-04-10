package com.mindinfo.xchangemall.xchangemall.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.MyFavAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.MyfavModel;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;


public class SaveSearchFragment extends Fragment {
    ListView rvMyFav;
    ProgressDialog ringProgressDialog;
    MyFavAdapter adapter;
    ArrayList<MyfavModel> itemList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.savesearchfragment,null);


        String user_id = getData(getActivity().getApplicationContext(),"user_id","");
//        getUserSav(user_id);
        adapter = new MyFavAdapter(getActivity(),itemList);
        rvMyFav = (ListView) v.findViewById(R.id.rvMyFav);

        rvMyFav.setAdapter(adapter);
        return v;
    }
//    @SuppressLint("StaticFieldLeak")
//    private void getUserSav(final String user_id)
//    {
//        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
//        ringProgressDialog.setCancelable(false);
//        new AsyncTask<String, Void, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//                OkHttpClient client = new OkHttpClient();
//                String response = "";
//                try {
//                    response =  NetworkClass.POST_New(client,"show_save_search_by_user",NetworkClass.getMySav(user_id));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return response;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                ringProgressDialog.dismiss();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONArray jsonArray = jsonObject.getJSONArray("0");
//                    for(int i = 0; i<jsonArray.length(); i++)
//                    {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        String post_id = jsonObject1.getString("post_id");
//                        //   Toast.makeText(getActivity(), post_id, Toast.LENGTH_SHORT).show();
//                        post_by_id(post_id);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.execute();
//    }

}
