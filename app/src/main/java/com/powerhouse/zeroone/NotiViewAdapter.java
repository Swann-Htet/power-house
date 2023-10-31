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

public class NotiViewAdapter extends RecyclerView.Adapter<NotiViewAdapter.ViewHolder> {
    private List<WinningInfo> winningInfoList;

    public NotiViewAdapter(List<WinningInfo> winningInfoList) {
        this.winningInfoList = winningInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data from the winningInfoList to the ViewHolder views
        WinningInfo winningInfo = winningInfoList.get(position);

        holder.userName.setText(winningInfo.getUserName());
        holder.message.setText(winningInfo.getMessage());
        holder.matchIDNoti.setText(winningInfo.getMatchID());

        if (winningInfo.getRead().equals("unread")) {
            holder.readnread.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(true);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemView) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Claim your reward");
                    builder.setMessage("\n"+winningInfo.getMessage());
                    builder.setIcon(R.drawable.logophspinner);

                    LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                    View customLayout = inflater.inflate(R.layout.custom_submit_info, null);

                    EditText etMobileNumber = customLayout.findViewById(R.id.etQMobileNumber);
                    EditText etName = customLayout.findViewById(R.id.edQName);

                    builder.setView(customLayout);

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
                                    .getReference("Winners").child("UserData");

                            Query query = winningInfoRef.orderByChild("userID").equalTo(userId);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        WinningInfo winningInfo = snapshot.getValue(WinningInfo.class);
                                        String matchIDNotiF = holder.matchIDNoti.getText().toString();

                                        if (winningInfo != null && winningInfo.getUserID().equals(userId) && matchIDNotiF.equals(winningInfo.getMatchID())) {
                                            // Update the 'read' field to "read"
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
        return winningInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView message;
        public ImageView readnread;
        public TextView matchIDNoti;

        public ViewHolder(View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.userNameWinner);
            message = itemView.findViewById(R.id.messageWinner);
            readnread = itemView.findViewById(R.id.imageViewReadIndicator);
            matchIDNoti = itemView.findViewById(R.id.matchID);
        }
    }
}

