package com.learnera.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.learnera.app.R;
import com.learnera.app.data.Contacts;

import java.util.List;

/**
 * Created by praji on 1/27/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<Contacts> contactsList;
    private Context mContext;

    public ContactsAdapter(Context mContext, List<Contacts> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.card_contact_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contacts contact = contactsList.get(position);
        holder.mName.setText(contact.getName());
        holder.mPhone.setText(contact.getPhoneNo());
        holder.mMail.setText(contact.getMailId());

        Glide.with(mContext).load(contact.getPhoto()).into(holder.mImg);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName, mMail, mPhone;
        public ImageView mImg;

        public ViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.card_contact_name);
            mMail = v.findViewById(R.id.card_contact_mail);
            mPhone = v.findViewById(R.id.card_contact_phone);
            mImg = v.findViewById(R.id.card_contact_image);
        }
    }
}