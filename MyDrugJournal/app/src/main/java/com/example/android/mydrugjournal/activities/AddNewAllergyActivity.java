package com.example.android.mydrugjournal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.models.AllergyModel;

public class AddNewAllergyActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputEditText mName, mDescription;
    private Button mSaveBtn;
    private AllergyModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergies);

        mModel = AllergyModel.getInstance();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mName = findViewById(R.id.newAllergyName);
        mDescription = findViewById(R.id.newAllergyDescription);
        mSaveBtn = findViewById(R.id.saveAllergyBtn);
        mSaveBtn.setOnClickListener(onSaveAllergyClick);
    }

    private View.OnClickListener onSaveAllergyClick = view -> {
        String allergyName = mName.getText().toString();
        String allergyDescription = mDescription.getText().toString();

        mModel.setAllergyInfo(allergyName, allergyDescription);
        mModel.addNewAllergy();
        finish();
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
