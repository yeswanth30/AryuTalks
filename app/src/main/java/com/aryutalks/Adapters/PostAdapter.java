package com.aryutalks.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aryutalks.DetailPostActivity;
import com.aryutalks.DetailUserActivity;
import com.aryutalks.LoginActivity;
import com.aryutalks.Models.CommentModel;
import com.aryutalks.Models.PostModel;
import com.aryutalks.Models.UserModel;
import com.aryutalks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<PostModel> posts;
    private List<UserModel> users;
    private Context context;
    private Map<String,List<CommentModel>> commentMap;

    public PostAdapter(Context context, List<PostModel> posts, List<UserModel> users,Map<String, List<CommentModel>> commentMap ) {
        this.context = context;
        this.posts = posts;
        this.users = users;
        this.commentMap=commentMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel post = posts.get(position);
        bindComments(holder, post);
        Picasso.get().load(post.getImageUrl()).into(holder.imageView);
        holder.hashtagTextView.setText(post.getHashtag());
        holder.textView123.setText(post.getContent());




        for (UserModel user : users) {
            if (user.getUserid().equals(post.getUserId())) {
                holder.nametextView.setText(user.getName());
                Picasso.get().load(user.getImageurl()).into(holder.circleImageView);
                break;
            }
        }

        boolean isLikedByCurrentUser = post.isLikedByCurrentUser();

        if (isLikedByCurrentUser) {
            holder.likeimage.setImageResource(R.drawable.fullhear);
        } else {
            holder.likeimage.setImageResource(R.drawable.plainhear);
        }

        holder.likeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    // Toggle like status
                    boolean isLiked = post.isLikedByCurrentUser();
                    if (isLiked) {
                        holder.likeimage.setImageResource(R.drawable.plainhear);
                        updateLikeStatus(post.getPostId(), false);
                    } else {
                        holder.likeimage.setImageResource(R.drawable.fullhear);
                        updateLikeStatus(post.getPostId(), true);
                    }
                    post.setLikedByCurrentUser(!isLiked);
                } else {
                    showLoginDialog();
                }
            }
        });

        holder.commentimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    showCommentDialog(post.getPostId());
                } else {
                    showLoginDialog();
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPostActivity.class);
                intent.putExtra("postid", post.getPostId());
                context.startActivity(intent);
            }
        });

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", post.getUserId());
                context.startActivity(intent);
            }
        });

//        List<CommentModel> comments = commentMap.get(post.getPostId());
//        if (comments != null) {
//            CommentAdapter commentAdapter = new CommentAdapter(comments);
//            holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.commentsRecyclerView.getContext()));
//            holder.commentsRecyclerView.setAdapter(commentAdapter);
//        }
//        List<CommentModel> comments = post.getComments();
//        if (comments != null && !comments.isEmpty()) {
//            CommentAdapter commentAdapter = new CommentAdapter(comments);
//            holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.commentsRecyclerView.getContext()));
//            holder.commentsRecyclerView.setAdapter(commentAdapter);
//        } else {
//            holder.commentsRecyclerView.setVisibility(View.GONE);
//        }

//        List<CommentModel> comments = post.getComments();
//        if (comments != null && !comments.isEmpty()) {
//            // Log the number of comments received
//            Log.d("PostAdapter", "Number of comments for post: " + comments.size());
//
//            CommentAdapter commentAdapter = new CommentAdapter(comments);
//            holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.commentsRecyclerView.getContext()));
//            holder.commentsRecyclerView.setAdapter(commentAdapter);
//        } else {
//            // Log if no comments are present for the post
//            Log.d("PostAdapter", "No comments for post");
//
//            holder.commentsRecyclerView.setVisibility(View.GONE);
//        }



    }


//        List<CommentModel> comments = post.getComments();
//        CommentAdapter commentAdapter = new CommentAdapter(context, comments, users);
//        holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        holder.commentsRecyclerView.setAdapter(commentAdapter);



    @Override
    public int getItemCount() {
        return posts.size();
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please log in to perform this action")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
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

    private void updateLikeStatus(String postId, boolean isLiked) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        if (isLiked) {
            String userId = getCurrentUserId();
            postsRef.child(postId).child("likedUsers").child(userId).setValue(true);
        } else {
            String userId = getCurrentUserId();
            postsRef.child(postId).child("likedUsers").child(userId).removeValue();
        }
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, circleImageView, likeimage, commentimage;
        TextView hashtagTextView, nametextView, textView123, commenttext;
        RecyclerView commentsRecyclerView;
        LinearLayout commentsLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.backgroundImageView);
            hashtagTextView = itemView.findViewById(R.id.textView12);
            nametextView = itemView.findViewById(R.id.nametextView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            textView123 = itemView.findViewById(R.id.textView123);
            textView123.setMaxLines(1);
            textView123.setEllipsize(TextUtils.TruncateAt.END);
            textView123.setText("Your long text goes here...");
            textView123.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), textView123.getText(), Toast.LENGTH_LONG).show();
                }
            });


            likeimage = itemView.findViewById(R.id.likeimage);
            commentimage = itemView.findViewById(R.id.commentimage);
//            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
//            commenttext = itemView.findViewById(R.id.commenttext);
         //   commentsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            commentsLayout = itemView.findViewById(R.id.commentsLayout);

        }
    }

    private void showCommentDialog(String postId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_comment, null);
        EditText commentEditText = view.findViewById(R.id.commentEditText);
        Button sendButton = view.findViewById(R.id.sendButton);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    saveComment(postId, comment);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void saveComment(String postId, String comment) {
        String userId = getCurrentUserId();
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postId).child("comments");
        String commentId = commentsRef.push().getKey();

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("userid", userId);
        commentData.put("comment", comment);

        commentsRef.child(commentId).setValue(commentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                      //  Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to add comment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchCommentsForPost(String postId, TextView commenttext) {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(postId).child("comments");

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder comments = new StringBuilder();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.child("userid").getValue(String.class);
                    String comment = snapshot.child("comment").getValue(String.class);

                    comments.append(comment).append("\n");
                }
                commenttext.setText(comments.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.e(TAG, "Failed to fetch comments: " + databaseError.getMessage());
            }
        });
    }
    private void bindComments(ViewHolder holder, PostModel post) {
        List<CommentModel> comments = post.getComments();
        //holder.commentsLayout.removeAllViews();

        Log.d("PostAdapter", "Number of comments for post " + post.getPostId() + ": " + (comments != null ? comments.size() : 0)); // Log the number of comments

        if (comments != null && !comments.isEmpty()) {


            for (CommentModel comment : comments) {
                Log.d("PostAdapter", "Comment: " + comment.getName() + " - " + comment.getComment()); // Log each comment

                View commentView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
                TextView userNameTextView = commentView.findViewById(R.id.commentUserNameTextView);
                TextView commentTextView = commentView.findViewById(R.id.commentContentTextView);
                ImageView userImageView = commentView.findViewById(R.id.commentUserImageView);



                userNameTextView.setText(comment.getName());
                commentTextView.setText(comment.getComment());

                Picasso.get().load(comment.getImageurl()).into(userImageView);

                holder.commentsLayout.addView(commentView);

            }
        } else {
            Log.d("PostAdapter", "No comments for post " + post.getPostId());
        }
    }


}



