package com.example.android.mydrugjournal.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mydrugjournal.data.Allergy;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.interfaces.Subject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllergyModel implements Subject {
    private static final AllergyModel instance = new AllergyModel();
    private final String CONTACT_DOC_NAME = "allergies";
    private ArrayList<Allergy> mAllergies;
    private ArrayList<Observer> mObservers;
    private FirebaseFirestore db;
    private FirebaseUser mCurrentUser;

    private boolean hasFetchedData;

    private Allergy tmpAllergy;

    private AllergyModel() {
        db = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAllergies = new ArrayList<>();
        mObservers = new ArrayList<>();
        tmpAllergy = new Allergy();
        hasFetchedData = false;
        loadAllergies();
    }

    private void loadAllergies() {
        mAllergies.clear();
        String documentRoute = mCurrentUser.getUid() + "/" + CONTACT_DOC_NAME;
        DocumentReference docRef = db.document(documentRoute);

        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> allergies = documentSnapshot.getData();
                        for (Object allergyObj : allergies.values()) {
                            String json = new Gson().toJson(allergyObj);
                            Allergy allergy = new Gson().fromJson(json, Allergy.class);

                            if (allergyObj != null) {
                                mAllergies.add(allergy);
                            }
                        }

                        hasFetchedData = true;
                        notifyObservers();
                    }
                })
                .addOnFailureListener(e -> {

                });

    }

    public static AllergyModel getInstance() {
        return instance;
    }

    public ArrayList<Allergy> getAllergies() {
        return mAllergies;
    }

    public boolean hasFetchedData() {
        return hasFetchedData;
    }

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

    public void addNewAllergy() {
        String newId = mAllergies.size() + tmpAllergy.getAllergen();
        tmpAllergy.setId(newId);

        Map<String, Object> allergies = new HashMap<>();
        allergies.put(newId, tmpAllergy);

        db.collection(mCurrentUser.getUid()).document(CONTACT_DOC_NAME).
                set(allergies, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    mAllergies.add(tmpAllergy);
                    notifyObservers();
                    tmpAllergy = new Allergy();
                });
    }

    public void setAllergyInfo(String name, String description) {
        if (tmpAllergy == null) {
            tmpAllergy = new Allergy();
        }

        tmpAllergy.setAllergen(name);
        tmpAllergy.setDescription(description);
    }

    public void deleteAllergyById(String id) {
        String documentRoute = mCurrentUser.getUid() + "/" + CONTACT_DOC_NAME;
        DocumentReference docRef = db.document(documentRoute);
        Log.d("delete", id);
        // Remove the 'capital' field from the document
        Map<String,Object> updates = new HashMap<>();
        updates.put(id, FieldValue.delete());

        docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("delete", "DocumentSnapshot successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("delete", "Error deleting document", e);
            }
        });

        removeById(id);
    }

    private void removeById(String id) {
        for(int x = 0; x < mAllergies.size(); x++){
            if (mAllergies.get(x).getId().equals(id)) {
                mAllergies.remove(x);
                Log.d("delete", "Successfully deleted!");
                break;
            }
        }
    }
}
