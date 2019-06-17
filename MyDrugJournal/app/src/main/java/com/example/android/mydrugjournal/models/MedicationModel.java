package com.example.android.mydrugjournal.models;

import com.example.android.mydrugjournal.data.Medication;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.interfaces.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MedicationModel implements Subject {
    private static final MedicationModel instance = new MedicationModel();

    private final String MEDICATION_DOC_NAME = "meds";
    private final String ID_FIELD = "id";
    private final String NAME_FIELD = "name";
    private final String DESCRIPTION_FIELD = "description";
    private final String ADMINISTRATION_FIELD = "administration";

    private ArrayList<Medication> mMedications;
    private ArrayList<Observer> mObservers;
    private FirebaseFirestore mDb;
    private FirebaseUser mCurrentUser;

    private MedicationModel() {
        mDb = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mMedications = new ArrayList<>();
        mObservers = new ArrayList<>();
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

    public void addNewMedication(String name, String description, String adRoute) {
        String newMedId = Integer.toString(mMedications.size());

        Map<String, Object> medication = new HashMap<>();
        Medication med = new Medication(newMedId, name, description, adRoute);
        medication.put(newMedId, med);

        mDb.collection(mCurrentUser.getUid()).document(MEDICATION_DOC_NAME).
                set(medication, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    mMedications.add(new Medication(newMedId, name, description, adRoute));
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
                            Medication med = stringToClass(medication.toString());

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

    private Medication stringToClass(String data) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String id = obj != null ? obj.getString(ID_FIELD) : null;
            String name = obj != null ? obj.getString(NAME_FIELD) : null;
            String description = obj != null ? obj.getString(DESCRIPTION_FIELD) : null;
            String administration = obj != null ? obj.getString(ADMINISTRATION_FIELD) : null;

            return new Medication(id, name, description, administration);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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
