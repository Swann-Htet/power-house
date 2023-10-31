package com.powerhouse.zeroone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassword extends AppCompatActivity {

    private EditText editTextEmailForgot;
    private Button buttonForgot, buttonBack;
    private ProgressBar progressBarForgot;
    private FirebaseAuth authProfile;
    private final static String TAG = "ForgotPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        buttonBack = findViewById(R.id.backBtn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editTextEmailForgot = findViewById(R.id.editTextEmailForgot);
        buttonForgot = findViewById(R.id.buttonForgot);
        progressBarForgot = findViewById(R.id.progressBarForgot);

        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailForgot.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPassword.this, "Please enter registered email address!", Toast.LENGTH_SHORT).show();
                    editTextEmailForgot.setError("Email is required");
                    editTextEmailForgot.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgotPassword.this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
                    editTextEmailForgot.setError("Invalid Email Address");
                    editTextEmailForgot.requestFocus();
                } else {
                    progressBarForgot.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setTitle("Check your email");
                    builder.setMessage("Please check your email to reset your password");
                    builder.setIcon(R.drawable.logoph);
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextEmailForgot.setError("User does not exists!");
                    } catch (Exception e) {
                        Log.e (TAG, e.getMessage());
                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(ForgotPassword.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBarForgot.setVisibility(View.GONE);
            }
        });
    }
}
