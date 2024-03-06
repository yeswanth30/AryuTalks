package com.aryutalks.Adapters;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aryutalks.DetailUserActivity;
import com.aryutalks.Models.CommentModel;
import com.aryutalks.Models.PostModel;
import com.aryutalks.Models.UserModel;
import com.aryutalks.R;

import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> comments;
    private List<UserModel> users;
    private Context context;

    public CommentAdapter( List<CommentModel> comments) {
      //  this.context = context;
        this.comments = comments;
      //  this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel comment = comments.get(position);
            Log.e("comment adapter",comment.getName());
           holder.userNameTextView.setText(comment.getName());
            Picasso.get().load(comment.getImageurl()).into(holder.userImageView);
            holder.commentTextView.setText(comment.getComment());

        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", comment.getUserid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView commentTextView;
        ImageView userImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            userNameTextView = itemView.findViewById(R.id.nametextView1);
//            commentTextView = itemView.findViewById(R.id.commenttext);
//            userImageView = itemView.findViewById(R.id.circleImageView1);
        }
    }
}
