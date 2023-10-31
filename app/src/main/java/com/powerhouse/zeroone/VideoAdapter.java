package com.powerhouse.zeroone;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoModel> videoList;

    public VideoAdapter(List<VideoModel> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoModel video = videoList.get(position);

        Uri videoUri = Uri.parse(video.getVideoUrl());
        holder.videoView.setVideoURI(videoUri);

        holder.titleTextView.setText(video.getTitle());
        holder.descriptionTextView.setText(video.getDescription());

        holder.userName.setText(video.getName());
        holder.dateVD.setText(video.getCurrentTime());
        holder.timeVD.setText(video.getCurrentDate());

        Picasso.get().load(video.getPhotoUrl()).into(holder.imageViewUser);

        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.videoView.pause();
                int duration = mp.getDuration();
                holder.seekBar.setMax(duration);
                holder.totalDurationTextView.setText(holder.formatTime(duration));
                holder.loader.setVisibility(View.GONE);

                holder.timer = new Timer();
                holder.timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                int currentPosition = holder.videoView.getCurrentPosition();
                                holder.seekBar.setProgress(currentPosition);
                                holder.currentTimeTextView.setText(holder.formatTime(currentPosition));
                            }
                        });
                    }
                }, 0, 1000); // Update every second
            }
        });

        holder.speedUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = holder.videoView.getCurrentPosition();
                holder.videoView.seekTo(currentPos + 10000); // Add 10 seconds
            }
        });

        holder.speedDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = holder.videoView.getCurrentPosition();
                holder.videoView.seekTo(currentPos - 10000); // Subtract 10 seconds
            }
        });

        holder.fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(v.getContext(), FullScreenVideoActivity.class);
                fullScreenIntent.putExtra("videoUrl", video.getVideoUrl());
                v.getContext().startActivity(fullScreenIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public VideoView videoView;
        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageButton playPauseButton;
        public SeekBar seekBar;
        public TextView currentTimeTextView;
        public TextView totalDurationTextView;
        public Timer timer;
        public ProgressBar loader;
        public ImageButton speedUpButton;
        public ImageButton speedDownButton;
        public ImageButton fullScreenButton;
        public TextView userName;
        public TextView timeVD;
        public TextView dateVD;
        public ShapeableImageView imageViewUser;

        public ViewHolder(View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.uploadedVideos);
            titleTextView = itemView.findViewById(R.id.videoTitle);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            playPauseButton = itemView.findViewById(R.id.playPauseButton);
            seekBar = itemView.findViewById(R.id.seekBar);
            currentTimeTextView = itemView.findViewById(R.id.currentTimeTextView);
            totalDurationTextView = itemView.findViewById(R.id.totalDurationTextView);
            loader = itemView.findViewById(R.id.loader);
            speedUpButton = itemView.findViewById(R.id.speedUpButton);
            speedDownButton = itemView.findViewById(R.id.speedDownButton);
            fullScreenButton = itemView.findViewById(R.id.fullScreenButton);

            userName = itemView.findViewById(R.id.nameText);
            timeVD = itemView.findViewById(R.id.videoUDate);
            dateVD = itemView.findViewById(R.id.videoUTime);
            imageViewUser = itemView.findViewById(R.id.imageView_Profile);

            playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    togglePlayPause();
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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

        public void togglePlayPause() {
            if (videoView.isPlaying()) {
                videoView.pause();
                playPauseButton.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_play);
            } else {
                videoView.start();
                playPauseButton.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_pause);
            }
        }

        public String formatTime(int milliseconds) {
            int seconds = (milliseconds / 1000) % 60;
            int minutes = (milliseconds / (1000 * 60)) % 60;
            int hours = (milliseconds / (1000 * 60 * 60)) % 24;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }
}
