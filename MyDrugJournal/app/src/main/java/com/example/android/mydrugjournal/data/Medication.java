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
public class Medication {
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

    public void setConsumptionDates(ArrayList<Date> dates) {
        if (consumptionDates == null || consumptionDates.size() == 0) {
            consumptionDates = dates;
        }
        else {
            consumptionDates.addAll(dates);
        }
    }
}
