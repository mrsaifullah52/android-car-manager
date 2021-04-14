package com.softtechglobal.androidcarmanager.capture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.Views.BaseAdapterForGallery;
import com.softtechglobal.androidcarmanager.Views.ModelForGallery;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    ListView listView;
    GridView gridview;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<String>();

    ModelForGallery modelForGallery;
    ArrayList<ModelForGallery> modelList=new ArrayList<ModelForGallery>();
    BaseAdapterForGallery adapterForGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().hide();


//        listView=(ListView) findViewById(R.id.gallerylistview);

        gridview=(GridView) findViewById(R.id.gridview);

//        titles.add("This is dummy title 1");
//        titles.add("This is dummy title 2");
//        titles.add("This is dummy title 1");
//        titles.add("This is dummy title 2");
//        titles.add("This is dummy title 1");
//        titles.add("This is dummy title 2");
        urls.add("https://picsum.photos/200/201");
        urls.add("https://picsum.photos/200/202");
        urls.add("https://picsum.photos/200/203");
        urls.add("https://picsum.photos/200/204");
        urls.add("https://picsum.photos/200/205");
        urls.add("https://picsum.photos/200/206");

        urls.add("https://picsum.photos/200/201");
        urls.add("https://picsum.photos/200/202");
        urls.add("https://picsum.photos/200/203");
        urls.add("https://picsum.photos/200/204");
        urls.add("https://picsum.photos/200/205");
        urls.add("https://picsum.photos/200/206");

        urls.add("https://picsum.photos/200/201");
        urls.add("https://picsum.photos/200/202");
        urls.add("https://picsum.photos/200/203");
        urls.add("https://picsum.photos/200/204");
        urls.add("https://picsum.photos/200/205");
        urls.add("https://picsum.photos/200/206");

        urls.add("https://picsum.photos/200/201");
        urls.add("https://picsum.photos/200/202");
        urls.add("https://picsum.photos/200/203");
        urls.add("https://picsum.photos/200/204");
        urls.add("https://picsum.photos/200/205");
        urls.add("https://picsum.photos/200/206");

        urls.add("https://picsum.photos/200/201");
        urls.add("https://picsum.photos/200/202");
        urls.add("https://picsum.photos/200/203");
        urls.add("https://picsum.photos/200/204");
        urls.add("https://picsum.photos/200/205");
        urls.add("https://picsum.photos/200/206");

        for (int i=0; i<urls.size();i++){
            modelForGallery=new ModelForGallery (urls.get(i));
            modelList.add(modelForGallery);
        }

        adapterForGallery=new BaseAdapterForGallery(Gallery.this, modelList);
//        listView.setAdapter(adapterForGallery);

        gridview.setAdapter(adapterForGallery);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(Gallery.this, ViewImage.class);
                i.putExtra ( "imageurl", urls.get(position) );
                startActivity(i);
            }
        });
    }
}