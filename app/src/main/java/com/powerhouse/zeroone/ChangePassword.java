package com.powerhouse.zeroone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private EditText editTextPwDCurrent, editTextPwDNew, editTextPwDNewConfirm;
    private TextView textViewAuthenticated;
    private Button buttonChangePwD, buttonReAuthenticated;
    private ProgressBar progressBar;
    private String userPwDCurrent;
    private Button backChangePass;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextPwDNew = findViewById(R.id.editTextChgPwdNew);
        editTextPwDCurrent = findViewById(R.id.editTextChgPwdCurrent);
        editTextPwDNewConfirm = findViewById(R.id.editTextCChgPwdNew);
        textViewAuthenticated = findViewById(R.id.textViewChgPwdAuthenticated);
        progressBar = findViewById(R.id.progressBarPwDChange);
        buttonReAuthenticated = findViewById(R.id.buttonChgPwdAuthenticateUser);
        buttonChangePwD = findViewById(R.id.buttonUpdatePwd);

        buttonChangePwD.setClickable(false);
        buttonChangePwD.setBackgroundResource(R.drawable.disable);

        editTextPwDNew.setEnabled(false);
        editTextPwDNew.setBackgroundResource(R.drawable.disable);

        editTextPwDNewConfirm.setEnabled(false);
        editTextPwDNewConfirm.setBackgroundResource(R.drawable.disable);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.FULL_BANNER);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.adViewPassUp);
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
                Toast.makeText(ChangePassword.this, adError.getMessage(), Toast.LENGTH_SHORT).show();
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

        InterstitialAd.load(this,"ca-app-pub-4743806105497141/6024287251", adRequestBanner,
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
                        Toast.makeText(ChangePassword.this, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(ChangePassword.this);
                }
            }
        },2000);

        backChangePass = findViewById(R.id.backChangePwD);
        backChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(ChangePassword.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePassword.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            reAuthenticated(firebaseUser);
        }

    }

    private void reAuthenticated(FirebaseUser firebaseUser) {
        buttonReAuthenticated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwDCurrent = editTextPwDCurrent.getText().toString();

                if (TextUtils.isEmpty(userPwDCurrent)){
                    Toast.makeText(ChangePassword.this, "Password is needed to continue!", Toast.LENGTH_SHORT).show();
                    editTextPwDCurrent.setError("Please enter your password");
                    editTextPwDCurrent.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwDCurrent);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(ChangePassword.this, "Password has been verified. You can change your password.", Toast.LENGTH_SHORT).show();

                                textViewAuthenticated.setText("You are authenticated. You can update your password.");

                                editTextPwDNew.setEnabled(true);
                                editTextPwDNew.setBackgroundResource(R.drawable.canedit);

                                editTextPwDNewConfirm.setEnabled(true);
                                editTextPwDNewConfirm.setBackgroundResource(R.drawable.canedit);

                                int whiteColor = getResources().getColor(R.color.white);
                                editTextPwDCurrent.setFocusable(false);
                                editTextPwDCurrent.setClickable(false);
                                editTextPwDCurrent.setBackgroundResource(R.drawable.disable);
                                editTextPwDCurrent.setTextColor(whiteColor);

                                buttonReAuthenticated.setClickable(false);
                                buttonReAuthenticated.setBackgroundResource(R.drawable.disable);

                                buttonChangePwD.setClickable(true);
                                buttonChangePwD.setBackgroundResource(R.drawable.custom_button);

                                buttonChangePwD.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwD(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePwD(FirebaseUser firebaseUser) {
        String userPwDNew = editTextPwDNew.getText().toString();
        String userPwDConfirmNew = editTextPwDNewConfirm.getText().toString();

        if (TextUtils.isEmpty(userPwDNew)) {
            Toast.makeText(ChangePassword.this, "New password is needed!", Toast.LENGTH_SHORT).show();
            editTextPwDNew.setError("Please enter your new password!");
            editTextPwDNew.requestFocus();
        } else if (TextUtils.isEmpty(userPwDConfirmNew)) {
            Toast.makeText(ChangePassword.this, "Confirm password is needed!", Toast.LENGTH_SHORT).show();
            editTextPwDNewConfirm.setError("Please confirm your new password!");
            editTextPwDNewConfirm.requestFocus();
        } else if (!userPwDNew.matches(userPwDConfirmNew)) {
            Toast.makeText(ChangePassword.this, "Password did not match!", Toast.LENGTH_SHORT).show();
            editTextPwDNewConfirm.setError("Please enter same password!");
            editTextPwDNewConfirm.requestFocus();
        } else if (userPwDCurrent.matches(userPwDNew)) {
            Toast.makeText(ChangePassword.this, "New password cannot be same as old password!", Toast.LENGTH_SHORT).show();
            editTextPwDNew.setError("Please enter new password!");
            editTextPwDNew.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(userPwDNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePassword.this, "Successfully change your password!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}