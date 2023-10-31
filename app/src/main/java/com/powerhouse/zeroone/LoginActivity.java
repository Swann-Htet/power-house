package com.powerhouse.zeroone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmailLog, editTextPassLog;
    ProgressBar progressBarLog;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textRegister = findViewById(R.id.textRegister);
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReg = new Intent(LoginActivity.this, Register.class);
                startActivity(toReg);
                finish();
            }
        });

        TextView textViewForgotPass = findViewById(R.id.forgotPass);
        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                finish();
            }
        });

        ImageView imageViewShowHide = findViewById(R.id.showhidePassword);
        imageViewShowHide.setImageResource(R.drawable.eyeclose);
        imageViewShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPassLog.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextPassLog.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHide.setImageResource(R.drawable.eyeclose);
                } else {
                    editTextPassLog.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHide.setImageResource(R.drawable.eyeopen);
                }
            }
        });

        editTextEmailLog = findViewById(R.id.editTextEmailLog);
        editTextPassLog = findViewById(R.id.editTextPasswordLog);
        progressBarLog = findViewById(R.id.progressBarLog);

        authProfile = FirebaseAuth.getInstance();

        Button buttonLogin = findViewById(R.id.buttonLog);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = editTextEmailLog.getText().toString();
                String Password = editTextPassLog.getText().toString();

                if (TextUtils.isEmpty(Email)){
                    Toast.makeText(LoginActivity.this, "Please enter your email address!", Toast.LENGTH_SHORT).show();
                    editTextEmailLog.setError("Your email is required!");
                    editTextEmailLog.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(LoginActivity.this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
                    editTextEmailLog.setError("Invalid email address!");
                    editTextEmailLog.requestFocus();
                } else if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                    editTextPassLog.setError("Password is required!");
                    editTextPassLog.requestFocus();
                } else {
                    progressBarLog.setVisibility(View.VISIBLE);
                    loginUser (Email, Password);
                }

            }
        });
    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Successfully Login!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextEmailLog.setError("User does not exists!");
                        editTextEmailLog.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextEmailLog.setError("Invalid User!");
                        editTextEmailLog.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBarLog.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verify");
        builder.setMessage("Please verify your email now!\nYou cannot login without email verification.");
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
    }
}
