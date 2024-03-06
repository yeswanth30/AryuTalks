package com.aryutalks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.DetailUserActivity;
import com.aryutalks.Models.UserFollowingModel;
import com.aryutalks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserFollowingAdapter extends RecyclerView.Adapter<UserFollowingAdapter.ViewHolder> {

    private Context context;
    private List<UserFollowingModel> followingList;

    public UserFollowingAdapter(Context context, List<UserFollowingModel> followingList) {
        this.context = context;
        this.followingList = followingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_following_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserFollowingModel followingModel = followingList.get(position);
        holder.usernameTextView.setText(followingModel.getUsername());
        Picasso.get().load(followingModel.getImageUrl()).placeholder(R.drawable.authorrr).into(holder.userImageView);
        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", followingModel.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView usernameTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }
}
