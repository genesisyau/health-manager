package com.example.android.mydrugjournal.models;

import android.util.Log;

import com.example.android.mydrugjournal.data.Date;
import com.example.android.mydrugjournal.data.Medication;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.interfaces.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class MedicationModel implements Subject {
    private static final MedicationModel instance = new MedicationModel();

    private final String MEDICATION_DOC_NAME = "meds";
    private final String ID_FIELD = "id";
    private final String NAME_FIELD = "name";
    private final String DESCRIPTION_FIELD = "description";
    private final String ADMINISTRATION_FIELD = "administration";
    private final String DATE_FIELD = "consumptionDates";

    private ArrayList<Medication> mMedications;
    private ArrayList<Observer> mObservers;
    private FirebaseFirestore mDb;
    private FirebaseUser mCurrentUser;

    private Medication mTempMedication;

    private MedicationModel() {
        mDb = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mMedications = new ArrayList<>();
        mObservers = new ArrayList<>();
        mTempMedication = new Medication();
        loadMedications();
    }

    public static MedicationModel getInstance() {
        return instance;
    }

    //************************//
    //****Class Functions*****//
    //************************//
    public ArrayList<Medication> getMedications() {
        return mMedications;
    }

    public void addNewMedication() {
        String newMedId = mMedications.size() + mTempMedication.getName();
        mTempMedication.setId(newMedId);

        Map<String, Object> medication = new HashMap<>();
        medication.put(newMedId, mTempMedication);

        mDb.collection(mCurrentUser.getUid()).document(MEDICATION_DOC_NAME).
                set(medication, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    mMedications.add(mTempMedication);
                    mTempMedication = null;
                    notifyObservers();
                });
    }

    private void loadMedications() {
        String documentRoute = mCurrentUser.getUid() + "/" + MEDICATION_DOC_NAME;
        DocumentReference docRef = mDb.document(documentRoute);

        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> medications = documentSnapshot.getData();
                        for (Object medication : medications.values()) {
                            String json = new Gson().toJson(medication);
                            Medication med = new Gson().fromJson(json, Medication.class);

                            if (med != null) {
                                mMedications.add(med);
                            }
                        }

                        notifyObservers();
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    public void appendConsumptionDate(List<MaterialDayPicker.Weekday> days, String hour, String minutes) {
        if (mTempMedication == null) {
            mTempMedication = new Medication();
        }

        ArrayList<Date> dates = new ArrayList<>();
        for (int n = 0; n < days.size(); n++) {
            dates.add(new Date(days.get(n).toString(), hour + ":" + minutes));
        }

        mTempMedication.setConsumptionDates(dates);
    }

    public void setMedicationInfo(String medName, String medDesc, String medAdmin) {
        mTempMedication.setName(medName);
        mTempMedication.setDescription(medDesc);
        mTempMedication.setAdministration(medAdmin);
    }

    //***************************//
    //Subject Interface Functions//
    //***************************//
    @Override
    public void register(Observer observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : mObservers) {
            observer.update();
        }
    }
}
