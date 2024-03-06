package com.aryutalks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailPostActivity extends AppCompatActivity {

    private ImageView imageView, back, likeImageView;
    private TextView hashtagTextView, nametextView, textView123, textViewMiddle;
    private TextView textView1, textView2;
    private boolean isLiked = false;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpostactivity);

        imageView = findViewById(R.id.backgroundImageView);
        hashtagTextView = findViewById(R.id.hastag);
        nametextView = findViewById(R.id.username);
        textView123 = findViewById(R.id.content);
        textViewMiddle = findViewById(R.id.textViewMiddle);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        likeImageView = findViewById(R.id.likeimage);
        back = findViewById(R.id.back);

        postId = getIntent().getStringExtra("postid");
//
//        if (!isLoggedIn()) {
//            showLoginDialog();
//        }
        // Check if the post is already liked
        checkIfLiked();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPostActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



                 likeImageView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if(isLoggedIn()) {
                          onLikeClicked(v);
                      } else {
                          showLoginDialog();
                      }
                      }
                        });



        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postId);
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String hashtag = dataSnapshot.child("hashtag").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String userId = dataSnapshot.child("userId").getValue(String.class);
                    String heading = dataSnapshot.child("heading").getValue(String.class);
                    DataSnapshot likesSnapshot = dataSnapshot.child("likedUsers");
                    DataSnapshot commentsSnapshot = dataSnapshot.child("comments");

                    int likesCount = (int) likesSnapshot.getChildrenCount();
                    int commentsCount = (int) commentsSnapshot.getChildrenCount();

                    textView1.setText(String.valueOf(likesCount));
                    textView2.setText(String.valueOf(commentsCount));

                    Picasso.get().load(imageUrl).into(imageView);
                    hashtagTextView.setText(hashtag);
                    textViewMiddle.setText(heading);

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userName = snapshot.child("name").getValue(String.class);
                                nametextView.setText("By-" + userName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    textView123.setText(content);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        updateLikeStatus();
    }

    public void onLikeClicked(View view) {
        isLiked = !isLiked;

        if (isLiked) {
            likeImageView.setImageResource(R.drawable.fullhear);
        } else {
            likeImageView.setImageResource(R.drawable.plainhear);
        }

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postId).child("likedUsers").child(getCurrentUserId());
        if (isLiked) {
            postRef.setValue(true);
            int currentLikes = Integer.parseInt(textView1.getText().toString());
            textView1.setText(String.valueOf(currentLikes + 1));
        } else {
            postRef.removeValue();
            int currentLikes = Integer.parseInt(textView1.getText().toString());
            textView1.setText(String.valueOf(currentLikes - 1));
        }
    }
    private void updateLikeStatus() {
        if (isLiked) {
            likeImageView.setImageResource(R.drawable.fullhear);
        } else {
            likeImageView.setImageResource(R.drawable.plainhear);
        }
    }
    private void checkIfLiked() {
        DatabaseReference likedUsersRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postId).child("likedUsers").child(getCurrentUserId());
        likedUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isLiked = true;
                    likeImageView.setImageResource(R.drawable.fullhear);
                } else {
                    isLiked = false;
                    likeImageView.setImageResource(R.drawable.plainhear);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "");
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please log in to like this post")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open login activity
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
