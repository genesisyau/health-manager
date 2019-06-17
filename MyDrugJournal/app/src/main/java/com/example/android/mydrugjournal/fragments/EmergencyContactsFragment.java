package com.example.android.mydrugjournal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.activities.AddNewEmergencyContactActivity;
import com.example.android.mydrugjournal.adapters.EmergencyContactsRecyclerAdapter;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.EmergencyContactsModel;

public class EmergencyContactsFragment extends Fragment implements Observer {

    private FloatingActionButton mAddFab;
    private RecyclerView recyclerContacts;
    private EmergencyContactsRecyclerAdapter contactsAdapter;

    private EmergencyContactsModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency_numbers, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mModel = EmergencyContactsModel.getInstance();
        mModel.register(this);

        recyclerContacts = getView().findViewById(R.id.emergencyContactsRecyclerView);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsAdapter = null;

        mAddFab = getView().findViewById(R.id.addContactFab);
        mAddFab.setOnClickListener(onAddContactClicked);
    }

    private View.OnClickListener onAddContactClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), AddNewEmergencyContactActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(MODEL_KEY, mModel);
//            intent.putExtras(bundle);

            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void update() {
        Toast.makeText(getContext(), "update", Toast.LENGTH_SHORT).show();
        contactsAdapter = new EmergencyContactsRecyclerAdapter(mModel.getContacts());
        recyclerContacts.setAdapter(contactsAdapter);

    }

}
