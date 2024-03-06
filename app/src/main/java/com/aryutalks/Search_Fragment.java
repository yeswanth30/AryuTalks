package com.aryutalks;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aryutalks.Adapters.SearchResultAdapter;
import com.aryutalks.Models.SearchedPostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Search_Fragment extends Fragment {
    private EditText searchText;
    private TextView noResultsTextView;
    private RecyclerView searchResultsRecyclerView;
    private List<SearchedPostModel> searchedPosts;
    private SearchResultAdapter searchResultAdapter;
    String hashtag;
    ImageView profile,add;
    private String userId="";
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchText = view.findViewById(R.id.searchtext);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);
        searchResultsRecyclerView = view.findViewById(R.id.recycler_view);
        profile = view.findViewById(R.id.profile);
        add = view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    Intent intent = new Intent(getActivity(), AddPostActivity.class);
                    startActivity(intent);
                } else {
                    showLoginAlert();
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);



        if(isLoggedIn()){
            String imageUrl = sharedPreferences.getString("imageurl", "");
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.authorrr)
                    .error(R.drawable.authorrr)
                    .into(profile);

        } else {
            Picasso.get().load(R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.authorrr)
                    .into(profile);


        }
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchedPosts = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(getContext(), searchedPosts);
        searchResultsRecyclerView.setAdapter(searchResultAdapter);

        view.findViewById(R.id.searchButton).setOnClickListener(v -> performSearch());

        return view;
    }

    private void performSearch() {
         hashtag = searchText.getText().toString().trim();
        if (hashtag.isEmpty()) {
            return;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");


        if (hashtag.startsWith("#")) {
            Log.d("SearchFragment", "Searching by hashtag: " + hashtag);
        } else {


            Log.d("SearchFragment", "Searching by username: " + hashtag);
        }

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchedPosts.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String hashtags = postSnapshot.child("hashtag").getValue(String.class);
                    String username = postSnapshot.child("username").getValue(String.class);
                    if(hashtags.equals(hashtag) || username.equals(hashtag)){
                        String postId = postSnapshot.child("postId").getValue(String.class);
                        String userId = postSnapshot.child("userId").getValue(String.class);

                        String content = postSnapshot.child("content").getValue(String.class);
                        String imageUrl = postSnapshot.child("imageUrl").getValue(String.class);
                        String heading = postSnapshot.child("heading").getValue(String.class);

                        SearchedPostModel post = new SearchedPostModel(postId, userId, username, content,imageUrl,heading,hashtags);
                        searchedPosts.add(post);

                        Log.d("SearchFragment", "Post ID: " + postId + ", Username: " + username + ", Content: " + content);

                    }else{

                    }
                   }

                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SearchFragment", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateUI() {
        if (searchedPosts.isEmpty()) {
            Log.d("SearchFragment", "No search results found" + searchedPosts );
            searchResultsRecyclerView.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            Log.d("SearchFragment", "Search results found: " + searchedPosts);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.GONE);
        }

        searchResultAdapter.notifyDataSetChanged();
    }

    public String getuseridforusername(String username){
       // String userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String usernames = postSnapshot.child("username").getValue(String.class);
                    if(username.equals(usernames)){
                       userId = postSnapshot.child("userid").getValue(String.class);

                    }else{

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SearchFragment", "Database Error: " + databaseError.getMessage());
            }
        });
        return userId;
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void showLoginAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Login Required");
        builder.setMessage("You need to login to access this feature.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
