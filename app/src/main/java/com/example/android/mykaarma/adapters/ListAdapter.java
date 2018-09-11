package com.example.android.mykaarma.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.mykaarma.ClassObjects.Dealer;
import com.example.android.mykaarma.R;

import java.util.ArrayList;

/**
 * This is an adapter for the listView
 */
public class ListAdapter extends ArrayAdapter<Dealer> implements android.widget.ListAdapter {

    private ArrayList<Dealer> dealerArrayList;
    private Context mContext;

    public ListAdapter(ArrayList<Dealer> dealerArrayList, Context context) {
        super(context,0);
        this.dealerArrayList = dealerArrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dealerArrayList.size();
    }

    @Nullable
    @Override
    public Dealer getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_layout, parent,false);
            holder = new ViewHolder();
            TextView id = convertView.findViewById(R.id.name);
            TextView add = convertView.findViewById(R.id.address);
            TextView rating = convertView.findViewById(R.id.rating);
            TextView price = convertView.findViewById(R.id.price);
            holder.name = id;
            holder.address = add;
            holder.rating = rating;
            holder.price = price;
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
        Dealer dealer = dealerArrayList.get(position);
        holder.name.setText(dealer.ID);
        holder.address.setText(dealer.address);
        holder.price.setText(dealer.priceInINR);
        holder.rating.setText(dealer.rating);
        return convertView;
    }

    public static class ViewHolder {
        TextView name, address,price,rating;
    }

}
