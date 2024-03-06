package com.aryutalks;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.Adapters.UserFollowingAdapter;
import com.aryutalks.Models.UserFollowingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFollowingFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserFollowingAdapter adapter;
    private List<UserFollowingModel> followingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_following, container, false);
        recyclerView = view.findViewById(R.id.followingRecyclerView);
        followingList = new ArrayList<>();
        adapter = new UserFollowingAdapter(getContext(), followingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        fetchFollowingData();
        return view;
    }

    private void fetchFollowingData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userid", "");

        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("following");

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followingUserId = snapshot.getKey();
                    fetchFollowingUserData(followingUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchFollowingUserData(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);
                    UserFollowingModel followingModel = new UserFollowingModel(userId, username, imageUrl);
                    followingList.add(followingModel);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
