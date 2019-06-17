package com.example.android.mydrugjournal.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Date {
    private String year;
    private String month;
    private String day;
    private String time;

    public Date() {
        year = "";
        month = "";
        day = "";
        time = "";
    }

    public Date(String year, String month, String day, String time) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
    }
}
