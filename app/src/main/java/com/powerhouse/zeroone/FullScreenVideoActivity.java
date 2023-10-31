package com.powerhouse.zeroone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton playPauseButton;
    private ImageButton rewindButton;
    private ImageButton fastForwardButton;
    private ImageButton exitFullScreenButton;
    private MediaController mediaController;
    private SeekBar seekBar;
    private TextView currentTimeTextView;
    private TextView totalDurationTextView;
    private ProgressBar loader;
    private Handler handler;
    private Runnable runnable;
    private boolean isFullScreen = false;

    private LinearLayout mediaControllerCustom;
    private boolean isMediaControllerVisible = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        videoView = findViewById(R.id.fullScreenVideoView);
        playPauseButton = findViewById(R.id.fullScreenPlayPauseButton);
        rewindButton = findViewById(R.id.fullScreenRewindButton);
        fastForwardButton = findViewById(R.id.fullScreenFastForwardButton);
        exitFullScreenButton = findViewById(R.id.exitFullScreenButton);
        seekBar = findViewById(R.id.fullScreenSeekBar);
        currentTimeTextView = findViewById(R.id.fullScreenCurrentTimeTextView);
        totalDurationTextView = findViewById(R.id.fullScreenTotalDurationTextView);
        loader = findViewById(R.id.fullScreenLoader);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        String videoUrl = getIntent().getStringExtra("videoUrl");

        mediaControllerCustom = findViewById(R.id.mediaControllerCustom);

        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Set OnPreparedListener to handle loader and play
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = mp.getDuration();
                seekBar.setMax(duration);
                totalDurationTextView.setText(formatTime(duration));
                loader.setVisibility(View.GONE);

                // Video is prepared, enable play
                playPauseButton.setEnabled(true);

                startSeekBarUpdate();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMediaControllerVisibility();
            }
        });

        // Disable play button until the video is prepared
        playPauseButton.setEnabled(false);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = videoView.getCurrentPosition();
                videoView.seekTo(currentPos - 10000); // Rewind 10 seconds
            }
        });

        fastForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = videoView.getCurrentPosition();
                videoView.seekTo(currentPos + 10000); // Fast forward 10 seconds
            }
        });

        exitFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoActivity.this, VideoViewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("SeekBar", "Progress: " + progress);
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void startSeekBarUpdate() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    int currentPosition = videoView.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    currentTimeTextView.setText(formatTime(currentPosition));
                }
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void toggleMediaControllerVisibility() {
        if (isMediaControllerVisible) {
            mediaControllerCustom.setVisibility(View.GONE);
            isMediaControllerVisible = false;
        } else {
            mediaControllerCustom.setVisibility(View.VISIBLE);
            isMediaControllerVisible = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void togglePlayPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
            playPauseButton.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_play);
        } else {
            videoView.start();
            playPauseButton.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_pause);
        }
    }

    public String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        int hours = (milliseconds / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FullScreenVideoActivity.this, VideoViewActivity.class);
        startActivity(intent);
        finish();
    }
}
