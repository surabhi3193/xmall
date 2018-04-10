package com.mindinfo.xchangemall.xchangemall.ImageClass;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchItemsTask extends AsyncTask<Void, Void, JSONArray> {

    private int startIndex;
    private ResponseListener responseListener;
    private JSONArray jsonArray;

    public FetchItemsTask(int startIndex, ResponseListener listener,JSONArray jsonArray) {
        this.startIndex = startIndex;
        this.responseListener = listener;
        this.jsonArray = jsonArray;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        JSONArray dummyItems = new JSONArray();

        try {
            //In this example, we are fetching dummy data locally;
            // but in a real world app its usually a network or database request which takes time.
            // So FAKE that time delay here.
            Thread.sleep(3000);



        // Fetch a maximum of 50 new items.
        int end = startIndex + 5 ;

        // Get the data.
        for(int i = startIndex ; i < end ; i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            dummyItems.put(item);
        }
        } catch (Exception e) {
e.printStackTrace();
            Thread.dumpStack();
return  null;

        }
        return dummyItems;
    }

    @Override
    protected void onPostExecute(JSONArray newItems) {
        // give the new items back to the activity
        responseListener.onResponse(newItems);
    }
}
