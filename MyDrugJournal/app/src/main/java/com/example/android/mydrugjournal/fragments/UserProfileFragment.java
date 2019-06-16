package com.example.android.mydrugjournal.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private TextInputEditText mFirstName, mLastName, mBirthdate, mWeight, mHeight, mEmail;
    private Spinner mSex, mBlood;
    private CountryCodePicker mCountry;
    private ImageView mProfilePic;
    private Button mEditBtn, mSaveBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser fbUser;
    private DocumentReference docRef;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fbUser = mAuth.getCurrentUser();
        docRef = db.collection(fbUser.getUid()).document("profile");

        mFirstName = getView().findViewById(R.id.firstNameInput);
        mLastName = getView().findViewById(R.id.lastNameInput);
        mBirthdate = getView().findViewById(R.id.birthdateInput);
        mWeight = getView().findViewById(R.id.weightInput);
        mHeight = getView().findViewById(R.id.heightInput);
        mEmail = getView().findViewById(R.id.emailInput);
        mSex = getView().findViewById(R.id.sexInput);
        mBlood = getView().findViewById(R.id.bloodInput);
        mCountry = getView().findViewById(R.id.countryInput);
        mProfilePic = getView().findViewById(R.id.profilePic);
        mEditBtn = getView().findViewById(R.id.editBtn);
        mSaveBtn = getView().findViewById(R.id.saveBtn);

        RetrieveProfileData();

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchFieldsState(mEditBtn.isEnabled());
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
    }

    private void SaveData() {
        User tmp = new User();
        Map<String, Object> newData = new HashMap<>();

        tmp.setBloodType(mBlood.getSelectedItem().toString());
        tmp.setSex(mSex.getSelectedItem().toString());
        tmp.setWeight(Double.parseDouble(mWeight.getText().toString()));
        tmp.setHeight(Double.parseDouble(mHeight.getText().toString()));
        tmp.setBirthDate(mBirthdate.getText().toString());
        tmp.setCountry(mCountry.getSelectedCountryEnglishName());
        tmp.setLastName(mLastName.getText().toString());
        tmp.setFirstName(mFirstName.getText().toString());

        docRef.set(tmp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("profile", "DocumentSnapshot successfully written!");
                Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("profile", "Error writing document", e);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        SwitchFieldsState(mEditBtn.isEnabled());
    }

    private void SwitchFieldsState(boolean canEdit) {
        mFirstName.setEnabled(canEdit);
        mFirstName.setFocusable(canEdit);
        mFirstName.setFocusableInTouchMode(canEdit);
        mFirstName.setCursorVisible(canEdit);
        mFirstName.setClickable(canEdit);

        mLastName.setEnabled(canEdit);
        mLastName.setClickable(canEdit);
        mLastName.setFocusable(canEdit);
        mLastName.setFocusableInTouchMode(canEdit);
        mLastName.setCursorVisible(canEdit);

        mBirthdate.setEnabled(canEdit);

        mWeight.setEnabled(canEdit);
        mWeight.setClickable(canEdit);
        mWeight.setFocusable(canEdit);
        mWeight.setFocusableInTouchMode(canEdit);
        mWeight.setCursorVisible(canEdit);

        mHeight.setEnabled(canEdit);
        mHeight.setClickable(canEdit);
        mHeight.setFocusable(canEdit);
        mHeight.setFocusableInTouchMode(canEdit);
        mHeight.setCursorVisible(canEdit);

        mSex.setEnabled(canEdit);
        mSex.setClickable(canEdit);

        mBlood.setEnabled(canEdit);
        mBlood.setClickable(canEdit);

        mCountry.setEnabled(canEdit);
        mCountry.setCcpClickable(canEdit);

        mSaveBtn.setEnabled(canEdit);
        mEditBtn.setEnabled(!canEdit);
    }

    private void RetrieveProfileData() {
        mEmail.setText(fbUser.getEmail());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        user.setBloodType(document.getString("bloodType"));
//                        user.setCountry(document.getString("country"));
//                        user.setSex(document.getString("sex"));

                        mFirstName.setText(document.getString("firstName"));
                        mLastName.setText(document.getString("lastName"));
                        mBirthdate.setText(document.getString("birthDate"));
                        mHeight.setText(String.format("%s", document.getDouble("height")));
                        mWeight.setText(String.format("%s", document.getDouble("weight")));

                    } else {
                        Log.d("fb", "No such document");
                    }
                } else {
                    Log.d("fb", "get failed with ", task.getException());
                }
            }
        });

        //Log.d("fb", user.getFirstName());
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
