package com.aryutalks;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aryutalks.Adapters.PostAdapter;
import com.aryutalks.Adapters.UserImageAdapter;
import com.aryutalks.Models.CommentModel;
import com.aryutalks.Models.PostModel;
import com.aryutalks.Models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AllPost_Fragment extends Fragment {

    ImageView add,back;
    RecyclerView recyclerView, userRecyclerView;
    PostAdapter postAdapter;
    UserImageAdapter userAdapter;
    List<PostModel> posts;
    List<UserModel> users;
    String userids;
    Map<String, List<CommentModel>> commentsMap ;

    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<>();
        commentsMap = new HashMap<>();
        users=new ArrayList<>();
    }


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allpost, container, false);
        add = view.findViewById(R.id.add);
        recyclerView = view.findViewById(R.id.recycler_view);
        userRecyclerView = view.findViewById(R.id.ImagesrecyclerView);
        back = view.findViewById(R.id.back);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);

        if (isLoggedIn()) {
            String imageUrl = sharedPreferences.getString("imageurl", "");
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.authorrr)
                    .error(R.drawable.authorrr)
                    .into(back);
        } else {
            Picasso.get().load(R.drawable.logo)
                    .placeholder(R.drawable.authorrr)
                    .error(R.drawable.authorrr)
                    .into(back);
        }
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

         sharedPreferences = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        userids = sharedPreferences.getString("userid", "");



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        postAdapter = new PostAdapter(getContext(),posts,users);
//        recyclerView.setAdapter(postAdapter);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userAdapter = new UserImageAdapter(getContext(),users);
        userRecyclerView.setAdapter(userAdapter);

        fetchPostsData();
        fetchUserData();



        return view;
    }

    private void fetchPostsData() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
       // Map<String, List<CommentModel>> commentsMap = new HashMap<>();

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = postSnapshot.child("imageUrl").getValue(String.class);
                    String hashtag = postSnapshot.child("hashtag").getValue(String.class);
                    String userId = postSnapshot.child("userId").getValue(String.class);
                    String content = postSnapshot.child("content").getValue(String.class);
                    String postId = postSnapshot.child("postId").getValue(String.class);
                    String heading = postSnapshot.child("heading").getValue(String.class);
                    Boolean isLiked = postSnapshot.child("likedUsers").child(userids).exists();

                    List<CommentModel> comments = new ArrayList<>();

                    DataSnapshot commentsSnapshot = postSnapshot.child("comments");

                    for (DataSnapshot commentSnapshot : commentsSnapshot.getChildren()) {
                        String commentUserId = commentSnapshot.child("userid").getValue(String.class);
                        String commentContent = commentSnapshot.child("comment").getValue(String.class);

                        for (UserModel allusers : users) {
                                if (commentUserId.equals(allusers.getUserid())) {
                                    Log.e("all post fragment",commentContent+commentUserId);
                                    CommentModel comment = new CommentModel(commentUserId, commentContent, allusers.getName(), allusers.getImageurl());
                                    comments.add(comment);
                                    if (commentsMap.containsKey(commentUserId)) {
                                        commentsMap.get(commentUserId).add(comment);
                                    } else {
                                        List<CommentModel> userComments = new ArrayList<>();
                                        userComments.add(comment);
                                        commentsMap.put(commentUserId, userComments);
                                    }
                                }
                            }

                    }
                    PostModel post = new PostModel(imageUrl, hashtag, userId, content, postId, heading, isLiked, comments);
                    posts.add(post);

                }

                postAdapter = new PostAdapter(getContext(), posts, users, commentsMap);
                recyclerView.setAdapter(postAdapter);

                if (postAdapter != null) {
                    postAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Error", "postAdapter is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }


    private void fetchUserData() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserModel user = userSnapshot.getValue(UserModel.class);
                    users.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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

    public void onResume(){
        super.onResume();
        fetchPostsData();
        fetchUserData();


    }

}
