package com.powerhouse.zeroone;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserQuizAdapter extends RecyclerView.Adapter<UserQuizAdapter.ViewHolder> {
    private Context context;
    private List<QuizQuestion> quizQuestions;

    public UserQuizAdapter(Context context, List<QuizQuestion> quizQuestions) {
        this.context = context;
        this.quizQuestions = quizQuestions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizQuestion quizQuestion = quizQuestions.get(position);
        holder.bind(quizQuestion);
    }

    @Override
    public int getItemCount() {
        return quizQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView quizPhotoImageView;
        private TextView quizQuestionTextView;
        private Button optionButton1;
        private Button optionButton2;
        private Button optionButton3;
        private Button quizIDText;
        ProgressBar progressBarImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizPhotoImageView = itemView.findViewById(R.id.quizPhotoImageView);
            quizQuestionTextView = itemView.findViewById(R.id.quizQuestionTextView);
            optionButton1 = itemView.findViewById(R.id.optionButton1);
            optionButton2 = itemView.findViewById(R.id.optionButton2);
            optionButton3 = itemView.findViewById(R.id.optionButton3);
            quizIDText = itemView.findViewById(R.id.quizIDText);

            optionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Are you sure?");
                    builder.setIcon(R.drawable.logophspinner);
                    builder.setMessage("Are you certain that you wish to select "+ optionButton1.getText().toString() + "?\nPlease note that you are allowed only one attempt to answer.");
                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleButtonClick(itemView, (String) optionButton1.getText());
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
            });

            optionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Are you sure?");
                    builder.setIcon(R.drawable.logophspinner);
                    builder.setMessage("Are you certain that you wish to select "+ optionButton2.getText().toString() + "?\nPlease note that you are allowed only one attempt to answer.");
                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleButtonClick(itemView, (String) optionButton2.getText());
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
            });

            optionButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Are you sure?");
                    builder.setIcon(R.drawable.logophspinner);
                    builder.setMessage("Are you certain that you wish to select "+ optionButton3.getText().toString() + "?\nPlease note that you are allowed only one attempt to answer.");
                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleButtonClick(itemView, (String) optionButton3.getText());
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
            });
        }

        public void bind(QuizQuestion quizQuestion) {
            quizQuestionTextView.setText(quizQuestion.getQuestionText());

            String imageUrl = quizQuestion.getImageUrl();
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.loader)
                    .into(quizPhotoImageView);

            // Set the option button text
            optionButton1.setText(quizQuestion.getOption1());
            optionButton2.setText(quizQuestion.getOption2());
            optionButton3.setText(quizQuestion.getOption3());
            quizIDText.setText(quizQuestion.getQuizIdRe());
        }
    }

    private void handleButtonClick(View itemView, String answerQuiz) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String userId = currentUser.getUid();

        TextView quizIDXML = itemView.findViewById(R.id.quizIDText);
        String quizId = quizIDXML.getText().toString();
        String answer = answerQuiz;


        DatabaseReference quizAnswersRef = FirebaseDatabase.getInstance("https://powerhouse-app-ph-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("QuizAnswers");

        // Create a query to check if there's an existing answer for the same quizID and userID
        Query query = quizAnswersRef.orderByChild("quizID")
                .equalTo(quizId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userHasAnswered = false;

                // Check if there's an answer for the same quizID and userID
                for (DataSnapshot answerSnapshot : dataSnapshot.getChildren()) {
                    String existingUserId = answerSnapshot.child("userID").getValue(String.class);

                    if (existingUserId != null && existingUserId.equals(userId)) {
                        userHasAnswered = true;
                        break;
                    }
                }

                if (!userHasAnswered) {
                    DatabaseReference newAnswerRef = quizAnswersRef.push();

                    // Set the values for the new answer
                    newAnswerRef.child("quizID").setValue(quizId);
                    newAnswerRef.child("userID").setValue(userId);
                    newAnswerRef.child("answer").setValue(answer);

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Submited");
                    builder.setIcon(R.drawable.logophspinner);
                    builder.setMessage("Your response has been submitted.\nKindly await the results.\nGood Luck!");
                    builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Already answered");
                    builder.setIcon(R.drawable.logophspinner);
                    builder.setMessage("You have already responded to this quiz.\nAdditional responses are not permitted.");
                    builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
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
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error checking existing answers: " + error.getMessage());
            }
        });
    }
}
