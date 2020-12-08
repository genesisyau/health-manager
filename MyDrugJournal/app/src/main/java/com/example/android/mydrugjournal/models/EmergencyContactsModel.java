package com.example.android.mydrugjournal.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mydrugjournal.data.EmergencyContact;
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

    private boolean hasFetchedData;

    private EmergencyContact tmpContact;

    private EmergencyContactsModel() {
        db = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mContacts = new ArrayList<>();
        mObservers = new ArrayList<>();
        tmpContact = new EmergencyContact();
        hasFetchedData = false;
        loadContacts();
    }

    public static EmergencyContactsModel getInstance() {
        return instance;
    }

    public ArrayList<EmergencyContact> getContacts() {
        return mContacts;
    }

    public boolean hasFetchedData() {
        return hasFetchedData;
    }

    public ArrayList<Observer> getObservers() {
        return mObservers;
    }

    public void setFirestoreInstance() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
    }

    public void addNewEmergencyContact() {
        String newMedId = mContacts.size() + tmpContact.getName();
        tmpContact.setId(newMedId);

        Map<String, Object> contacts = new HashMap<>();
        contacts.put(newMedId, tmpContact);

        db.collection(mCurrentUser.getUid()).document(CONTACT_DOC_NAME).
                set(contacts, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    mContacts.add(tmpContact);
                    notifyObservers();
                    tmpContact = new EmergencyContact();
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
                        for (Object contact : contacts.values()) {
                            String json = new Gson().toJson(contact);
                            EmergencyContact emergencyContact = new Gson().fromJson(json, EmergencyContact.class);

                            if (contact != null) {
                                mContacts.add(emergencyContact);
                            }
                        }

                        hasFetchedData = true;
                        notifyObservers();
                    }
                })
                .addOnFailureListener(e -> {

                });

    }

    public void setContactInfo(String contactName, String contactAddress, String contactPhoneNumber) {
        if (tmpContact== null) {
            tmpContact = new EmergencyContact();
        }
        tmpContact.setName(contactName);
        tmpContact.setAddress(contactAddress);
        tmpContact.setPhoneNumber(contactPhoneNumber);
    }

    public void deleteContactById(String id) {
        String documentRoute = mCurrentUser.getUid() + "/" + CONTACT_DOC_NAME;
        DocumentReference docRef = db.document(documentRoute);

        // Remove the 'id' field from the document
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
        for(int x = 0; x < mContacts.size(); x++){
            if (mContacts.get(x).getId().equals(id)) {
                mContacts.remove(x);
                Log.d("delete", "Successfully deleted!");
                break;
            }
        }
    }
}
