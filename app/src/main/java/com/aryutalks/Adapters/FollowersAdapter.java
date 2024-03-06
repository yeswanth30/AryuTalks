package com.aryutalks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.DetailUserActivity;
import com.aryutalks.Models.FollowersModel;
import com.aryutalks.Models.UserModel;
import com.aryutalks.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {
    private List<FollowersModel> followersList;
    private Context context;


    public FollowersAdapter(Context context,List<FollowersModel> followersList) {
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowersModel follower = followersList.get(position);
        holder.followerNameTextView.setText(follower.getName());
        Picasso.get().load(follower.getImageurl()).into(holder.followerImageView);
        holder.followerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", follower.getUserid());
                context.startActivity(intent);
            }
        });
        Log.d("FollowersAdapter", "name: " + follower.getName() + ", Image URL: " + follower.getImageurl());

    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView followerNameTextView;
        ImageView followerImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            followerNameTextView = itemView.findViewById(R.id.followerNameTextView);
            followerImageView = itemView.findViewById(R.id.followerImageView);
        }
    }
}
