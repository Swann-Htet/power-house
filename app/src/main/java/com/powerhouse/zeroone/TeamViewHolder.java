package com.powerhouse.zeroone;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeamViewHolder extends RecyclerView.ViewHolder {

    public TextView team1NameTextView;
    public Button voteTeam1Button;
    public TextView team2NameTextView;
    public Button voteTeam2Button;
    public TextView team1VoteCountTextView; // Add this
    public TextView team2VoteCountTextView;
    public ImageView team1ImageView;
    public ImageView team2ImageView;
    public ProgressBar progressBarC;

    public TeamViewHolder(@NonNull View itemView) {
        super(itemView);

        team1NameTextView = itemView.findViewById(R.id.team1NameTextView);
        voteTeam1Button = itemView.findViewById(R.id.voteTeam1Button);
        team2NameTextView = itemView.findViewById(R.id.team2NameTextView);
        voteTeam2Button = itemView.findViewById(R.id.voteTeam2Button);
        team1VoteCountTextView = itemView.findViewById(R.id.team1VoteCountTextView);
        team2VoteCountTextView = itemView.findViewById(R.id.team2VoteCountTextView);
        team1ImageView = itemView.findViewById(R.id.team1ImageView);
        team2ImageView = itemView.findViewById(R.id.team2ImageView);
        progressBarC = itemView.findViewById(R.id.progressBar);
    }
}
