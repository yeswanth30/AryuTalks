package com.aryutalks;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aryutalks.Adapters.FollowingUserAdapter;
import com.aryutalks.Adapters.UserDetailsPagerAdapter;
import com.aryutalks.Adapters.UserDetailsPagerAdapter1;
import com.aryutalks.Adapters.UserPostAdapter;
import com.aryutalks.Models.FollowingUserModel;
import com.aryutalks.Models.UserPost;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class Profile_Fragment extends Fragment {


    private TextView logoutTextView,Nametextview,Usernametextview,Emailtextview;

    private List<FollowingUserModel> followingUserList;
    private FollowingUserAdapter followingUserAdapter;

    ImageView profile,add,leftImageView;

    List<UserPost> postList = new ArrayList<>();

    UserPostAdapter userPostAdapter;

    String userId ;
    RecyclerView recyclerView12;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userId = getCurrentUserId();

        logoutTextView = view.findViewById(R.id.logout);
        profile = view.findViewById(R.id.profile);
        add = view.findViewById(R.id.add);
        leftImageView = view.findViewById(R.id.leftImageView);
        Nametextview = view.findViewById(R.id.Nametextview);
        Usernametextview = view.findViewById(R.id.Usernametextview);
        Emailtextview = view.findViewById(R.id.Emailtextview);

        recyclerView12 = view.findViewById(R.id.recyclerView12);
        recyclerView12.setHasFixedSize(true);
        recyclerView12.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageViewDialog();
            }
        });

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        if (isLoggedIn()) {
            UserDetailsPagerAdapter1 adapter = new UserDetailsPagerAdapter1(getChildFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } else {
           // showNotFollowingAnyoneDialog();
        }


        if (isLoggedIn()) {
            logoutTextView.setVisibility(View.VISIBLE);
        } else {
            logoutTextView.setVisibility(View.GONE);
        }



        if(isLoggedIn()){
            userPostAdapter = new UserPostAdapter(getContext(), postList);
            recyclerView12.setAdapter(userPostAdapter);

            loadUserPosts(userId);
        }else {
            showNotFollowingAnyoneDialog();

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);

        if (isLoggedIn()) {
            Nametextview.setText(sharedPreferences.getString("name", ""));
            Usernametextview.setText(sharedPreferences.getString("username", ""));
            Emailtextview.setText(sharedPreferences.getString("email", ""));
            String imageUrl = sharedPreferences.getString("imageurl", "");
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.authorrr)
                    .error(R.drawable.authorrr)
                    .into(leftImageView);
        } else {
            Nametextview.setVisibility(View.GONE);
            Usernametextview.setVisibility(View.GONE);
            Emailtextview.setVisibility(View.GONE);
            leftImageView.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.authorrr)
                    .into(profile);
        }


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



        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });




        return view;
    }

    private void loadUserPosts(String userId) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        postsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserPost userPost = postSnapshot.getValue(UserPost.class);
                    postList.add(userPost);
                }

                if (userPostAdapter != null) {
                    userPostAdapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logout());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
        Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_SHORT).show();
    }


    private void showNotFollowingAnyoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();

        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        Button okButton = dialogView.findViewById(R.id.okButton);

        messageTextView.setText("You are not Loggedin.");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_details", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "");
    }
    private void showImageViewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_image_view, null);

        ImageView imageViewFullScreen = dialogView.findViewById(R.id.imageViewFullScreen);
      ImageButton closeButton = dialogView.findViewById(R.id.closeButton);

        imageViewFullScreen.setImageDrawable(leftImageView.getDrawable());

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

}
