package com.aryutalks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryutalks.DetailPostActivity;
import com.aryutalks.DetailUserActivity;
import com.aryutalks.Models.SearchedPostModel;
import com.aryutalks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private Context context;
    private List<SearchedPostModel> searchedPostList;

    public SearchResultAdapter(Context context, List<SearchedPostModel> searchedPostList) {
        this.context = context;
        this.searchedPostList = searchedPostList;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchedPostModel searchedPost = searchedPostList.get(position);
        holder.usernameTextView.setText(searchedPost.getUsername());
        holder.postContentTextView.setText(searchedPost.getContent());
        holder.heading.setText(searchedPost.getHeading());
        holder.hashtag.setText(searchedPost.getHashtag());
        Picasso.get().load(searchedPost.getImageUrl()).into(holder.backgroundImageView);


        holder.backgroundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailPostActivity.class);
                intent.putExtra("postid", searchedPost.getPostId());

                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return searchedPostList.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView;
        private TextView postContentTextView;

        private TextView heading;
        private TextView hashtag;
        private ImageView backgroundImageView;


        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.nametextView);
            postContentTextView = itemView.findViewById(R.id.textView123);
            postContentTextView.setMaxLines(1);
            postContentTextView.setEllipsize(TextUtils.TruncateAt.END);
            postContentTextView.setText("Your long text goes here...");
            postContentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), postContentTextView.getText(), Toast.LENGTH_LONG).show();
                }
            });
            heading = itemView.findViewById(R.id.textView12);
            hashtag = itemView.findViewById(R.id.textView1244);
            backgroundImageView = itemView.findViewById(R.id.backgroundImageView);
        }


    }
}
