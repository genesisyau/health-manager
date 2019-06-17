package com.example.android.mydrugjournal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.dialogs.AddToCalendarDialog;
import com.example.android.mydrugjournal.models.MedicationModel;

public class AddNewMedicationActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Spinner mSpinnerRoutes;
    private Button mButtonSave;
    private Button mButtonAddToCalendar;
    private TextInputEditText mEditTextName;
    private TextInputEditText mEditTextDescription;
    private MedicationModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medication);

        mModel = MedicationModel.getInstance();

        //Setup toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setup spinner
        mSpinnerRoutes = findViewById(R.id.spinnner_administration_routes);
        mSpinnerRoutes.setOnItemSelectedListener(onAdministrationRouteSelected);
        mSpinnerRoutes.setSelection(0, true);

        //SetupViews
        mEditTextName = findViewById(R.id.edit_text_med_name);
        mEditTextDescription = findViewById(R.id.edit_text_med_description);

        //Setup Button
        mButtonSave = findViewById(R.id.button_save_medication);
        mButtonSave.setOnClickListener(onButtonSaveClick);

        mButtonAddToCalendar = findViewById(R.id.button_add_to_calendar);
        mButtonAddToCalendar.setOnClickListener(onButtonAddToCalendarClick);
    }

    //************************//
    //*******Listeners********//
    //************************//
    private View.OnClickListener onButtonSaveClick = view -> {
//        AddToCalendarDialog dialog = new AddToCalendarDialog();
//        dialog.show(getSupportFragmentManager(), "confirmation dialog");
        String medName = mEditTextName.getText().toString();
        String medDesc = mEditTextDescription.getText().toString();
        String medAdmin = mSpinnerRoutes.getSelectedItem().toString();
//
        mModel.addNewMedication(medName, medDesc, medAdmin);
        finish();
    };

    private View.OnClickListener onButtonAddToCalendarClick = view -> {
        Intent intent = new Intent(this, AddToCalendarActivity.class);
        startActivity(intent);
    };

    private AdapterView.OnItemSelectedListener onAdministrationRouteSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    //************************//
    //***Activity Functions***//
    //************************//
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
