package com.example.android.mydrugjournal.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Date implements Parcelable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean isConsumed;

    public Date() {
        year = -1;
        month = -1;
        hour = -1;
        minute = -1;
        day = -1;
        isConsumed = false;
    }

    public Date(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        isConsumed = false;
    }

    protected Date(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        isConsumed = in.readByte() != 0;
    }

    public static final Creator<Date> CREATOR = new Creator<Date>() {
        @Override
        public Date createFromParcel(Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };

    public void printDate() {
        Log.d("YEAR", Integer.toString(year));
        Log.d("MONTH", Integer.toString(month));
        Log.d("DAY", Integer.toString(day));
        Log.d("TIME", hour + ":" + minute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeByte((byte) (isConsumed ? 1 : 0));
    }
}
