package com.example.android.mydrugjournal.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.activities.AddToCalendarActivity;

public class AddToCalendarDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.add_to_calendar_message))
                 .setPositiveButton(getString(R.string.add_to_calendar_button_text), (dialog, which) -> {
                     Intent intent = new Intent(getActivity(), AddToCalendarActivity.class);
                     startActivity(intent);
                 })
                 .setNegativeButton(getString(R.string.save_anyway), (dialog, which) ->
                     Toast.makeText(getActivity(), "Save without adding", Toast.LENGTH_SHORT).show()
                 );

        return builder.create();
    }
}
