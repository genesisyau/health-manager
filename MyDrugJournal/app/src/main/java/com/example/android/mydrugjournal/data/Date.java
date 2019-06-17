package com.example.android.mydrugjournal.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Date {
    private String day;
    private String time;

    public Date() {
        day = "";
        time = "";
    }

    public Date(String day, String time) {
        this.day = day;
        this.time = time;
    }
}
