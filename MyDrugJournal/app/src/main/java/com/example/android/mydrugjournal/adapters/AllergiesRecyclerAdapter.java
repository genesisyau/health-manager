package com.example.android.mydrugjournal.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.Allergy;

import java.util.ArrayList;

public class AllergiesRecyclerAdapter extends RecyclerView.Adapter<AllergiesRecyclerAdapter.MyViewHolder> {
    private ArrayList<Allergy> mAllergies;

//    public interface OnContactCallClickListener{
//        void onContactCall(int position);
//    }

//    private OnContactCallClickListener onContactCallClickListener;

    // Provides a suitable constructor (depends on the kind of dataset)
    public AllergiesRecyclerAdapter(ArrayList<Allergy> myDataset/*, OnContactCallClickListener callListener*/) {
        mAllergies = myDataset;
//        onContactCallClickListener = callListener;
    }

    public void updateList(ArrayList<Allergy> newList) {
        mAllergies.clear();
        mAllergies.addAll(newList);
        notifyDataSetChanged();
    }

    // Creates new views (invoked by the layout manager)
    @Override
    public AllergiesRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_allergy_template, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.allergyName.setText(mAllergies.get(position).getAllergen());
        holder.allergyDescription.setText(mAllergies.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mAllergies.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //public OnContactCallClickListener onContactCallClickListener;

        // each data item is just a string in this case
        public TextView allergyName;
        public TextView allergyDescription;

        public MyViewHolder(View view/*, OnContactCallClickListener onContactCallClickListener*/) {
            super(view);
            //this.onContactCallClickListener = onContactCallClickListener;
            allergyName = view.findViewById(R.id.allergenName);
            allergyDescription = view.findViewById(R.id.allergenDescription);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}