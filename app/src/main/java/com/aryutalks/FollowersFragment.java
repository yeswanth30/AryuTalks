package com.aryutalks;

import android.annotation.SuppressLint;
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
import com.aryutalks.Models.FollowersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowersFragment extends Fragment {

    private RecyclerView followersRecyclerView;
    private FollowersAdapter followersAdapter;
    private List<FollowersModel> followersList;
    private DatabaseReference usersRef;
    private String userId;

    TextView followingNameTextView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followers_fragment, container, false);

        followersRecyclerView = view.findViewById(R.id.followersRecyclerView);
        followingNameTextView = view.findViewById(R.id.followingNameTextView);

        followersList = new ArrayList<>();
        followersAdapter = new FollowersAdapter(getContext(),followersList);
        followersRecyclerView.setAdapter(followersAdapter);

        followersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            userId = getActivity().getIntent().getStringExtra("userid");
        }

        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followers");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followerUserId = snapshot.getKey();
                    fetchFollowerDetails(followerUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchFollowerDetails(String followerUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(followerUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);
                    Log.d("FollowersFragment", "Username: " + name);
                    Log.d("FollowersFragment", "Image URL: " + imageUrl);
                    if (name != null && imageUrl != null) {
                        FollowersModel follower = new FollowersModel(followerUserId, name, imageUrl);
                        followersList.add(follower);
                        followersAdapter.notifyDataSetChanged();
                        Log.d("FollowersFragment", "Model data set: " + name + imageUrl );

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
