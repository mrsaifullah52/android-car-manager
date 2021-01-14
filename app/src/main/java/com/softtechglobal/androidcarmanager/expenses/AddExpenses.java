package com.softtechglobal.androidcarmanager.expenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softtechglobal.androidcarmanager.R;

public class AddExpenses extends RecyclerView.ViewHolder {
    public final ImageView icon;
    public final TextView text;

    public AddExpenses(@NonNull View itemView) {
        super(itemView);

        this.icon = itemView.findViewById(R.id.mtrl_list_item_icon);
        this.text = itemView.findViewById(R.id.mtrl_list_item_text);
    }

    @NonNull
    public static AddExpenses create(@NonNull ViewGroup parent) {
        return new AddExpenses(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_addexpenses, parent, false));
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_addexpenses);
//        this.icon = itemView.findViewById(R.id.mtrl_list_item_icon);
//        this.text = itemView.findViewById(R.id.mtrl_list_item_text);
//
//    }
}
