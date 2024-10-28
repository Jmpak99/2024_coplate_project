package com.example.review2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewitem, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProfileItem;
        private TextView textViewUserNameItem;
        private RatingBar ratingBar;
        private TextView textViewComment;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfileItem = itemView.findViewById(R.id.review_user_photo);
            textViewUserNameItem = itemView.findViewById(R.id.review_user_name);
            ratingBar = itemView.findViewById(R.id.review_rating_bar);
            textViewComment = itemView.findViewById(R.id.review_comment);
        }

        void bind(Review review) {
            imageViewProfileItem.setImageResource(review.getUserPhotoResId());
            textViewUserNameItem.setText(review.getUserName());
            ratingBar.setRating((float) review.getRating());
            textViewComment.setText(review.getComment());
        }
    }
}