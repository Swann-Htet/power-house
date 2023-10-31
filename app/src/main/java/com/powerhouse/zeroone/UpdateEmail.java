package com.powerhouse.zeroone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textViewAuthenticated;
    private String userOldEmail, userNewEmail, userPwd;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail, editTextPwd;
    private AdView mAdView;
    private LottieAnimationView animationViewPoll;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        progressBar = findViewById(R.id.progressBarUpdateEmail);
        editTextPwd = findViewById(R.id.editTextEmailVerifyPassword);
        editTextNewEmail = findViewById(R.id.editTextEmailNew);
        textViewAuthenticated = findViewById(R.id.textViewEmailAuthenticated);
        buttonUpdateEmail = findViewById(R.id.buttonUpdateEmail);

        buttonUpdateEmail.setClickable(false);
        buttonUpdateEmail.setBackgroundResource(R.drawable.disable);

        editTextNewEmail.setEnabled(false);
        editTextNewEmail.setBackgroundResource(R.drawable.disable);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.FULL_BANNER);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.adViewEmailUp);
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
                Toast.makeText(UpdateEmail.this, adError.getMessage(), Toast.LENGTH_SHORT).show();
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

        InterstitialAd.load(this,"ca-app-pub-4743806105497141/3793995143", adRequestBanner,
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
                        Toast.makeText(UpdateEmail.this, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(UpdateEmail.this);
                }
            }
        },2000);


        userOldEmail = firebaseUser.getEmail();
        TextView textViewOld = findViewById(R.id.textViewUpdateEmailOld);
        textViewOld.setText(userOldEmail);

        Button backBtnUpdateEmail = findViewById(R.id.backBtnUpdateEmail);
        backBtnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateEmail.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        if (firebaseUser.equals("")){
            Toast.makeText(UpdateEmail.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        } else {
            reAuthenticated(firebaseUser);
        }
    }

    private void reAuthenticated(FirebaseUser firebaseUser) {
        Button buttonVerifyUser = findViewById(R.id.buttonAuthenticateUser);
        buttonVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = editTextPwd.getText().toString();

                if (TextUtils.isEmpty(userPwd)){
                    Toast.makeText(UpdateEmail.this, "Password is needed to continue!", Toast.LENGTH_SHORT).show();
                    editTextPwd.setError("Please enter your password");
                    editTextPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if  (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(UpdateEmail.this, "Password has been verified. You can update your email address.", Toast.LENGTH_SHORT).show();

                                textViewAuthenticated.setText("You are authenticated. You can update your email address.");

                                editTextNewEmail.setEnabled(true);
                                editTextNewEmail.setBackgroundResource(R.drawable.canedit);

                                int whiteColor = getResources().getColor(R.color.white);
                                editTextPwd.setFocusable(false);
                                editTextPwd.setClickable(false);
                                editTextPwd.setBackgroundResource(R.drawable.disable);
                                editTextPwd.setTextColor(whiteColor);

                                buttonVerifyUser.setClickable(false);
                                buttonVerifyUser.setBackgroundResource(R.drawable.disable);

                                buttonUpdateEmail.setClickable(true);
                                buttonUpdateEmail.setBackgroundResource(R.drawable.custom_button);

                                buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = editTextNewEmail.getText().toString();
                                        if (TextUtils.isEmpty(userNewEmail)){
                                            Toast.makeText(UpdateEmail.this, "New email is required!", Toast.LENGTH_SHORT).show();
                                            editTextNewEmail.setError("Enter your new email address!");
                                            editTextNewEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()){
                                            Toast.makeText(UpdateEmail.this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
                                            editTextNewEmail.setError("Please enter valid email address!");
                                            editTextNewEmail.requestFocus();
                                        } else if (userOldEmail.matches(userNewEmail)){
                                            Toast.makeText(UpdateEmail.this, "Please enter new email address!", Toast.LENGTH_SHORT).show();
                                            editTextNewEmail.setError("Please enter new email address!");
                                            editTextNewEmail.requestFocus();
                                        } else {
                                            progressBar.setVisibility(View.VISIBLE);
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    firebaseUser.sendEmailVerification();

                    showAlertDialog();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEmail.this);
        builder.setTitle("Successfully change your email");
        builder.setMessage("Please verify your new email address!");
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
