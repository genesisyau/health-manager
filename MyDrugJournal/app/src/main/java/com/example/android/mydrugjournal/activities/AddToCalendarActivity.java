package com.example.android.mydrugjournal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.models.MedicationModel;

import java.util.ArrayList;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddToCalendarActivity extends AppCompatActivity {
    private Button mButtonAddDate;
    private MaterialDayPicker mMaterialDayPicker;

    private MedicationModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_calendar);

        mModel = MedicationModel.getInstance();

        mMaterialDayPicker = findViewById(R.id.day_picker);

        mButtonAddDate = findViewById(R.id.button_add_date);
        mButtonAddDate.setOnClickListener(onButtonAddDateClick);
    }

    private View.OnClickListener onButtonAddDateClick = view -> {
        List<MaterialDayPicker.Weekday> daysSelected = mMaterialDayPicker.getSelectedDays();

        for (int n = 0; n < daysSelected.size(); n++) {
            Log.i("DAY", daysSelected.get(n).toString());
        }
    };
}
