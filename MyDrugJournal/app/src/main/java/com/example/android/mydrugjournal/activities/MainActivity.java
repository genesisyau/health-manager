package com.example.android.mydrugjournal.activities;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.example.android.mydrugjournal.fragments.AboutFragment;
import com.example.android.mydrugjournal.fragments.DatePickerFragment;
import com.example.android.mydrugjournal.fragments.EmergencyContactsFragment;
import com.example.android.mydrugjournal.fragments.MyAllergiesFragment;
import com.example.android.mydrugjournal.fragments.MyMedicationsFragment;
import com.example.android.mydrugjournal.fragments.UserProfileFragment;
import com.example.android.mydrugjournal.fragments.WeeklyScheduleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);
        TextView userEmail = hView.findViewById(R.id.userEmail);
        TextView userName = hView.findViewById(R.id.userName);
        ImageView userPic = hView.findViewById(R.id.userProfilePic);
        userEmail.setText(mAuth.getCurrentUser().getEmail() + "!");

        //test();

        CountryCodePicker ccp = findViewById(R.id.countryInput);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Display weekly schedule fragment
        replaceFragment(new WeeklyScheduleFragment());

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setTitle(getResources().getString(R.string.profile_information));
                replaceFragment(new UserProfileFragment());
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile_info:
                toolbar.setTitle(getResources().getString(R.string.profile_information));
                replaceFragment(new UserProfileFragment());
                break;

            case R.id.nav_my_meds:
                toolbar.setTitle(getResources().getString(R.string.my_medications));
                replaceFragment(new MyMedicationsFragment());
                break;

            case R.id.nav_weekly_schedule:
                toolbar.setTitle(getResources().getString(R.string.weekly_schedule));
                replaceFragment(new WeeklyScheduleFragment());
                break;

            case R.id.nav_my_allergies:
                toolbar.setTitle(getResources().getString(R.string.my_allergies));
                replaceFragment(new MyAllergiesFragment());
                break;

            case R.id.nav_about_and_feedback:
                toolbar.setTitle(getResources().getString(R.string.about_and_feedback));
                replaceFragment(new AboutFragment());
                break;

            case R.id.nav_emergency_numbers:
                toolbar.setTitle(getResources().getString(R.string.emergency_numbers));
                replaceFragment(new EmergencyContactsFragment());
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SigninActivity.class));
                this.finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                newFragment).commit();
    }


    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
        EditText mBirthdate = findViewById(R.id.birthdateInput);
        mBirthdate.setText(dateMessage);

        int age = calculateAge(Integer.parseInt(year_string), Integer.parseInt(month_string), Integer.parseInt(day_string));
        EditText mAge = findViewById(R.id.ageInput);
        mAge.setText(Integer.toString(age));

    }

    public static int calculateAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        if (ageInt < 0) {
            ageInt = 0;
        }

        return ageInt;

    }
}

