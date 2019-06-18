package com.example.android.mydrugjournal.models;

import com.example.android.mydrugjournal.adapters.AllergiesRecyclerAdapter;
import com.example.android.mydrugjournal.data.Allergy;
import com.example.android.mydrugjournal.fragments.MyAllergiesFragment;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.interfaces.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllergyModel implements Subject {
    private static final AllergyModel instance = new AllergyModel();
    private final String CONTACT_DOC_NAME = "allergies";
    private boolean isFetched;
    private ArrayList<Allergy> mAllergies;
    private ArrayList<Observer> mObservers;
    private FirebaseFirestore db;
    private FirebaseUser mCurrentUser;

    private Allergy tmpAllergy;

    private AllergyModel(){
        db = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        isFetched = false;
        mAllergies = new ArrayList<>();
        mObservers = new ArrayList<>();
        tmpAllergy = new Allergy();
        loadAllergies();
    }

    private void loadAllergies() {
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

                        notifyObservers();
                        isFetched = true;
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    public static AllergyModel getInstance() {
        return instance;
    }

    public ArrayList<Allergy> getAllergies(){ return mAllergies;}

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

    public boolean getIsFetched() {
        return isFetched;
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
                });
    }

    public void setAllergyInfo(String name, String description) {
        tmpAllergy.setAllergen(name);
        tmpAllergy.setDescription(description);
    }
}
