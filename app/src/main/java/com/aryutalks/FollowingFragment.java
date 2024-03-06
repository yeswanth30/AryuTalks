package com.aryutalks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.Adapters.FollowersAdapter;
import com.aryutalks.Adapters.FollowingAdapter;
import com.aryutalks.Models.FollowingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {

    private RecyclerView followingRecyclerView;
    private FollowingAdapter followingAdapter;
    private List<FollowingModel> followingList =new ArrayList<>();
    private DatabaseReference usersRef;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.following_fragment, container, false);

        followingRecyclerView = view.findViewById(R.id.followingRecyclerView);



        followingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        followingAdapter = new FollowingAdapter(getContext(),followingList);
        followingRecyclerView.setAdapter(followingAdapter);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            userId = getActivity().getIntent().getStringExtra("userid");

            Log.d("FollowingFragment", "userId: " + userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("following");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followingUserId = snapshot.getKey();
                    fetchFollowingDetails(followingUserId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FollowingFragment", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void fetchFollowingDetails(String followingUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(followingUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);
                    Log.d("FollowingFragment", "Username: " + name);
                    Log.d("FollowingFragment", "Image URL: " + imageUrl);
                    if (name != null && imageUrl != null) {
                        FollowingModel following = new FollowingModel(followingUserId, name, imageUrl);
                        followingList.add(following);
                        followingAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
