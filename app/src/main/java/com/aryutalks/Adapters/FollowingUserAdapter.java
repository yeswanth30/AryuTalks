package com.aryutalks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.DetailUserActivity;
import com.aryutalks.Models.FollowingUserModel;
import com.aryutalks.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.ViewHolder> {

    private List<FollowingUserModel> followingUserList;
    private Context context;

    public FollowingUserAdapter(Context context,List<FollowingUserModel> followingUserList) {
        this.context = context;

        this.followingUserList = followingUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowingUserModel user = followingUserList.get(position);
        holder.userNameTextView.setText(user.getName());

        Picasso.get().load(user.getImageurl()).into(holder.userImageView);
        holder.totallayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("userid", user.getUserid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return followingUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView userNameTextView;

        RelativeLayout totallayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.leftImageView);
            userNameTextView = itemView.findViewById(R.id.Nametextview);
            totallayout = itemView.findViewById(R.id.totallayout);
        }
    }
}
