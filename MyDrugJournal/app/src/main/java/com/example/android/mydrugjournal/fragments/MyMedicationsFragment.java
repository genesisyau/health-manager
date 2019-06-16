package com.example.android.mydrugjournal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mydrugjournal.activities.AddNewMedicationActivity;
import com.example.android.mydrugjournal.R;

public class MyMedicationsFragment extends Fragment {
    private FloatingActionButton mFabAddMedication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_medications, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFabAddMedication = getView().findViewById(R.id.fab_add_medication);
        mFabAddMedication.setOnClickListener(onAddMedicationClicked);
    }

    private View.OnClickListener onAddMedicationClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getContext(), AddNewMedicationActivity.class));
        }
    };
}
