package com.example.android.mydrugjournal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.Medication;

import java.util.ArrayList;

public class MedicationsRecyclerAdapter extends RecyclerView.Adapter<MedicationsRecyclerAdapter.MyViewHolder> {
    private ArrayList<Medication> mMedications;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MedicationsRecyclerAdapter(ArrayList<Medication> myDataset) {
        mMedications = myDataset;
    }

    public void updateList(ArrayList<Medication> newList) {
        mMedications.clear();
        mMedications.addAll(newList);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MedicationsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_medication_template, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textName.setText(mMedications.get(position).getName());
        holder.textAdminRoute.setText(mMedications.get(position).getAdministration());
    }

    @Override
    public int getItemCount() {
        return mMedications.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textName;
        public TextView textAdminRoute;

        public MyViewHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_drug);
            textAdminRoute = view.findViewById(R.id.text_consumption_method);
        }
    }
}