package com.example.review2;

public class FoodItem {
    private String name;
    private int price;
    private int upvotes;
    private int downvotes;
    private int imageResId; // Add this line


    public FoodItem(String name, int price, int upvotes, int downvotes,int imageResId) {
        this.name = name;
        this.price = price;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.imageResId = imageResId; // Add this line

    }
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getImageResId() {
        return imageResId;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}