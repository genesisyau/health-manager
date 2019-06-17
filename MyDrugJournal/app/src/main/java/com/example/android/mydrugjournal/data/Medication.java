package com.example.android.mydrugjournal.data;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Medication implements Parcelable {
    private String name;
    private String description;
    private String administration;
    private Date consumptionDate;

    public Medication(String name, String description, String adRoute) {
        this.name = name;
        this.description = description;
        this.administration = adRoute;
    }

    public Medication(String name, String description, String adRoute, Date date) {
        this.name = name;
        this.description = description;
        this.administration = adRoute;
        this.consumptionDate = date;
    }

    protected Medication(Parcel in) {
        name = in.readString();
        description = in.readString();
        administration = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(administration);
    }
}
