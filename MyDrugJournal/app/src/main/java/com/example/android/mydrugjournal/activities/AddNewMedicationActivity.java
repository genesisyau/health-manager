package com.example.android.mydrugjournal.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcel;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.fragments.MyMedicationsFragment;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.MedicationModel;

public class AddNewMedicationActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Spinner mSpinnerRoutes;
    private Button mButtonSave;
    private TextInputEditText mEditTextName;
    private TextInputEditText mEditTextDescription;
    private MedicationModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medication);

//        Bundle bundle = getIntent().getExtras();
//        mModel = bundle.getParcelable(MyMedicationsFragment.MODEL_KEY);
//        mModel.setFirestoreInstance();
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
    }

    //************************//
    //*******Listeners********//
    //************************//
    private View.OnClickListener onButtonSaveClick = view -> {
        String medName = mEditTextName.getText().toString();
        String medDesc = mEditTextDescription.getText().toString();
        String medAdmin = mSpinnerRoutes.getSelectedItem().toString();

        mModel.addNewMedication(medName, medDesc, medAdmin);
        finish();
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
