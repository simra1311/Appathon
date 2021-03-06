package com.example.android.mykaarma.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mykaarma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This is to create mail adapter of MyMails activity Recycler View
 *
 * @author Vinod Kumar
 * @version 1.0
 */
public class mailAdapter extends RecyclerView.Adapter<mailAdapter.ViewHolder> {

    JSONArray mails;
    String user_id;

    /**
     * Paramaters required to create mail Adapter View
     *
     * @param mails JSONArray containing mails and its details as JSONObject
     * @param user_id current user id used for identinfying if mail is sended or received
     */
    public mailAdapter(JSONArray mails, String user_id) {
        this.mails = mails;
        this.user_id = user_id;
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

            if(data.getString("sender_id").contentEquals(user_id)){

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
