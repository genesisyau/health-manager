package com.example.android.mydrugjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewMedicationActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Spinner mSpinnerRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medication);

        //Setup toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setup spinner
        mSpinnerRoutes = findViewById(R.id.spinnner_administration_routes);
        mSpinnerRoutes.setOnItemSelectedListener(onAdministrationRouteSelected);
        mSpinnerRoutes.setSelection(0, true);
    }

    private AdapterView.OnItemSelectedListener onAdministrationRouteSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
