package com.softtechglobal.androidcarmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String>  date=new ArrayList<String>();

    public CustomBaseAdapter(Context c, ArrayList<String> titles, ArrayList<String> date) {
        super(c, R.layout.listview, R.id.titleTv, titles);

        this.context = c;
        this.title = titles;
        this.date = date;
    }

    public class MyHolder {
        TextView titleTv, dateTv;
        MyHolder(View v) {
            titleTv = (TextView) v.findViewById(R.id.titleTv);
            dateTv = (TextView) v.findViewById(R.id.dateTv);
        }

        public View getView(final int position, View converView, ViewGroup parent){
            View row = converView;
            MyHolder holder=null;

            if(row==null){
                LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.listview,parent,false);
                holder = new MyHolder(row);
                row.setTag(holder);
            }else{
                holder = (MyHolder) row.getTag();
            }
            holder.titleTv.setText(title.get(position));
            holder.dateTv.setText(date.get(position));

            return row;
        }

    }

}
