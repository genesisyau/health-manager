package com.example.android.mydrugjournal.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.data.Date;
import com.example.android.mydrugjournal.interfaces.Observer;
import com.example.android.mydrugjournal.models.MedicationModel;
import com.example.android.mydrugjournal.receivers.AlertReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNewMedicationActivity extends AppCompatActivity implements Observer {
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private Toolbar mToolbar;
    private Spinner mSpinnerRoutes;
    private Button mButtonSave;
    private Button mButtonAddToCalendar;
    private TextInputEditText mEditTextName;
    private TextInputEditText mEditTextDescription;

    private NotificationManager mNotificationManager;
    private AlarmManager alarmManager;
    private PendingIntent notifyPendingIntent;

    private MedicationModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medication);

        mModel = MedicationModel.getInstance();
        mModel.register(this);

        //Setup toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent notifyIntent = new Intent(this, AlertReceiver.class);

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getSystemService
                (ALARM_SERVICE);

        //Setup spinner
        mSpinnerRoutes = findViewById(R.id.spinnner_administration_routes);
        mSpinnerRoutes.setOnItemSelectedListener(onAdministrationRouteSelected);
        mSpinnerRoutes.setSelection(0, true);

        //SetupViews
        mEditTextName = findViewById(R.id.edit_text_med_name);
        mEditTextDescription = findViewById(R.id.edit_text_med_description);

        //Setup Button
        mButtonSave = findViewById(R.id.button_save_medication);
        mButtonSave.setOnClickListener(onButtonSaveClick);

        mButtonAddToCalendar = findViewById(R.id.button_add_to_calendar);
        mButtonAddToCalendar.setOnClickListener(onButtonAddToCalendarClick);

        createNotificationChannel();
    }

    //************************//
    //*******Listeners********//
    //************************//
    private View.OnClickListener onButtonSaveClick = view -> {
//        AddToCalendarDialog dialog = new AddToCalendarDialog();
//        dialog.show(getSupportFragmentManager(), "confirmation dialog");
        String medName = mEditTextName.getText().toString();
        String medDesc = mEditTextDescription.getText().toString();
        String medAdmin = mSpinnerRoutes.getSelectedItem().toString();
//
        mModel.setMedicationInfo(medName, medDesc, medAdmin);
        mModel.addNewMedication();

        finish();
    };

    private View.OnClickListener onButtonAddToCalendarClick = view -> {
        Intent intent = new Intent(this, AddToCalendarActivity.class);
        startActivity(intent);
    };

    private AdapterView.OnItemSelectedListener onAdministrationRouteSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    //************************//
    //***Activity Functions***//
    //************************//
    public void createNotificationChannel() {
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("I am a notification");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(boolean add) {
        ArrayList<Date> consumpDate = mModel.getMedications().get(mModel.getMedications().size() - 1).getConsumptionDates();
        if (consumpDate != null && consumpDate.size() > 0) {
            for (Date date : consumpDate) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.YEAR, date.getYear());
                cal.set(Calendar.MONTH, date.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, date.getDay());
                cal.set(Calendar.HOUR_OF_DAY, date.getHour());
                cal.set(Calendar.MINUTE, date.getMinute());
                cal.set(Calendar.SECOND, 0);
                long triggerTime = cal.getTimeInMillis();
                Log.i("TRIGGER", Long.toString(triggerTime));
                if (alarmManager != null) {
                    alarmManager.setExact
                            (AlarmManager.RTC_WAKEUP,
                                    triggerTime,
                                    notifyPendingIntent);
                }
            }
        }
    }
}
