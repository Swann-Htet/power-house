package com.powerhouse.zeroone;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MissionMainActivity extends AppCompatActivity {

    TextView textViewTotalPoint;
    Button rewardBtn, interBtn;
    int totalPoint = 0;
    RewardedAd rewardedAd;
    GifImageView gifImageView;
    LottieAnimationView lottieAnimationView;
    LottieAnimationView lottieAnimationView2;
    TextView textNoAvalible;
    private AdView mAdView, mAdView02;
    private InterstitialAd mInterstitialAd;

    ProgressBar progressBarReward;
    int milestone1 = 50;
    int milestone2 = 100;

    private List<QuizQuestion> quizQuestionsList = new ArrayList<>();
    UserQuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_main);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.FULL_BANNER);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.adViewMission);
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
                Toast.makeText(MissionMainActivity.this, adError.getMessage(), Toast.LENGTH_SHORT).show();
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

        mAdView02 = findViewById(R.id.adViewMission02);
        AdRequest adRequest02 = new AdRequest.Builder().build();
        mAdView02.loadAd(adRequest02);

        mAdView02.setAdListener(new AdListener() {
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
                Toast.makeText(MissionMainActivity.this, adError.getMessage(), Toast.LENGTH_SHORT).show();
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

        InterstitialAd.load(this,"ca-app-pub-4743806105497141/3520724481", adRequestBanner,
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
                        Toast.makeText(MissionMainActivity.this, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MissionMainActivity.this);
                }
            }
        },2000);

        TextView backTomain = findViewById(R.id.backTomain);
        backTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MissionMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewTotalPoint = findViewById(R.id.textViewTotalPoint);
        rewardBtn = findViewById(R.id.btnRewardAD);
        gifImageView = findViewById(R.id.unavaliableGift);
        lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView2 = findViewById(R.id.animationView2);
        textNoAvalible = findViewById(R.id.textNoAvalible);

        RecyclerView recyclerView = findViewById(R.id.quizRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserQuizAdapter(this, quizQuestionsList);
        recyclerView.setAdapter(adapter);

        fetchDataFromFirebase();

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MissionMainActivity.this);
                builder.setTitle("Congratulations");
                builder.setMessage("You reached 100 Points!\nPlease fill your mobile number and name to get your reward.");
                builder.setIcon(R.drawable.logoph);

                View customLayout = getLayoutInflater().inflate(R.layout.custom_input_dialog, null);
                builder.setView(customLayout);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Retrieve user input from EditText fields
                        EditText etMobileNumber = customLayout.findViewById(R.id.etMobileNumber);
                        EditText etName = customLayout.findViewById(R.id.etName);

                        String mobileNumber = etMobileNumber.getText().toString();
                        String name = etName.getText().toString();

                        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = auth.getUid();

                        String rewardAccountKey = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Reward Accounts")
                                .push()
                                .getKey();

                        DatabaseReference userRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Reward Accounts")
                                .child(rewardAccountKey);
                        DatabaseReference userDetailsRef = userRef.child("details");

                        UserDetails userDetails = new UserDetails(mobileNumber, name, userID, rewardAccountKey);
                        userDetailsRef.setValue(userDetails);


                        DatabaseReference userPointsRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("AD Point")
                                .child("Users")
                                .child(userID) // Use the UID as the child node to target the specific user
                                .child("points");

                        userPointsRef.setValue(0);
                        progressBarReward.setProgress(0);

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        progressBarReward = findViewById(R.id.progressBarReward);

        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        String userID = auth.getUid();

        DatabaseReference userPointsRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("AD Point")
                .child("Users")
                .child(userID)
                .child("points");

        if (userPointsRef != null) {
            userPointsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Long points = dataSnapshot.getValue(Long.class);

                        if (points != null) {

                            totalPoint = points.intValue();
                            int maximumValue = milestone2; // Adjust this based on your desired range
                            int initialProgress = (int) (((float) totalPoint / maximumValue) * 100);
                            progressBarReward.setProgress(initialProgress);
                            textViewTotalPoint.setText("Point: " + totalPoint);

                            if (totalPoint == milestone2) {
                                lottieAnimationView.setVisibility(View.VISIBLE);
                                gifImageView.setVisibility(View.GONE);
                            } else {
                                lottieAnimationView.setVisibility(View.GONE);
                                gifImageView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Database Error: " + databaseError.getMessage());
                }
            });
        }

        MobileAds.initialize(this);

        rewardedAD();

        rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardedAd();
            }
        });
    }

    private void fetchDataFromFirebase() {
        DatabaseReference quizQuestionsRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("QuizQuestions");

        quizQuestionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizQuestionsList.clear();

                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = questionSnapshot.child("imageUrl").getValue(String.class);
                    String option1 = questionSnapshot.child("option1").getValue(String.class);
                    String option2 = questionSnapshot.child("option2").getValue(String.class);
                    String option3 = questionSnapshot.child("option3").getValue(String.class);
                    String questionText = questionSnapshot.child("questionText").getValue(String.class);
                    String quizId = questionSnapshot.child("quizId").getValue(String.class);

                    QuizQuestion quizQuestion = new QuizQuestion(quizId, questionText, option1, option2, option3, imageUrl);

                    quizQuestionsList.add(quizQuestion);
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();

                showAnimationIfAdapterEmpty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void rewardedAD() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-4743806105497141/7651541185",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        Toast.makeText(MissionMainActivity.this, loadAdError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                        setFullScreenContentCallbackForRewardedAd();
                    }
                });
    }

    private void setFullScreenContentCallbackForRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    rewardedAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    rewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });
        } else {
            Log.d(TAG, "Rewarded ad is null, cannot set FullScreenContentCallback.");
        }
    }

    private void showRewardedAd() {
        if (rewardedAd != null) {
            Activity activityContext = MissionMainActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                    totalPoint += 1;
                    textViewTotalPoint.setText("Total Point: " + totalPoint);

                    updatePointsInFirebase(totalPoint);
                    updateProgress();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    private void updateProgress() {
        int maximumValue = milestone2;
        int progress = (int) (((float) totalPoint / maximumValue) * 100);

        progressBarReward.setProgress(progress);

        if (totalPoint >= milestone1) {

        }

        if (totalPoint >= milestone2) {

        }
        textViewTotalPoint.setText("Total Point: " + totalPoint);
    }

    private void updatePointsInFirebase(int points) {
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        if (auth != null) {
            String userID = auth.getUid(); // Get the user's unique ID (UID)
            DatabaseReference userPointsRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("AD Point")
                    .child("Users")
                    .child(userID) // Use the UID as the child node to target the specific user
                    .child("points");

            userPointsRef.setValue(points);
            progressBarReward.setProgress(totalPoint);
        } else {
            // Handle the case where the user is not authenticated
            Log.e(TAG, "User is not authenticated.");
        }
    }

    private void showAnimationIfAdapterEmpty() {
        if (adapter.getItemCount() == 0) {
            lottieAnimationView2.setVisibility(View.VISIBLE);
            textNoAvalible.setVisibility(View.VISIBLE);
        } else {
            lottieAnimationView2.setVisibility(View.GONE);
            textNoAvalible.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MissionMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
