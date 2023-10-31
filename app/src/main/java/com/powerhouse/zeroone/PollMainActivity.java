package com.powerhouse.zeroone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PollMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<Match> matchList;
    private AdView mAdView;
    private LottieAnimationView animationViewPoll;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_main);

        animationViewPoll = findViewById(R.id.animationViewpoll);
        TextView textViewNot = findViewById(R.id.notavaText);

        TextView btnBackPoll = findViewById(R.id.backTomain);
        btnBackPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PollMainActivity.this, MainActivity.class);
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
                Toast.makeText(PollMainActivity.this, adError.getMessage(), Toast.LENGTH_SHORT).show();
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

        InterstitialAd.load(this,"ca-app-pub-4743806105497141/4103727155", adRequestBanner,
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
                        Toast.makeText(PollMainActivity.this, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(PollMainActivity.this);
                }
            }
        },2000);

        recyclerView = findViewById(R.id.recyclerViewTeamVote);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        matchList = fetchMatchData(); // Implement this method to retrieve data

        adapter = new MatchAdapter(matchList);
        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (adapter.getItemCount() == 0) {
                    animationViewPoll.setVisibility(View.VISIBLE);
                    textViewNot.setVisibility(View.VISIBLE);
                } else {
                    animationViewPoll.setVisibility(View.GONE);
                    textViewNot.setVisibility(View.GONE);
                }
            }
        });
    }

    private List<Match> fetchMatchData() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Matches");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("DataChange", "Data changed event triggered.");

                matchList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Match match = snapshot.getValue(Match.class);

                    int beforeVoteTeamOne = match.getTeam1VoteCount();
                    int beforeVoteTeamTwo = match.getTeam2VoteCount();
                    int afterVoteTeamOne = beforeVoteTeamOne;
                    int afterVoteTeamTwo = beforeVoteTeamTwo;

                    for (DataSnapshot voteSnapshot : snapshot.child("Votes").getChildren()) {
                        String teamSelected = voteSnapshot.child("teamSelected").getValue(String.class);

                        // Log vote data and trimmed team names
                        Log.d("VoteData", "TeamSelected: " + teamSelected);

                        if (teamSelected != null) {
                            if (teamSelected.equals(match.getTeam1())) {
                                afterVoteTeamOne++;
                            } else if (teamSelected.equals(match.getTeam2())) {
                                afterVoteTeamTwo++;
                            }
                        }
                    }

                    // Log the calculated counts
                    Log.d("VoteCounts", "Team 1 Count: " + afterVoteTeamOne);
                    Log.d("VoteCounts", "Team 2 Count: " + afterVoteTeamTwo);

                    match.setTeam1VoteCount(afterVoteTeamOne);
                    match.setTeam2VoteCount(afterVoteTeamTwo);

                    matchList.add(match);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("DatabaseError", "Database error: " + databaseError.getMessage());
            }
        });

        return new ArrayList<>();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PollMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}