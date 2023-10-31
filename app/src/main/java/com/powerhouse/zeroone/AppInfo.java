package com.powerhouse.zeroone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.grpc.Context;

public class AppInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        TextView backTomain = findViewById(R.id.backTomain);
        backTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppInfo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView emailDev = findViewById(R.id.emailDev);
        emailDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "shynx.11.wz@gmail.com";

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                startActivity(emailIntent);
            }
        });

        TextView appVersion = findViewById(R.id.appVersion);
        appVersion.setText("Version " + getAppVersion());

        TextView termsofservice = findViewById(R.id.termsofservice);
        termsofservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AppInfo.this);
                alertDialog.setTitle("Terms of Service");
                alertDialog.setIcon(R.drawable.logophspinner);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.termsofservice, null);

                alertDialog.setView(dialogView);

                alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });

        TextView privacypolicy = findViewById(R.id.privacypolicy);
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AppInfo.this);
                alertDialog.setTitle("Privacy & Policy");
                alertDialog.setIcon(R.drawable.logophspinner);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.privacypolicy, null);

                alertDialog.setView(dialogView);

                alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
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
}