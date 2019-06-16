package com.example.android.mydrugjournal.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private EditText mFirstName, mLastName, mBirthdate, mWeight, mHeight, mPassword1, mPassword2, mEmail;
    private Spinner mSex, mBlood;
    private CountryCodePicker mCountry;
    private ImageView mProfilePic;
    private Button mEditBtn, mSaveBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mFirstName = getView().findViewById(R.id.firstNameInput);
        mLastName =  getView().findViewById(R.id.lastNameInput);
        mBirthdate = getView().findViewById(R.id.birthdateInput);
        mWeight = getView().findViewById(R.id.weightInput);
        mHeight = getView().findViewById(R.id.heightInput);
        mPassword1 = getView().findViewById(R.id.passwordInput);
        mPassword2 = getView().findViewById(R.id.confirmPasswordInput);
        mEmail = getView().findViewById(R.id.emailInput);
        mSex = getView().findViewById(R.id.sexInput);
        mBlood = getView().findViewById(R.id.bloodInput);
        mCountry = getView().findViewById(R.id.countryInput);
        mProfilePic = getView().findViewById(R.id.profilePic);
        mEditBtn = getView().findViewById(R.id.editBtn);
        mSaveBtn = getView().findViewById(R.id.saveBtn);

        if (user != null){
            RetrieveDataFromDB();
        }

        mEmail.setText(mAuth.getCurrentUser().getEmail());
    }

    private void RetrieveDataFromDB() {
        db = FirebaseFirestore.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
