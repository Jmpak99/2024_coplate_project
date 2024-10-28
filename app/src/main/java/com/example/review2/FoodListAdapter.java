package com.example.review2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<FoodItem> {
    private final LayoutInflater inflater;

    public FoodListAdapter(Context context, List<FoodItem> objects) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.food_item, parent, false);
        }

        FoodItem item = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.rlvvbyo308cl);
        TextView priceTextView = convertView.findViewById(R.id.r47fem66nqg6);
        TextView votesTextView = convertView.findViewById(R.id.rvap4afd30jo);
        ImageView foodImageView = convertView.findViewById(R.id.foodimage); // Add this line


        foodImageView.setImageResource(item.getImageResId()); // Add this line
        nameTextView.setText(item.getName());
        priceTextView.setText("가격: " + item.getPrice() + "원");
        votesTextView.setText("추천: " + item.getUpvotes() + " / 비추천: " + item.getDownvotes());

        return convertView;
    }
}