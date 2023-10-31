package com.powerhouse.zeroone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UpdateProfile extends AppCompatActivity {

    private EditText editTextUpdateName, editTextUpdateDoB, editTextUpdateMobile;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private String textFullName, textDoB, textGender, textMobile;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private Button backBtnUpdate;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        progressBar = findViewById(R.id.progressBarUpdate);
        editTextUpdateName = findViewById(R.id.editTextFullNameUpdate);
        editTextUpdateDoB = findViewById(R.id.editTextDobUpdate);
        editTextUpdateMobile = findViewById(R.id.editTextMobileUpdate);

        radioGroupUpdateGender = findViewById(R.id.radioGroupGenUpdate);

        backBtnUpdate = findViewById(R.id.backBtnUpdate);
        backBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.FULL_BANNER);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.adViewPoll);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Toast.makeText(UpdateProfile.this, adError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Ad Error", adError.getMessage());
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.d("AdLoad", "Ad loaded successfully");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });

        MobileAds.initialize(this);
        AdRequest adRequestBanner = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-4743806105497141/3523328563", adRequestBanner,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Toast.makeText(UpdateProfile.this, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(UpdateProfile.this);
                }
            }
        },2000);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

        editTextUpdateDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textADoB[] = textDoB.split("/");

                int day = Integer.parseInt(textADoB[0]);
                int month = Integer.parseInt(textADoB[1]) - 1;
                int year = Integer.parseInt(textADoB[2]);

                DatePickerDialog picker;

                picker = new DatePickerDialog(UpdateProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofMonth) {
                        editTextUpdateDoB.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonUpdateProfile = findViewById(R.id.buttonUpdate);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenID = radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected = findViewById(selectedGenID);

        if (TextUtils.isEmpty(textFullName)){
            Toast.makeText(UpdateProfile.this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            editTextUpdateName.setError("Your name is required!");
            editTextUpdateName.requestFocus();
        } else if (TextUtils.isEmpty(textDoB)){
            Toast.makeText(UpdateProfile.this, "Please select your birthday!", Toast.LENGTH_SHORT).show();
            editTextUpdateDoB.setError("Your birthday is required!");
            editTextUpdateDoB.requestFocus();
        } else if (TextUtils.isEmpty(textMobile)){
            Toast.makeText(UpdateProfile.this, "Please enter your phone number!", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Your phone number is required!");
            editTextUpdateMobile.requestFocus();
        } else if (textMobile.length() < 10){
            Toast.makeText(UpdateProfile.this, "Please re-enter your phone number!", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Re-enter your phone number!");
            editTextUpdateMobile.requestFocus();
        } else if (TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())) {
            Toast.makeText(UpdateProfile.this, "Please select your gender!", Toast.LENGTH_SHORT).show();
        } else {
            textGender = radioButtonUpdateGenderSelected.getText().toString();
            textFullName = editTextUpdateName.getText().toString();
            textDoB = editTextUpdateDoB.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();

            ReadWriteDetails writeDetails = new ReadWriteDetails(textDoB, textGender, textMobile);

            DatabaseReference reference = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Registered Users");
            String userID = firebaseUser.getUid();

            progressBar.setVisibility(View.VISIBLE);

            reference.child(userID).setValue(writeDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileChangeRequest);

                        Toast.makeText(UpdateProfile.this, "Successfully update your details", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(UpdateProfile.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(UpdateProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIDRegistered = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Registered Users");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIDRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteDetails readWriteDetails = snapshot.getValue(ReadWriteDetails.class);
                if (readWriteDetails != null){
                    textFullName = firebaseUser.getDisplayName();
                    textDoB = readWriteDetails.doB;
                    textGender = readWriteDetails.gender;
                    textMobile = readWriteDetails.mobile;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateDoB.setText(textDoB);
                    editTextUpdateMobile.setText(textMobile);

                    if (textGender.equals("Male")){
                        radioButtonUpdateGenderSelected = findViewById(R.id.radioMale);
                    } else {
                        radioButtonUpdateGenderSelected = findViewById(R.id.radioFemale);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);
                } else {
                    Toast.makeText(UpdateProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
