package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;

public class AddReviewActivity extends BaseActivity {
    private ImageView reviewUserPic;
    private TextView reviewUserName;
    private RatingBar reviewRatingBar;
    private EditText editReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_review);
        setupBottomNavigation();

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("리뷰 작성");

        reviewUserPic = findViewById(R.id.mainUserPic);
        reviewUserName = findViewById(R.id.mainUserName);
        reviewRatingBar = findViewById(R.id.reviewRatingBar);
        editReview = findViewById(R.id.editReview);
        Button submitbutton = findViewById(R.id.submitButton);

        Intent intent = getIntent();
        String userName= intent.getStringExtra("userName");
        int userPhotoResId = intent.getIntExtra("userPhotoResId", R.drawable.ic_profile);

        reviewUserPic.setImageResource(userPhotoResId);
        reviewUserName.setText(userName);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
                Log.d("AddReviewActivity", "Review submitted: " + reviewUserName.getText().toString() + ", " + reviewRatingBar.getRating() + ", " + editReview.getText().toString());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitReview() {
        String userName = reviewUserName.getText().toString();
        int userPhotoResId = getIntent().getIntExtra("userPhotoResId", R.drawable.ic_profile); // 사용자 사진의 리소스 ID
        double rating = reviewRatingBar.getRating();
        String comment = editReview.getText().toString();



        Intent intent = new Intent();
        intent.putExtra("userName", userName);
        intent.putExtra("userPhotoResId", userPhotoResId);
        intent.putExtra("rating", rating);
        intent.putExtra("comment", comment);
        setResult(RESULT_OK, intent);


        finish();
    }

}