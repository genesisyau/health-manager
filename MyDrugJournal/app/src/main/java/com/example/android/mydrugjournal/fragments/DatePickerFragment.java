package com.example.android.mydrugjournal.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.android.mydrugjournal.activities.MainActivity;
import com.example.android.mydrugjournal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        MainActivity activity = (MainActivity) getActivity();
        activity.processDatePickerResult(year, month, day);
    }

    public DatePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        // Create a new instance of DatePickerDialog and return it.
        return dpDialog;
    }

}
