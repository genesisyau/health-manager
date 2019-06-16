package com.example.android.mydrugjournal.fragments;


import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.android.mydrugjournal.R;
import com.hbb20.CountryCodePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyScheduleFragment extends Fragment {
    private WeekView mWeekView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWeekView = getView().findViewById(R.id.week_view);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);
    }

    MonthLoader.MonthChangeListener mMonthChangeListener = (newYear, newMonth) -> {
        // Populate the week view with some events.
        List<WeekViewEvent> events = getEvents(newYear, newMonth);
        Log.d("EVENT LENGTH", Integer.toString(events.size()));
        return events;
    };

    WeekView.EventClickListener mEventClickListener = (event, eventRect) -> {

    };

    WeekView.EventLongPressListener mEventLongPressListener = (event, eventRect) -> {

    };

    public List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();

        // Initialize start and end time.
        Calendar now = Calendar.getInstance();
        Calendar eventTime = (Calendar) now.clone();
        eventTime.set(Calendar.YEAR, newYear);
        eventTime.set(Calendar.MONTH, newMonth);
        eventTime.set(Calendar.DAY_OF_MONTH, 10);
        eventTime.set(Calendar.HOUR_OF_DAY, 12);
        eventTime.set(Calendar.MINUTE, 0);
        Log.d("TIME", Long.toString(eventTime.getTimeInMillis()));

        Calendar endTime = (Calendar) now.clone();
        endTime.set(Calendar.YEAR, newYear);
        endTime.set(Calendar.MONTH, newMonth);
        endTime.set(Calendar.DAY_OF_MONTH, 10);
        endTime.set(Calendar.HOUR_OF_DAY, 13);
        endTime.set(Calendar.MINUTE, 0);

        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setId(12345);
        weekViewEvent.setName("Pill 1");
        weekViewEvent.setStartTime(eventTime);
        weekViewEvent.setEndTime(endTime);
        weekViewEvent.setColor(Color.parseColor("#32CD32"));

        events.add(weekViewEvent);
        return events;
    }

}
