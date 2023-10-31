package com.powerhouse.zeroone;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class MyViewHolder extends RecyclerView.ViewHolder {

    PlayerView playerView;
    SimpleExoPlayer player;
    ShapeableImageView imageViewUserPhoto;
    TextView textViewTitle, textViewUserName;
    TextView textViewDescriptin;
    TextView textViewuploadDate, textViewuploadTime;
    LottieAnimationView animationViewHome;
    TextView textNoVD;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        playerView = itemView.findViewById(R.id.uploadedVideos);
        imageViewUserPhoto = itemView.findViewById(R.id.userPhoto);
        textViewTitle = itemView.findViewById(R.id.videoTitle);
        textViewUserName = itemView.findViewById(R.id.channelName);
        textViewDescriptin = itemView.findViewById(R.id.videoDescription);
        textViewuploadDate = itemView.findViewById(R.id.currentDate);
        textViewuploadTime = itemView.findViewById(R.id.currentTime);
        animationViewHome = itemView.findViewById(R.id.animationViewHome);
        textNoVD = itemView.findViewById(R.id.textNoVD);
    }


    void prepareExoPlayer(Application application, String videoUrl, String userPhoto, String userName, String videoTitle, String videoDescription, String uploadDate, String uploadTime) {
        try {
            if (userName != null) {
                Picasso.get().load(userPhoto).into(imageViewUserPhoto);
                textViewTitle.setText(videoTitle);
                textViewUserName.setText(userName);
                textViewDescriptin.setText(videoTitle);
                textViewDescriptin.setText(videoDescription);
                textViewuploadDate.setText(uploadDate);
                textViewuploadTime.setText(uploadTime);

                player = new SimpleExoPlayer.Builder(application).build();
                playerView.setPlayer(player);

                Uri videoUri = Uri.parse(videoUrl);
                MediaSource mediaSource = buildMediaSource(videoUri);

                player.setPlayWhenReady(true);
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
            }

        } catch (Exception e) {
            Log.d("ExoPlayer Crashed", e.getMessage());
        }
    }

    private MediaSource buildMediaSource(Uri videoUri) {
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(itemView.getContext(), "YourApplicationName"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
    }

}

