package com.example.android.mydrugjournal.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.media.RemoteController;
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
import com.alamkanak.weekview.WeekViewLoader;
import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.activities.DateMedicationsActivity;
import com.example.android.mydrugjournal.data.Date;
import com.example.android.mydrugjournal.data.Medication;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.MedicationModel;
import com.hbb20.CountryCodePicker;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class WeeklyScheduleFragment extends Fragment implements Observer {
    private final static String MEDICATIONS_KEY = "MEDKEY";
    private final static String DATE_KEY = "DATEKEY";

    private WeekView mWeekView;

    private MedicationModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mModel = MedicationModel.getInstance();
        mModel.register(this);

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
        Log.i("EVENT", "event");
        return getEvents(newYear, newMonth);
    };

    WeekView.EventClickListener mEventClickListener = (event, eventRect) -> {
        ArrayList<Medication> medications = new ArrayList<>();

        for (Medication medication : mModel.getMedications()) {
            if (medication.getConsumptionDates() != null && medication.getConsumptionDates().size() > 0) {
                for (Date date : medication.getConsumptionDates()) {
                    if (date.getYear() == event.getStartTime().get(Calendar.YEAR)
                            && date.getMonth() == event.getStartTime().get(Calendar.MONTH)
                            && date.getDay() == event.getStartTime().get(Calendar.DAY_OF_MONTH)
                            && date.getHour() == event.getStartTime().get(Calendar.HOUR_OF_DAY)) {

                        medications.add(medication);
                    }
                }
            }
        }

        Intent intent = new Intent(getActivity(), DateMedicationsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MEDICATIONS_KEY, medications);
        bundle.putString(DATE_KEY, event.getStartTime().get(Calendar.DAY_OF_MONTH) + "/" + ((event.getStartTime().get(Calendar.MONTH) + 1) % 12) + "/" + event.getStartTime().get(Calendar.YEAR));
        intent.putExtras(bundle);
        startActivity(intent);
    };

    WeekView.EventLongPressListener mEventLongPressListener = (event, eventRect) -> {

    };

    public List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        HashMap<String, Integer> eventsPerDay = new LinkedHashMap<>();

        for (Medication medication : mModel.getMedications()) {
            if (medication.getConsumptionDates() != null) {
                for (Date date : medication.getConsumptionDates()) {
                    if (newYear == date.getYear() && newMonth == date.getMonth()) {
                        if (eventsPerDay.get(date.getDay() + " " + date.getHour() + " " + date.getMinute()) == null) {
                            eventsPerDay.put(date.getDay() + " " + date.getHour() + " " + date.getMinute(), 0);
                        }
                        int eventTimes = eventsPerDay.get(date.getDay() + " " + date.getHour() + " " + date.getMinute()) + 1;
                        eventsPerDay.put(date.getDay() + " " + date.getHour() + " " + date.getMinute(), eventTimes);
                    }
                }
            }
        }

        for (String key : eventsPerDay.keySet()) {
            String[] date = key.split(" ");
            events.add(addNewEvent(newYear, newMonth, Integer.parseInt(date[0]),
                    Integer.parseInt(date[1]), Integer.parseInt(date[2]), eventsPerDay.get(key), 0));

            Log.i("EVENTS", key + ":" + eventsPerDay.get(key));
        }

        return events;
    }

    private WeekViewEvent addNewEvent(int year, int month, int day, int hour, int minutes, int medNumber, int weeksPlus) {
        // Initialize start and end time.
        String colorCode = "#32CD32";
        boolean isInPast = false;
        boolean isInPresent = false;

        Calendar now = Calendar.getInstance();

        Calendar eventTime = (Calendar) now.clone();
        eventTime.set(Calendar.YEAR, year);
        eventTime.set(Calendar.MONTH, month);
        eventTime.set(Calendar.DAY_OF_MONTH, day);
        eventTime.set(Calendar.HOUR_OF_DAY, hour);
        eventTime.set(Calendar.MINUTE, minutes);
        eventTime.add(Calendar.WEEK_OF_YEAR, weeksPlus);

        Calendar endTime = (Calendar) now.clone();
        endTime.set(Calendar.YEAR, year);
        endTime.set(Calendar.MONTH, month);
        endTime.set(Calendar.DAY_OF_MONTH, day);
        endTime.set(Calendar.HOUR_OF_DAY, hour + 1);
        endTime.set(Calendar.MINUTE, minutes);
        endTime.add(Calendar.WEEK_OF_YEAR, weeksPlus);

        String date = eventTime.get(Calendar.DAY_OF_MONTH) + "/" + (month + 1) % 12 + "/" + year + " " + hour + ":" + minutes;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = dateFormat.format(eventTime.getTime());
        Log.i("FORMATTED", formattedDate);
        try {
            if (dateFormat.parse(dateFormat.format(endTime.getTime())).before(new java.util.Date())) {
                Log.i("DATE", "PAST: " + date);
                isInPast = true;
            }

            if (dateFormat.parse(dateFormat.format(eventTime.getTime())).before(new java.util.Date())
                && dateFormat.parse(dateFormat.format(endTime.getTime())).after(new java.util.Date())) {
                Log.i("DATE", "PRESENT: " + date);
                isInPresent = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (isInPast) {
            colorCode = "#AEB6BF";
        }
        else if (isInPresent) {
            colorCode = "#328ee4";
        }

        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName("Meds x" + medNumber);
        weekViewEvent.setStartTime(eventTime);
        weekViewEvent.setEndTime(endTime);
        weekViewEvent.setColor(Color.parseColor(colorCode));

        return weekViewEvent;
    }

    @Override
    public void update() {
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public void update(boolean add) {

    }
}
