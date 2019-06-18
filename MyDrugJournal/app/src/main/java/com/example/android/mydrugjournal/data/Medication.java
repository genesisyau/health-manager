package com.example.android.mydrugjournal.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Medication implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String administration;
    private ArrayList<Date> consumptionDates;

    public Medication(String id, String name, String description, String adRoute) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.administration = adRoute;
    }

    public Medication(String id, String name, String description, String adRoute, ArrayList<Date> dates) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.administration = adRoute;
        this.consumptionDates = dates;
    }

    protected Medication(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        administration = in.readString();
        consumptionDates = in.createTypedArrayList(Date.CREATOR);
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    public void setConsumptionDates(ArrayList<Date> dates) {
        if (consumptionDates == null || consumptionDates.size() == 0) {
            consumptionDates = dates;
        }
        else {
            consumptionDates.addAll(dates);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(administration);
        dest.writeTypedList(consumptionDates);
    }
}
