package com.example.android.mydrugjournal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.adapters.DateMedicationsRecyclerAdapter;
import com.example.android.mydrugjournal.data.Medication;
import com.example.android.mydrugjournal.fragments.WeeklyScheduleFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateMedicationsActivity extends AppCompatActivity {
    private final String OLD_FORMAT = "dd/MM/yyyy";
    private final String NEW_FORMAT = "EEEE, d MMM yyyy";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerMedications;
    private DateMedicationsRecyclerAdapter mRecyclerAdapter;

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
        String date = bundle.getString("DATEKEY");

        Log.i("MEDS", Integer.toString(mMedications.size()));

        mRecyclerMedications = findViewById(R.id.recycler_date_medications);
        mRecyclerMedications.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new DateMedicationsRecyclerAdapter(mMedications);
        mRecyclerMedications.setAdapter(mRecyclerAdapter);

        setToolbarTitle(date);
    }

    private void setToolbarTitle(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        String newDateString = sdf.format(d);

        getSupportActionBar().setTitle(newDateString);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
