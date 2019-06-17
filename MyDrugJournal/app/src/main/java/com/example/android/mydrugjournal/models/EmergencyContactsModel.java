package com.example.android.mydrugjournal.models;

import android.util.Log;

import com.example.android.mydrugjournal.data.EmergencyContact;
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

public class EmergencyContactsModel implements Subject {
    private static final EmergencyContactsModel instance = new EmergencyContactsModel();
    private final String CONTACT_DOC_NAME = "contacts";
    private final String NAME_FIELD = "name";
    private final String ADDRESS_FIELD = "address";
    private final String PHONE_NUMBER_FIELD = "phoneNumber";

    private ArrayList<EmergencyContact> mContacts;
    private ArrayList<Observer> mObservers;
    private FirebaseFirestore db;
    private FirebaseUser mCurrentUser;

    private EmergencyContactsModel(){
        db = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mContacts = new ArrayList<>();
        mObservers = new ArrayList<>();
        loadContacts();
    }

    public static EmergencyContactsModel getInstance() {
        return instance;
    }

    private EmergencyContact stringToClass(String data) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String name = obj.getString(NAME_FIELD);
            String address = obj.getString(ADDRESS_FIELD);
            String phoneNumber = obj.getString(PHONE_NUMBER_FIELD);

            return new EmergencyContact(name, address, phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<EmergencyContact> getContacts() {
        return mContacts;
    }
    public ArrayList<Observer> getObservers() {
        return mObservers;
    }

    public void setFirestoreInstance() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
    }

    public void addNewEmergencyContact(String name, String address, String phoneNumber) {
        Map<String, Object> contacts = new HashMap<>();
        EmergencyContact emergencyContact = new EmergencyContact(name, address, phoneNumber);
        contacts.put(Integer.toString(mContacts.size()), emergencyContact);

        db.collection(mCurrentUser.getUid()).document(CONTACT_DOC_NAME).
                set(emergencyContact, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    mContacts.add(new EmergencyContact(name, address, phoneNumber));
                    notifyObservers();
                });
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

    private void loadContacts() {
        String documentRoute = mCurrentUser.getUid() + "/" + CONTACT_DOC_NAME;
        DocumentReference docRef = db.document(documentRoute);

        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> contacts = documentSnapshot.getData();
                        for (Object medication : contacts.values()) {
                            mContacts.add(stringToClass(medication.toString()));
                        }

                        notifyObservers();
                    }
                })
                .addOnFailureListener(e -> {

                });
    }
}
