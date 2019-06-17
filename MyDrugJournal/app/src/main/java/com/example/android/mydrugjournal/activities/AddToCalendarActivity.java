package com.example.android.mydrugjournal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.models.MedicationModel;

import java.util.ArrayList;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddToCalendarActivity extends AppCompatActivity {
    private MaterialDayPicker mMaterialDayPicker;
    private TimePicker mTimePicker;
    private Button mButtonAddDate;
    private Toolbar mToolbar;

    private MedicationModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_calendar);

        //Setup toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mModel = MedicationModel.getInstance();

        mMaterialDayPicker = findViewById(R.id.day_picker);

        mTimePicker = findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);

        mButtonAddDate = findViewById(R.id.button_add_date);
        mButtonAddDate.setOnClickListener(onButtonAddDateClick);
    }

    private View.OnClickListener onButtonAddDateClick = view -> {
        List<MaterialDayPicker.Weekday> daysSelected = mMaterialDayPicker.getSelectedDays();
        String hour = Integer.toString(mTimePicker.getHour());
        String minute = Integer.toString(mTimePicker.getMinute());


        for (int n = 0; n < daysSelected.size(); n++) {
            Log.i("DAY", daysSelected.get(n).toString());
        }

        Log.i("TIME", hour + ":" + minute);
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
