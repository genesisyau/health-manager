package com.example.android.mydrugjournal.data;

import android.util.Log;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Date {
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

    public void printDate() {
        Log.d("YEAR", Integer.toString(year));
        Log.d("MONTH", Integer.toString(month));
        Log.d("DAY", Integer.toString(day));
        Log.d("TIME", hour + ":" + minute);
    }
}
