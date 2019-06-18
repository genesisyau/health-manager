package com.example.android.mydrugjournal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.Medication;
import com.example.android.mydrugjournal.fragments.WeeklyScheduleFragment;

import java.util.ArrayList;

public class DateMedicationsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTextMedName;
    private TextView mTextMedDescription;
    private TextView mTextMedAdministration;

    private ArrayList<Medication> mMedications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_medications);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mMedications = bundle.getParcelableArrayList("MEDKEY");

        mTextMedName = findViewById(R.id.text_medication_name);
        mTextMedDescription = findViewById(R.id.text_medication_description);
        mTextMedAdministration = findViewById(R.id.text_administration_type);

        mTextMedName.setText("Name: " + mMedications.get(0).getName());
        mTextMedDescription.setText("Description: " + mMedications.get(0).getDescription());
        mTextMedAdministration.setText("Administration Route: " + mMedications.get(0).getAdministration());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
