package com.example.review2;

public class Review {
    private String userName;
    private int userPhotoResId;
    private double rating;
    private String comment;

    public Review(String userName, int  userPhotoResId, double rating, String comment) {
        this.userName = userName;
        this.userPhotoResId= userPhotoResId;
        this.rating = rating;
        this.comment = comment;
    }





    public String getUserName() {
        return userName;
    }

    public int getUserPhotoResId() {
        return userPhotoResId;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}