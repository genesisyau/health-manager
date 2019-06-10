package com.example.android.mydrugjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EmergencyNumbersFragment extends Fragment {

    private EditText mContactName, mContactPhone, mContactAddress;
    private DialogFragment mSaveContactDialogFragment;
    private FloatingActionButton mAddFab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactName = (EditText) view.findViewById(R.id.contactNameInput);
        mContactPhone = (EditText) view.findViewById(R.id.contactPhoneNumberInput);
        mContactAddress = (EditText) view.findViewById(R.id.contactAddressInput);

        mAddFab = view.findViewById(R.id.addContactFab);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
//        onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency_numbers, container, false);
    }

//    @Override
//    public Dialog onCreateDialogFragment(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.dialog_add_new_contact, null))
//                // Add action buttons
//                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        Log.d("dialog", "save clicked");
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Log.d("dialog", "cancel clicked");
//                    }
//                });
//        return builder.create();
//    }
}
