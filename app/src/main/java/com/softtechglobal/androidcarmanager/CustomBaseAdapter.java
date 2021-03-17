package com.softtechglobal.androidcarmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    List<ModelForAdapter> modellist;
    ArrayList<ModelForAdapter> arrayList;

    public CustomBaseAdapter(Context context, List<ModelForAdapter> modellist ) {
        this.context = context;
        this.modellist = modellist;
        this.arrayList = arrayList;
        this.arrayList = new ArrayList<ModelForAdapter>();
        this.arrayList.addAll(modellist);
    }


    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int position) {
        return modellist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class MyHolder {
        TextView titleTv, dateTv;
        MyHolder(View v) {
            titleTv = (TextView) v.findViewById(R.id.listTitleTv);
            dateTv = (TextView) v.findViewById(R.id.listDateTv);
        }
    }

        @Override
        public View getView(final int position, View converView, ViewGroup parent){
            View row = converView;
            MyHolder holder=null;
            if(row==null){
                LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.listview,null);
                holder = new MyHolder(row);
                row.setTag(holder);
            }else{
                holder = (MyHolder) row.getTag();
            }
            holder.titleTv.setText(modellist.get(position).getTitle());
            holder.dateTv.setText(modellist.get(position).getDate());
            return row;
        }

}


