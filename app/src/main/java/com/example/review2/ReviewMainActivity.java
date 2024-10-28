package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewMainActivity extends BaseActivity {
    private TextView mainUserName,avgRating;
    private final String currentUserName="박재혁";
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private List<Review> reviewList;
    private ImageButton addReview;

    private static final int WRITE_REVIEW_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reviewmain);
        setupBottomNavigation();

        //툴바
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("휴게소 리뷰");


        mainUserName=findViewById(R.id.mainUserName);
        mainUserName.setText(currentUserName);

        reviewList = new ArrayList<>();
        reviewList.add(new Review("영희", R.drawable.ic_profile, 4.5, "정말 좋은 휴게소"));
        reviewList.add(new Review("철수", R.drawable.ic_profile, 3.0, "그저 그래요"));
        reviewList.add(new Review("길동", R.drawable.ic_profile, 1.0, "별로에요"));


        recyclerView = findViewById(R.id.recyclerViewReviews);
        adapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.scrollToPosition(reviewList.size() - 1);

        avgRating = findViewById(R.id.avgRating);
        avgRating.setText(String.format("평균 별점: %.1f", calculateAverageRating()));

        addReview=findViewById(R.id.addReview);
        addReview.setOnClickListener(v -> {
            if(reviewCheck(currentUserName)){
                Toast.makeText(this,"이미 리뷰를 작성하셨습니다.",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent =new Intent(this,AddReviewActivity.class);
                intent.putExtra("userName", currentUserName);
                intent.putExtra("userPhotoResId", R.drawable.ic_profile);
                startActivityForResult(intent,WRITE_REVIEW_REQUEST_CODE);
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(ReviewMainActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private double calculateAverageRating() {
        double totalRating = 0;
        for (Review review : reviewList) {
            totalRating += review.getRating();
        }
        return totalRating / reviewList.size();
    }
    private boolean reviewCheck(String mainUserName) {
        for (Review review : reviewList) {
            if (review.getUserName().equals(mainUserName)) {
                return true;
            }
        }
        return false;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //
        if (requestCode == WRITE_REVIEW_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String userName = data.getStringExtra("userName");
                int userPhotoResId = data.getIntExtra("userPhotoResId", R.drawable.ic_profile);
                double rating = data.getDoubleExtra("rating", 0);
                String comment = data.getStringExtra("comment");


                Review newReview = new Review(userName, userPhotoResId, rating, comment);


                reviewList.add(newReview);
                adapter.notifyDataSetChanged();
                avgRating = findViewById(R.id.avgRating);
                avgRating.setText(String.format("평균 별점: %.1f", calculateAverageRating()));


            }
        }
    }

}