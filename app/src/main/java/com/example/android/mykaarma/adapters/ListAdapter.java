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
    private Listener listener;
    private Context mContext;

    public ListAdapter(ArrayList<Dealer> dealerArrayList, Context context, Listener listener) {
        super(context,0);
        this.dealerArrayList = dealerArrayList;
        this.mContext = context;
        this.listener = listener;
    }

//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.row_layout,parent,false);
//        return new DealersClass(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(holder.getAdapterPosition());
//            }
//        });
//        Dealer dealer = dealerArrayList.get(position);
//        String id = dealer.ID+"";
//    }

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
            holder.name = id;
            holder.address = add;
            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();
        Dealer dealer = dealerArrayList.get(position);
        holder.name.setText(dealer.ID);
        holder.address.setText(dealer.address);
        return convertView;
    }

    public static class ViewHolder {
        TextView name, address;
//        View itemView;
//        public ViewHolder() {
////            super(itemView);
//            name = itemView.findViewById(R.id.name);
//            address = itemView.findViewById(R.id.address);
//        }
    }

//    @Override
//    public int getItemCount() {
//        return dealerArrayList.size();
//    }

    public interface Listener{
        void onItemClick(int position);
    }
}
