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
import com.aryutalks.Models.UserFollowersModel;
import com.aryutalks.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UserFollowersAdapter extends RecyclerView.Adapter<UserFollowersAdapter.ViewHolder> {

    private Context context;
    private List<UserFollowersModel> followersList;

    public UserFollowersAdapter(Context context, List<UserFollowersModel> followersList) {
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_followers_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserFollowersModel follower = followersList.get(position);
        holder.usernameTextView.setText(follower.getUsername());
        Picasso.get().load(follower.getImageUrl()).into(holder.userImageView);
        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", follower.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView usernameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }
}
