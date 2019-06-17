package com.example.android.mydrugjournal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.EmergencyContact;
import com.example.android.mydrugjournal.data.Medication;

import java.util.ArrayList;

public class EmergencyContactsRecyclerAdapter extends RecyclerView.Adapter<EmergencyContactsRecyclerAdapter.MyViewHolder> {
    private ArrayList<EmergencyContact> mContacts;

    // Provides a suitable constructor (depends on the kind of dataset)
    public EmergencyContactsRecyclerAdapter(ArrayList<EmergencyContact> myDataset) {
        mContacts = myDataset;
    }

    public void updateList(ArrayList<EmergencyContact> newList) {
        mContacts.clear();
        mContacts.addAll(newList);
        notifyDataSetChanged();
    }

    // Creates new views (invoked by the layout manager)
    @Override
    public EmergencyContactsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_emergency_numbers_template, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.contactName.setText(mContacts.get(position).getName());
        holder.contactAddress.setText(mContacts.get(position).getAddress());
        holder.contactPhone.setText(mContacts.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView contactName;
        public TextView contactAddress;
        public TextView contactPhone;

        public MyViewHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.contactName);
            contactAddress = view.findViewById(R.id.contactAddress);
            contactPhone= view.findViewById(R.id.contactNumber);
        }
    }
}