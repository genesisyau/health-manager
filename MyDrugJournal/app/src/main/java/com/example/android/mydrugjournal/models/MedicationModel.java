package com.example.android.mydrugjournal.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mydrugjournal.data.Medication;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.interfaces.Subject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

public class MedicationModel implements Subject {
    private static final MedicationModel instance = new MedicationModel();

    private final String MEDICATION_DOC_NAME = "meds";
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


//    //************************//
//    //**Parcelable Functions**//
//    //************************//
//    protected MedicationModel(Parcel in) {
//        mMedications = in.createTypedArrayList(Medication.CREATOR);
//        mCurrentUser = in.readParcelable(FirebaseUser.class.getClassLoader());
//    }
//
//    public static final Creator<MedicationModel> CREATOR = new Creator<MedicationModel>() {
//        @Override
//        public MedicationModel createFromParcel(Parcel in) {
//            return new MedicationModel(in);
//        }
//
//        @Override
//        public MedicationModel[] newArray(int size) {
//            return new MedicationModel[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeTypedList(mMedications);
//        dest.writeParcelable(mCurrentUser, flags);
//    }

    //************************//
    //****Class Functions*****//
    //************************//
    public ArrayList<Medication> getMedications() {
        return mMedications;
    }

    public ArrayList<Observer> getObservers() {
        return mObservers;
    }

    public void setFirestoreInstance() {
        if (mDb == null) {
            mDb = FirebaseFirestore.getInstance();
        }
    }

    public void addNewMedication(String name, String description, String adRoute) {
        Map<String, Object> medication = new HashMap<>();
        Medication med = new Medication(name, description, adRoute);
        medication.put(Integer.toString(mMedications.size()), med);

        mDb.collection(mCurrentUser.getUid()).document(MEDICATION_DOC_NAME).
                set(medication, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    mMedications.add(new Medication(name, description, adRoute));
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
                            mMedications.add(stringToClass(medication.toString()));
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
            String name = obj.getString(NAME_FIELD);
            String description = obj.getString(DESCRIPTION_FIELD);
            String administration = obj.getString(ADMINISTRATION_FIELD);

            return new Medication(name, description, administration);
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
