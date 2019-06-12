package com.example.android.mydrugjournal;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    EditText mEmail, mPassword1, mPassword2;
    Button mSignupBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.emailInput);
        mPassword1 = findViewById(R.id.passwordInput);
        mPassword2 = findViewById(R.id.confirmPasswordInput);
        mSignupBtn = findViewById(R.id.signUpBtn);

        mSignupBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SignupActivity.this.finish();
    }

    private void attemptSignUp() {
        String emailText = mEmail.getText().toString().trim();
        String password1Text = mPassword1.getText().toString().trim();
        String password2Text = mPassword2.getText().toString().trim();

        if(!isCorrectForm(emailText, password1Text, password2Text)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText, password1Text)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignupActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isCorrectForm(String email, String password1, String password2) {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password1.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password1.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password length must be greater than six", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
