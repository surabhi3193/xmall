package com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.EventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventByCat extends AppCompatActivity {

    String title;
ImageView back_btn;
    private List<EventItem> itemList;
private EventAdapter itemlistAdapter;
private ListView recyclerViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_by_cat);

        title = getIntent().getExtras().getString("title_name","");

        TextView title_TV= (TextView)findViewById(R.id.cat_titleTV);
        recyclerViewItem = (ListView)findViewById(R.id.event_list);
        back_btn = (ImageView) findViewById(R.id.back_btn);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");
        title_TV.setTypeface(face);
        title_TV.setText(title);
        itemList = new ArrayList<>();

            itemList.add(0, new EventItem(R.drawable.live_s,"","","","","",""));
            itemList.add(1, new EventItem(R.drawable.live5,"","","","","",""));
            itemList.add(2, new EventItem(R.drawable.live2,"","","","","",""));
            itemList.add(3, new EventItem(R.drawable.live4,"","","","","",""));
            itemList.add(4, new EventItem(R.drawable.live_sal1,"","","","","",""));
            //itemList.add(i,new ItemsMain("https://graph.facebook.com/1441489612612648/picture?height=100&width=100&migration_overrides=%7Boctober_2012%3Atrue%7D","1","2","subtitle","item review"));
            itemlistAdapter = new EventAdapter(EventByCat.this, itemList);
            recyclerViewItem.setAdapter(itemlistAdapter);


            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
    }

    @Override
    public void onBackPressed() {
finish();
    }
}
