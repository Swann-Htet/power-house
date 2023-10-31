package com.powerhouse.zeroone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.powerhouse.zeroone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final String AD_UNIT_ID = "ca-app-pub-4743806105497141/5544252590";
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private boolean isShowingAd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(AppOpenAd ad) {
                appOpenAd = ad;
                showAdIfAvailable();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                // Handle the error
            }
        };

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAppOpenAd();
            }
        });

        checkAppVersion();

        binding.bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.poll:
                    replaceFragment(new PollFragment());
                    break;
                case R.id.mission:
                    replaceFragment(new MissionFragment());
                    break;
                case R.id.noti:
                    replaceFragment(new NotiFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;

            }

            return true;
        });
    }

    public void loadAppOpenAd() {
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                this,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
        );
    }

    public void showAdIfAvailable() {
        if (!isShowingAd && appOpenAd != null) {
            isShowingAd = true;
            appOpenAd.show(this);
        } else {
            loadAppOpenAd();
        }
    }

    private void checkAppVersion() {
        DatabaseReference versionRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("AppVersion");

        versionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String version = dataSnapshot.child("version").getValue(String.class);
                    String marketUri = dataSnapshot.child("marketUri").getValue(String.class);

                    if (version != null && !version.equals(getAppVersion())) {
                        showUpdateDialog(marketUri);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("DatabaseError", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private String getAppVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showUpdateDialog(String marketUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logoph);
        builder.setTitle("Update Available");
        builder.setMessage("A new version of the app is available. Please update for the best experience.");
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirect to Play Store for the update
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUri));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);  // Add to back stack if you want to navigate back
        transaction.commit();
    }
}