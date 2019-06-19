package com.example.android.mydrugjournal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.Medication;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DateMedicationsRecyclerAdapter extends RecyclerView.Adapter<DateMedicationsRecyclerAdapter.MyViewHolder> {
    private ArrayList<Medication> mMedications;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DateMedicationsRecyclerAdapter(ArrayList<Medication> myDataset) {
        mMedications = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DateMedicationsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_date_medication_template, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textName.setText(mMedications.get(position).getName());
        holder.textDescription.setText(mMedications.get(position).getDescription());
        holder.textAdminRoute.setText(mMedications.get(position).getAdministration());
    }

    @Override
    public int getItemCount() {
        return mMedications.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textName;
        public TextView textDescription;
        public TextView textAdminRoute;

        public MyViewHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_med_name);
            textDescription = view.findViewById(R.id.text_med_description);
            textAdminRoute = view.findViewById(R.id.text_med_administration);
        }
    }
}