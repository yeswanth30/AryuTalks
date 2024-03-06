package com.aryutalks;



import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aryutalks.Models.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;

    EditText editTextHeading, editTextContent, editTextHashtag;
    Button selectImageButton, uploadButton;
    ImageView imageView,back,profile;
    Uri filePath;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    String userid;
    ProgressBar loadingProgress;

    TextView contenterrorText,hastagerrorText,headingerrorText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_activity);

        editTextHeading = findViewById(R.id.editText_heading);
        editTextContent = findViewById(R.id.editText_content);
        editTextHashtag = findViewById(R.id.editText_hashtag);
        selectImageButton = findViewById(R.id.select_image_button);
        uploadButton = findViewById(R.id.upload_button);
        imageView = findViewById(R.id.image_view);
        contenterrorText=findViewById(R.id.contenterrorText);
        hastagerrorText=findViewById(R.id.hastagerrorText);
        headingerrorText=findViewById(R.id.headingerrorText);
        back=findViewById(R.id.back);
        profile=findViewById(R.id.profile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        loadingProgress = findViewById(R.id.loading_progress);


        selectImageButton.setOnClickListener(v -> selectImageFromGallery());

        uploadButton.setOnClickListener(v -> uploadPost());

        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");

        String imageUrl = sharedPreferences.getString("imageurl", "");
        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.authorrr)
                .error(R.drawable.authorrr)
                .into(profile);

        editTextHeading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    editTextHeading.setBackgroundResource(R.drawable.rectangle_signup);
                    headingerrorText.setVisibility(View.GONE);
                    headingerrorText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    editTextContent.setBackgroundResource(R.drawable.rectangle_signup);
                    contenterrorText.setVisibility(View.GONE);
                    contenterrorText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editTextHashtag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    editTextHashtag.setBackgroundResource(R.drawable.rectangle_signup);
                    hastagerrorText.setVisibility(View.GONE);
                    hastagerrorText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void uploadPost() {
        loadingProgress.setVisibility(View.VISIBLE);

        String heading = editTextHeading.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String hashtag = editTextHashtag.getText().toString().trim();

        boolean hasError = false;


        if (heading.isEmpty()) {
            editTextHeading.setBackgroundResource(R.drawable.edittext_border_red);
            headingerrorText.setVisibility(View.VISIBLE);
            headingerrorText.requestFocus();

            headingerrorText.setError("");
            hasError = true;
        }



        if (content.isEmpty()) {
            editTextContent.setBackgroundResource(R.drawable.edittext_border_red);
            contenterrorText.setVisibility(View.VISIBLE);
            contenterrorText.requestFocus();
            contenterrorText.setError("");
            hasError = true;
        }

        if (hashtag.isEmpty()) {
            editTextHashtag.setBackgroundResource(R.drawable.edittext_border_red);
            hastagerrorText.setVisibility(View.VISIBLE);
            hastagerrorText.requestFocus();

            hastagerrorText.setError("");
            hasError = true;
        }

        if (filePath == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (hasError) {
            loadingProgress.setVisibility(View.GONE);
            return;
        }

        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

        ref.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        savePostToDatabase(imageUrl, hashtag);
                        Toast.makeText(AddPostActivity.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                        redirectToMainActivity();
                        loadingProgress.setVisibility(View.GONE);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddPostActivity.this, "Failed to upload post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingProgress.setVisibility(View.GONE);
                });
    }


    private void savePostToDatabase(String imageUrl, String hashtag) {
        String heading = editTextHeading.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        String postId = reference.child("posts").push().getKey();
        String userId = userid;
        String time = getCurrentTime();
        String currentUsername = getcurrentusername();


        String date = getCurrentDate();

        if (postId != null && userId != null) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("postId", postId);
            postData.put("userId", userId);
            postData.put("heading", heading);
            postData.put("content", content);
            postData.put("imageUrl", imageUrl);
            postData.put("timestamp", timestamp);
            postData.put("hashtag", hashtag);
            postData.put("date", date);
            postData.put("time", time);
            postData.put("username", currentUsername);


            reference.child("posts").child(postId).setValue(postData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddPostActivity.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                        redirectToMainActivity(); // Optional: Redirect to MainActivity after successful upload
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddPostActivity.this, "Failed to upload post: " + e.getMessage(), Toast.LENGTH_SHORT).show();


                    });

            editTextHeading.setText("");
            editTextContent.setText("");
            editTextHashtag.setText("");
            imageView.setImageResource(android.R.color.transparent);
        }
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void redirectToMainActivity() {
        Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(new Date());
    }
    private String getcurrentusername() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        return sharedPreferences.getString("name", "");

    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}

