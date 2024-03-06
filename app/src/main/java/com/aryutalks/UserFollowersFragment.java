package com.aryutalks;

import static android.content.Context.MODE_PRIVATE;

import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.Adapters.FollowersAdapter;
import com.aryutalks.Adapters.UserFollowersAdapter;
import com.aryutalks.Models.FollowersModel;
import com.aryutalks.Models.UserFollowersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFollowersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserFollowersAdapter adapter;
    private List<UserFollowersModel> followersList;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_followers, container, false);
        recyclerView = view.findViewById(R.id.followersRecyclerView);
        followersList = new ArrayList<>();
        adapter = new UserFollowersAdapter(getContext(), followersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchFollowersData();

        return view;
    }

    private void fetchFollowersData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_details", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("userid", "");

        DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("followers");
        followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followerUserId = snapshot.getKey();
                    if (followerUserId != null) {
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(followerUserId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String username = dataSnapshot.child("name").getValue(String.class);
                                    String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);
                                    if (username != null && imageUrl != null) {
                                        UserFollowersModel follower = new UserFollowersModel(followerUserId, username, imageUrl);
                                        followersList.add(follower);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
