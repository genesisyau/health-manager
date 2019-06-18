package com.example.android.mydrugjournal.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.EmergencyContact;
import com.example.android.mydrugjournal.models.EmergencyContactsModel;

public class AddNewEmergencyContactActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputEditText mName, mAddress, mPhoneNumber;
    private Button mSaveBtn;
    private EmergencyContactsModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency_contact);

        mModel = EmergencyContactsModel.getInstance();

        //Setup toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mName = findViewById(R.id.newContactName);
        mAddress = findViewById(R.id.newContactAddress);
        mPhoneNumber = findViewById(R.id.newContactPhoneNumber);
        mSaveBtn = findViewById(R.id.saveContactBtn);

        mSaveBtn.setOnClickListener(onSaveContactClick);
    }

    private View.OnClickListener onSaveContactClick = view -> {
        String contactName = mName.getText().toString();
        String contactAddress = mAddress.getText().toString();
        String contactPhoneNumber = mPhoneNumber.getText().toString();

        mModel.setContactInfo(contactName, contactAddress, contactPhoneNumber);
        mModel.addNewEmergencyContact();
        finish();
    };
}
