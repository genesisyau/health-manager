package com.example.android.mydrugjournal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mydrugjournal.activities.AddNewMedicationActivity;
import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.adapters.MedicationsRecyclerAdapter;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.MedicationModel;

public class MyMedicationsFragment extends Fragment implements Observer {
    public static final String MODEL_KEY = "MODELKEY";

    private FloatingActionButton mFabAddMedication;
    private RecyclerView recyclerMedications;
    private MedicationsRecyclerAdapter recyclerAdapter;

    private MedicationModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_medications, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mModel = MedicationModel.getInstance();
        mModel.register(this);

        recyclerMedications = getView().findViewById(R.id.my_recycler_view);
        recyclerMedications.setLayoutManager(new LinearLayoutManager(getActivity()));
        setRecyclerAdapter();

        mFabAddMedication = getView().findViewById(R.id.fab_add_medication);
        mFabAddMedication.setOnClickListener(onAddMedicationClicked);
    }

    private View.OnClickListener onAddMedicationClicked = view -> {
        Intent intent = new Intent(getContext(), AddNewMedicationActivity.class);

        startActivity(intent);
    };

    private void setRecyclerAdapter() {
        recyclerAdapter = new MedicationsRecyclerAdapter(mModel.getMedications());
        recyclerMedications.setAdapter(recyclerAdapter);
    }

    @Override
    public void update() {
        setRecyclerAdapter();
    }

    @Override
    public void update(boolean add) {

    }
}
