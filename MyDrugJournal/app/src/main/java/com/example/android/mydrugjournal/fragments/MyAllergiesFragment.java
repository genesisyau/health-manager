package com.example.android.mydrugjournal.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.activities.AddNewAllergyActivity;
import com.example.android.mydrugjournal.adapters.AllergiesRecyclerAdapter;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.AllergyModel;

public class MyAllergiesFragment extends Fragment implements Observer {

    private FloatingActionButton mAddFab;
    private ProgressBar progressBar;
    private RecyclerView recyclerAllergies;
    private AllergiesRecyclerAdapter allergiesAdapter;
    private AllergyModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_allergies, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressBar);
        hideSpinner();
        mModel = AllergyModel.getInstance();
        mModel.register(this);

        recyclerAllergies = getView().findViewById(R.id.allergiesRecyclerView);
        recyclerAllergies.setLayoutManager(new LinearLayoutManager(getActivity()));
        allergiesAdapter = new AllergiesRecyclerAdapter(mModel.getAllergies());
        recyclerAllergies.setAdapter(allergiesAdapter);

        mAddFab = getView().findViewById(R.id.addAllergyFab);
        mAddFab.setOnClickListener(onAddAllergyClicked);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String id = mModel.getAllergies().get(viewHolder.getAdapterPosition()).getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.confirm_delete))
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            mModel.deleteAllergyById(id);
                            allergiesAdapter = new AllergiesRecyclerAdapter(mModel.getAllergies());
                            recyclerAllergies.setAdapter(allergiesAdapter);
                            Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        allergiesAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                        Toast.makeText(getActivity(), "Deletion cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).show();
            }
        });

        helper.attachToRecyclerView(recyclerAllergies);
    }

    private View.OnClickListener onAddAllergyClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), AddNewAllergyActivity.class);
            startActivity(intent);
        }
    };

    public void showSpinner() {
        progressBar.setVisibility(getView().VISIBLE);
    }

    public void hideSpinner() {
        progressBar.setVisibility(getView().GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        showSpinner();
        if (mModel.hasFetchedData()) {
            hideSpinner();
        }
    }

    @Override
    public void update() {
        allergiesAdapter = new AllergiesRecyclerAdapter(mModel.getAllergies());
        recyclerAllergies.setAdapter(allergiesAdapter);
        allergiesAdapter.notifyDataSetChanged();
        hideSpinner();
    }

    @Override
    public void update(boolean add) {

    }
}
