// DetailUserActivity.java
package com.aryutalks;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aryutalks.Adapters.UserDetailsPagerAdapter;
import com.aryutalks.Adapters.UserPostAdapter;
import com.aryutalks.Models.UserPost;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailUserActivity extends AppCompatActivity {

    private ImageView userImageView, back;
    private TextView userNameTextView, userEmailTextView, nameeeee;
    private Button followButton;
    private DatabaseReference currentUserFollowingRef;
    private String userId;
    private String currentUserId;
    TextView Emailtextview;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    RecyclerView recyclerView12;
    List<UserPost> postList = new ArrayList<>();

    UserPostAdapter userPostAdapter;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailuseractivity);

        userImageView = findViewById(R.id.leftImageView);
        //   userNameTextView = findViewById(R.id.Nametextview);
        userEmailTextView = findViewById(R.id.Usernametextview);
        Emailtextview = findViewById(R.id.Emailtextview);
        followButton = findViewById(R.id.followButton);
        back = findViewById(R.id.back);
        nameeeee = findViewById(R.id.nameeeee);

        recyclerView12 = findViewById(R.id.recyclerView12);
        recyclerView12.setHasFixedSize(true);
        recyclerView12.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));





        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        UserDetailsPagerAdapter adapter = new UserDetailsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        userId = getIntent().getStringExtra("userid");

        currentUserId = getCurrentUserId();

        currentUserFollowingRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("following");
        loadUserDetails();
        setFollowButtonText();

        userPostAdapter = new UserPostAdapter(this, postList);
        recyclerView12.setAdapter(userPostAdapter);

        loadUserPosts(userId);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn()) {
                toggleFollowStatus();
                }else{
                    showLoginDialog();
                }
            }
        });
    }

    private void loadUserPosts(String userId) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        postsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserPost userPost = postSnapshot.getValue(UserPost.class);
                    postList.add(userPost);
                }

                if (userPostAdapter != null) {
                    userPostAdapter.notifyDataSetChanged();
                } else {
//                    userPostAdapter = new UserPostAdapter(this, postList);
//                    recyclerView12.setAdapter(userPostAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private void loadUserDetails() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userEmail = dataSnapshot.child("username").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("imageurl").getValue(String.class);
                    String useremail = dataSnapshot.child("email").getValue(String.class);

                    Picasso.get().load(userImageUrl).into(userImageView);
                    // userNameTextView.setText(userName);
                    userEmailTextView.setText(userEmail);
                    Emailtextview.setText(useremail);
                    nameeeee.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void toggleFollowStatus() {
        currentUserFollowingRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserFollowingRef.child(userId).removeValue();
                    DatabaseReference followedUserFollowersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followers");
                    followedUserFollowersRef.child(currentUserId).removeValue();
                    followButton.setText("Follow");
                } else {
                    if (userId.equals(currentUserId)) {
                        Toast.makeText(DetailUserActivity.this, "You cannot follow yourself", Toast.LENGTH_SHORT).show();
                    } else {
                        currentUserFollowingRef.child(userId).setValue(true);
                        DatabaseReference followedUserFollowersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followers");
                        followedUserFollowersRef.child(currentUserId).setValue(true);
                        followButton.setText("Unfollow");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void setFollowButtonText() {
        currentUserFollowingRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    followButton.setText("Unfollow");
                } else {
                    followButton.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "");
    }
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please log in to follow")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

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
