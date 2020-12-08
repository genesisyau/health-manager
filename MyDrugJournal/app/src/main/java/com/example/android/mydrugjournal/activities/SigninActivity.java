package com.example.android.mydrugjournal.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.mydrugjournal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mSigninBtn, mSignupBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.emailInput);
        mPassword = findViewById(R.id.passwordInput);
        mSigninBtn = findViewById(R.id.signInBtn);
        mSignupBtn = findViewById(R.id.signUpBtn);

        mAuth = FirebaseAuth.getInstance();

        SigninActivity.this.finish();
        startActivity(new Intent(this, MainActivity.class));

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            //open the home page directly if a user is already logged in
            SigninActivity.this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        mSigninBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                attemptSignIn();
            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });

    }

    private void attemptSignIn() {
        String emailText = mEmail.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();

        if(!isCorrectForm(emailText, passwordText)){
            return;
        }

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            SigninActivity.this.finish();
                            startActivity(new Intent(SigninActivity.this, MainActivity.class));
                        }
                        else {
                            Log.w("fail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if the user is signed in and update UI accordingly
        FirebaseUser user = mAuth.getCurrentUser();
        /*if(user != null) {
            startActivity(new Intent(this, LanguageActivity.class));
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        SigninActivity.this.finish();
    }

    private boolean isCorrectForm(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password length must be greater than six", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
