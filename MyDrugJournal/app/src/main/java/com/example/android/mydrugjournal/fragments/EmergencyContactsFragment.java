package com.example.android.mydrugjournal.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.activities.AddNewEmergencyContactActivity;
import com.example.android.mydrugjournal.adapters.EmergencyContactsRecyclerAdapter;
import com.example.android.mydrugjournal.data.EmergencyContact;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.EmergencyContactsModel;

public class EmergencyContactsFragment extends Fragment implements Observer, EmergencyContactsRecyclerAdapter.OnContactCallClickListener {

    private FloatingActionButton mAddFab;
    private ProgressBar progressBar;
    private RecyclerView recyclerContacts;
    private EmergencyContactsRecyclerAdapter contactsAdapter;
    private EmergencyContactsModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressBar);
        hideSpinner();
        mModel = EmergencyContactsModel.getInstance();
        mModel.register(this);

        recyclerContacts = getView().findViewById(R.id.emergencyContactsRecyclerView);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsAdapter = new EmergencyContactsRecyclerAdapter(mModel.getContacts(), this);
        recyclerContacts.setAdapter(contactsAdapter);

        mAddFab = getView().findViewById(R.id.addContactFab);
        mAddFab.setOnClickListener(onAddContactClicked);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String id = mModel.getContacts().get(viewHolder.getAdapterPosition()).getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.confirm_delete))
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            mModel.deleteContactById(id);
                            contactsAdapter = new EmergencyContactsRecyclerAdapter(mModel.getContacts());
                            recyclerContacts.setAdapter(contactsAdapter);
                            Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        contactsAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                        Toast.makeText(getActivity(), "Deletion cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).show();
            }
        });

        helper.attachToRecyclerView(recyclerContacts);
    }

    private View.OnClickListener onAddContactClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), AddNewEmergencyContactActivity.class);
            startActivity(intent);
        }
    };

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
        showSpinner();
        contactsAdapter = new EmergencyContactsRecyclerAdapter(mModel.getContacts(), this);
        recyclerContacts.setAdapter(contactsAdapter);
        hideSpinner();
    }

    @Override
    public void update(boolean add) {

    }

    @Override
    public void onContactCall(int position) {
        EmergencyContact contact = mModel.getContacts().get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.call_contact))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                    startActivity(phoneIntent);
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) ->
                        Toast.makeText(getActivity(), "Call cancelled", Toast.LENGTH_SHORT).show()
                ).show();
    }

    public void showSpinner() {
        progressBar.setVisibility(getView().VISIBLE);
    }

    public void hideSpinner() {
        progressBar.setVisibility(getView().GONE);
    }
}
