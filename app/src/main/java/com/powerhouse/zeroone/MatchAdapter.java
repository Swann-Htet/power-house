package com.powerhouse.zeroone;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchAdapter extends RecyclerView.Adapter<TeamViewHolder> {

    private List<Match> matches;

    public MatchAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);

        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.team1NameTextView.setText(match.getTeam1());
        holder.team2NameTextView.setText(match.getTeam2());

        String countTeam1 = String.valueOf(match.getTeam1VoteCount());
        String countTeam2 = String.valueOf(match.getTeam2VoteCount());

        double totalVotesForTeam1 = Double.parseDouble(countTeam1);
        double totalVotesForTeam2 = Double.parseDouble(countTeam2);

        double percentageTeam1;
        double percentageTeam2;

        if (totalVotesForTeam1 == 1 && totalVotesForTeam2 == 1) {
            percentageTeam1 = 50.0;
            percentageTeam2 = 50.0;
        } else {
            percentageTeam1 = (totalVotesForTeam1 / (totalVotesForTeam1 + totalVotesForTeam2)) * 100;
            percentageTeam2 = (totalVotesForTeam2 / (totalVotesForTeam1 + totalVotesForTeam2)) * 100;
        }

        int team1CountFinal = (int)percentageTeam1;
        int team2CountFinal = (int)percentageTeam2;

        holder.team1VoteCountTextView.setText(team1CountFinal + "%");
        holder.team2VoteCountTextView.setText(team2CountFinal + "%");

        if (percentageTeam1 > percentageTeam2) {
            holder.progressBarC.setProgress((int) percentageTeam1);
            holder.progressBarC.setScaleX(1);
        } else if (percentageTeam2 > percentageTeam1) {
            holder.progressBarC.setProgress((int) percentageTeam2);
            holder.progressBarC.setScaleX(-1);
        } else {
            holder.progressBarC.setProgress(0); // If both teams have the same percentage, set progress to halfway
        }


        Picasso.get().load(match.getUrlTeam1()).placeholder(R.drawable.football_default).into(holder.team1ImageView);
        Picasso.get().load(match.getUrlTeam2()).placeholder(R.drawable.football_default).into(holder.team2ImageView);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            if (match.getVotes() != null && match.getVotes().containsKey(userId)) {

                String teamSelected = String.valueOf(match.getVotes().get(userId));
                holder.voteTeam1Button.setEnabled(false);
                holder.voteTeam2Button.setEnabled(false);

                if (teamSelected.equals("team1")) {
                    holder.voteTeam1Button.setText("VOTED");
                } else if (teamSelected.equals("team2")) {
                    holder.voteTeam2Button.setText("VOTED");
                }
            } else {
                // The user has not voted for this match
                holder.voteTeam1Button.setText("VOTE");
                holder.voteTeam1Button.setEnabled(true);
                holder.voteTeam2Button.setText("VOTE");
                holder.voteTeam2Button.setEnabled(true);
            }
        }

        holder.voteTeam1Button.setOnClickListener(view -> {
            int itemPosition = holder.getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                Match selectedMatch = matches.get(itemPosition);
                DatabaseReference matchRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("Matches")
                        .child(selectedMatch.getMatchId());
                handleVoteButtonClick(selectedMatch, matchRef, "team1", holder); // Pass the holder parameter here
            }
        });

        holder.voteTeam2Button.setOnClickListener(view -> {
            int itemPosition = holder.getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                Match selectedMatch = matches.get(itemPosition);
                DatabaseReference matchRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("Matches")
                        .child(selectedMatch.getMatchId());
                handleVoteButtonClick(selectedMatch, matchRef, "team2", holder); // Pass the holder parameter here
            }
        });
    }

    private void handleVoteButtonClick(Match selectedMatch, DatabaseReference matchRef, String selectedTeam, TeamViewHolder holder) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        String matchId = selectedMatch.getMatchId();
        String userName = currentUser.getDisplayName();

        DatabaseReference userVotesRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Matches")
                .child(matchId)
                .child("UserVotes")
                .child(userId)
                .child(matchId);

        userVotesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setIcon(R.drawable.logoph);
                    builder.setTitle("Already Voted");
                    builder.setMessage("You have already voted this match!\nUser can only vote one time.");
                    builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setIcon(R.drawable.logoph);
                    builder.setTitle("Vote Confirm");
                    builder.setMessage("Are you sure you want to vote for " + selectedTeam + "?\nPlease make sure that you can only vote one time!");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int currentTeamVoteCount = (selectedTeam.equals("team1")) ?
                                    selectedMatch.getTeam1VoteCount() + 1 : selectedMatch.getTeam2VoteCount() + 1;

                            // Update the vote counts in the database
                            Map<String, Object> voteUpdates = new HashMap<>();
                            voteUpdates.put(selectedTeam + "VoteCount", currentTeamVoteCount);

                            matchRef.updateChildren(voteUpdates, (databaseError, databaseReference) -> {
                                if (databaseError != null) {
                                    // Handle database write error
                                    Log.e("FirebaseError", "Vote failed: " + databaseError.getMessage());
                                } else {
                                    // Vote added successfully
                                    Log.d("FirebaseSuccess", "Vote added successfully");

                                    // You can update the UI or take any necessary actions here
                                }
                            });

                            String userId = currentUser.getUid();

                            Map<String, Object> userVoteData = new HashMap<>();
                            userVoteData.put("teamSelected", selectedTeam);
                            userVoteData.put("username", userName);
                            userVoteData.put("userID", userId);
                            userVotesRef.setValue(userVoteData);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("DatabaseError", "Database error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }
}
