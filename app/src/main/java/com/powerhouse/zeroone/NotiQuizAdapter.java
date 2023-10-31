package com.powerhouse.zeroone;


import static com.google.firebase.auth.FirebaseAuth.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotiQuizAdapter extends RecyclerView.Adapter<NotiQuizAdapter.ViewHolder> {
    private List<QuizWinners> quizWinnersList;

    public NotiQuizAdapter(List<QuizWinners> quizWinnersList) {
        this.quizWinnersList = quizWinnersList;
    }

    // Implement the ViewHolder class and methods here

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data from the winningInfoList to the ViewHolder views
        QuizWinners quizWinners = quizWinnersList.get(position);

        holder.userName.setText("");
        holder.message.setText(quizWinners.getMessage());
        holder.quizNoti.setText(quizWinners.getQuizID());

        if (quizWinners.getRead().equals("unread")) {
            holder.readnread.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(true);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemView) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Claim your reward");
                    builder.setMessage("\n"+quizWinners.getMessage());
                    builder.setIcon(R.drawable.logophspinner);

                    LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                    View customLayout = inflater.inflate(R.layout.custom_submit_info, null);

                    EditText etMobileNumber = customLayout.findViewById(R.id.etQMobileNumber);
                    EditText etName = customLayout.findViewById(R.id.edQName);

                    builder.setView(customLayout); // Set the custom layout for the dialog

                    builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String mobileNumber = etMobileNumber.getText().toString();
                            String name = etName.getText().toString();

                            FirebaseUser auth = getInstance().getCurrentUser();
                            String userID = auth.getUid();
                            String userId = getInstance().getCurrentUser().getUid();

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

                            DatabaseReference winningInfoRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                                    .getReference("QuizWinners");

                            Query query = winningInfoRef.orderByChild("userID").equalTo(userId);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        QuizWinners winningInfo = snapshot.getValue(QuizWinners.class);
                                        String quizIDB = holder.quizNoti.getText().toString();

                                        if (winningInfo != null && winningInfo.getUserID().equals(userId) && quizIDB.equals(winningInfo.getQuizID())) {

                                            snapshot.getRef().child("read").setValue("read");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle error
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

        } else {
            holder.readnread.setVisibility(View.GONE);
            holder.itemView.setBackgroundResource(R.drawable.read_card);
            holder.itemView.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return quizWinnersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView message;
        public ImageView readnread;
        public TextView quizNoti;

        public ViewHolder(View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.userNameQWinner);
            message = itemView.findViewById(R.id.messageQWinner);
            readnread = itemView.findViewById(R.id.imageViewReadIndicator);
            quizNoti = itemView.findViewById(R.id.quizmatchID);
        }
    }
}


