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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class MedicationModel implements Subject {
    private static final MedicationModel instance = new MedicationModel();

    private final String MEDICATION_DOC_NAME = "meds";

    private ArrayList<Medication> mMedications;
    private ArrayList<Observer> mObservers;
    private FirebaseFirestore mDb;
    private FirebaseUser mCurrentUser;

    private HashMap<String, Integer> dayHashMap;
    private Medication mTempMedication;

    private MedicationModel() {
        mDb = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mMedications = new ArrayList<>();
        mObservers = new ArrayList<>();
        mTempMedication = new Medication();
        setDayHashMap();
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
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH);
            int day = dayHashMap.get(days.get(n).toString());

            while (now.get(Calendar.DAY_OF_WEEK) != day) {
                now.add(Calendar.DATE, 1);
            }

            String[] splitDate = now.getTime().toString().split(" ");

            dates.add(new Date(year, month, Integer.parseInt(splitDate[2]), Integer.parseInt(hour), Integer.parseInt(minutes)));
            dates.get(n).printDate();
        }

        mTempMedication.setConsumptionDates(dates);
    }

    public void setMedicationInfo(String medName, String medDesc, String medAdmin) {
        if (mTempMedication == null) {
            mTempMedication = new Medication();
        }

        mTempMedication.setName(medName);
        mTempMedication.setDescription(medDesc);
        mTempMedication.setAdministration(medAdmin);
    }

    public void setDayHashMap() {
        dayHashMap = new HashMap<>();
        dayHashMap.put("SUNDAY", Calendar.SUNDAY);
        dayHashMap.put("MONDAY", Calendar.MONDAY);
        dayHashMap.put("TUESDAY", Calendar.TUESDAY);
        dayHashMap.put("WEDNESDAY", Calendar.WEDNESDAY);
        dayHashMap.put("THURSDAY", Calendar.THURSDAY);
        dayHashMap.put("FRIDAY", Calendar.FRIDAY);
        dayHashMap.put("SATURDAY", Calendar.SATURDAY);
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
            observer.update(true);
        }
    }
}
