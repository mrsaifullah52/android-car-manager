package com.softtechglobal.androidcarmanager.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.softtechglobal.androidcarmanager.R;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapterForGallery extends BaseAdapter {
    Context context;
    List<ModelForGallery> list;
    ArrayList<ModelForGallery> arrayList;

    public BaseAdapterForGallery(Context context, List<ModelForGallery> list) {
        this.context = context;
        this.list = list;
//        this.arrayList = arrayList;
        this.arrayList = new ArrayList<ModelForGallery>();
        this.arrayList.addAll(list);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyHolder {
//        TextView titleTv;
        ImageView imageView;
        MyHolder(View v) {
//            titleTv = (TextView) v.findViewById(R.id.gallery_list_title);
            imageView = (ImageView) v.findViewById(R.id.gallery_list_thumbnail);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyHolder holder=null;
        if(row==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.gallery_list,null);
            holder = new MyHolder(row);
            row.setTag(holder);
        }else{
            holder = (MyHolder) row.getTag();
        }

//        holder.titleTv.setText(list.get(position).getTitle());
        Glide.with(context)
                .load(list.get(position).getUrl())
                .into(holder.imageView);


        return row;
    }

}
