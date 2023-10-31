package com.powerhouse.zeroone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotiFragment extends Fragment {

    private Button votingButton;
    private Button quizButton;
    private Button rewardButton;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewQ;

    public NotiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noti, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewVote);
        recyclerViewQ = view.findViewById(R.id.recyclerViewNoti);
        recyclerView.setVisibility(View.VISIBLE);


        votingButton = view.findViewById(R.id.votingButton);
        votingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewQ.setVisibility(View.GONE);
            }
        });

        quizButton = view.findViewById(R.id.quizButton);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewQ.setVisibility(View.VISIBLE);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference winningInfoRef = database.getReference("Winners").child("UserData");

            Query query = winningInfoRef.orderByChild("userID").equalTo(userId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<WinningInfo> winningInfoList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        WinningInfo winningInfo = snapshot.getValue(WinningInfo.class);
                        winningInfoList.add(winningInfo);
                    }

                    Collections.reverse(winningInfoList);

                    NotiViewAdapter adapter = new NotiViewAdapter(winningInfoList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // The user is not logged in
        }


        FirebaseAuth authQ = FirebaseAuth.getInstance();
        FirebaseUser userQ = authQ.getCurrentUser();
        if (user != null) {
            String userId = userQ.getUid();
            FirebaseDatabase databaseQ = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference winningInfoRefQ = databaseQ.getReference("QuizWinners");

            Query query = winningInfoRefQ.orderByChild("userID").equalTo(userId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<QuizWinners> winningInfoListQ = new ArrayList<>();
                    for (DataSnapshot snapshotQ : dataSnapshot.getChildren()) {
                        QuizWinners winningInfo = snapshotQ.getValue(QuizWinners.class);
                        winningInfoListQ.add(winningInfo);
                    }

                    Collections.reverse(winningInfoListQ);

                    NotiQuizAdapter adapterQ = new NotiQuizAdapter(winningInfoListQ);
                    recyclerViewQ.setAdapter(adapterQ);
                    recyclerViewQ.setLayoutManager(new LinearLayoutManager(getContext()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // The user is not logged in
        }

        return view;
    }
}
