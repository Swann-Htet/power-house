package com.powerhouse.zeroone;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText editTextFullnameReg, editTextEmailReg, editTextDobReg, editTextMobileReg, editTextPassReg, editTextCPassReg;
    private ProgressBar progressBar;
    private RadioGroup radioGroupReg;
    private RadioButton radioButtonGenReg;
    private DatePickerDialog picker;
    private static final String TAG = "Register";
    FirebaseAuth authProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authProfile = FirebaseAuth.getInstance();


        progressBar = findViewById(R.id.progressBarReg);
        editTextFullnameReg = findViewById(R.id.editTextFullNameReg);
        editTextEmailReg = findViewById(R.id.editTextEmailReg);
        editTextMobileReg = findViewById(R.id.editTextMobileReg);
        editTextDobReg = findViewById(R.id.editTextDobReg);
        editTextPassReg = findViewById(R.id.editTextPasswordReg);
        editTextCPassReg = findViewById(R.id.editTextCPasswordReg);
        TextView textLogin = findViewById(R.id.textLogin);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(Register.this, LoginActivity.class);
                startActivity(toLogin);
                finish();
            }
        });

        radioGroupReg = findViewById(R.id.radioGroupGenReg);
        radioGroupReg.clearCheck();

        editTextDobReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofMonth) {
                        editTextDobReg.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonReg = findViewById(R.id.buttonReg);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenID = radioGroupReg.getCheckedRadioButtonId();
                radioButtonGenReg = findViewById(selectedGenID);

                String textFullName = editTextFullnameReg.getText().toString();
                String textEmail = editTextEmailReg.getText().toString();
                String textDob = editTextDobReg.getText().toString();
                String textMobile = editTextMobileReg.getText().toString();
                String textPass = editTextPassReg.getText().toString();
                String textCPass = editTextCPassReg.getText().toString();
                String textGender;

                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(Register.this, "Please enter your name!", Toast.LENGTH_SHORT).show();
                    editTextFullnameReg.setError("Your name is required!");
                    editTextFullnameReg.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Register.this, "Please enter your email address!", Toast.LENGTH_SHORT).show();
                    editTextEmailReg.setError("Your email is required!");
                    editTextEmailReg.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(Register.this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
                    editTextEmailReg.setError("Invalid email address!");
                    editTextEmailReg.requestFocus();
                } else if (TextUtils.isEmpty(textDob)){
                    Toast.makeText(Register.this, "Please select your birthday!", Toast.LENGTH_SHORT).show();
                    editTextDobReg.setError("Your birthday is required!");
                    editTextDobReg.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)){
                    Toast.makeText(Register.this, "Please enter your phone number!", Toast.LENGTH_SHORT).show();
                    editTextMobileReg.setError("Your phone number is required!");
                    editTextMobileReg.requestFocus();
                } else if (textMobile.length() < 10){
                    Toast.makeText(Register.this, "Please re-enter your phone number!", Toast.LENGTH_SHORT).show();
                    editTextMobileReg.setError("Re-enter your phone number!");
                    editTextMobileReg.requestFocus();
                } else if (TextUtils.isEmpty(textPass)){
                    Toast.makeText(Register.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                    editTextPassReg.setError("Password is required!");
                    editTextPassReg.requestFocus();
                } else if (textPass.length() < 8){
                    Toast.makeText(Register.this, "Password must have at least 8 character!", Toast.LENGTH_SHORT).show();
                    editTextPassReg.setError("Password must have at least 8 character!");
                    editTextPassReg.requestFocus();
                } else if (TextUtils.isEmpty(textCPass)){
                    Toast.makeText(Register.this, "Please enter confirm password!", Toast.LENGTH_SHORT).show();
                    editTextCPassReg.setError("Confirm password is required!");
                    editTextCPassReg.requestFocus();
                } else if (!textPass.equals(textCPass)){
                    Toast.makeText(Register.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    editTextCPassReg.setError("Password does not match!");
                    editTextCPassReg.requestFocus();
                } else if (radioGroupReg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Register.this, "Please select your gender!", Toast.LENGTH_SHORT).show();
                } else {
                    textGender = radioButtonGenReg.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDob, textGender, textMobile, textPass);
                }
            }
        });
    }

    private void registerUser(String textFullName, String textEmail, String textDob, String textGender, String textMobile, String textPass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    ReadWriteDetails userDetails = new ReadWriteDetails(textDob, textGender, textMobile);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("Registered Users");
                    databaseReference.child(firebaseUser.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(Register.this, "Registration successful! Please verify your email address.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Database write failed.", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        editTextPassReg.setError("Your password is weak. Kindly use a mix of alphabets, numbers and special characters!");
                        editTextPassReg.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextEmailReg.setError("Your email is invalid or already in use!");
                        editTextEmailReg.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        editTextEmailReg.setError("This email is already register!");
                        editTextEmailReg.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null){
            Intent intenttoMain = new Intent(Register.this, AppOpen.class);
            startActivity(intenttoMain);
            finish();
        }
    }
}
