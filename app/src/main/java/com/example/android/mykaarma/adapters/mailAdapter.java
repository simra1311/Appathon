package com.example.android.mykaarma.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.mykaarma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class mailAdapter extends RecyclerView.Adapter<mailAdapter.ViewHolder> {

    JSONArray mails;
    String sender_id;

    public mailAdapter(JSONArray mails, String sender_id) {
        this.mails = mails;
        this.sender_id = sender_id;
    }

    @NonNull
    @Override
    public mailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_items,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull mailAdapter.ViewHolder holder, int position) {

        try {

            JSONObject data = mails.getJSONObject(position);

            if(data.getString("sender_id").contentEquals(sender_id)){

                holder.mailType.setText("Sent");
                holder.email.setText(data.getString("sender_email"));
            }else {
                holder.mailType.setText("Received");
                holder.email.setText(data.getString("receiver_email"));
            }

            holder.subject.setText(data.getString("subject"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mails.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mailType, email, subject;

        public ViewHolder(View itemView) {
            super(itemView);

            mailType = itemView.findViewById(R.id.mailType);
            email    = itemView.findViewById(R.id.email);
            subject  = itemView.findViewById(R.id.subject);
//
        }
    }
}
