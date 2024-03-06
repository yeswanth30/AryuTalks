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
import com.aryutalks.Models.FollowingModel;
import com.aryutalks.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    private List<FollowingModel> followingList;
    private Context context;

    public FollowingAdapter(Context context,List<FollowingModel> followingList) {
        this.context = context;

        this.followingList = followingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowingModel following = followingList.get(position);
        holder.followingNameTextView.setText(following.getName());

        Log.d("FollowingAdapter", "name: " + following.getName());
        Log.d("FollowingAdapter", "Image URL: " + following.getImageurl());

        Picasso.get().load(following.getImageurl()).into(holder.followingImageView);

        holder.followingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", following.getUserid());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView followingNameTextView;
        ImageView followingImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            followingNameTextView = itemView.findViewById(R.id.followingNameTextView);
            followingImageView = itemView.findViewById(R.id.followingImageView);
        }
    }
}
