package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class FoodActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food); // food.xml 레이아웃 설정
        setupBottomNavigation();

        ListView foodListView = findViewById(R.id.foodlist);
        FoodListAdapter adapter = new FoodListAdapter(this, new ArrayList<>());
        foodListView.setAdapter(adapter);
// Add food items to the adapter
        adapter.add(new FoodItem("치즈라면", 6000, 8, 1, R.drawable.plus1));
        adapter.add(new FoodItem("치즈라면", 6000, 8, 1, R.drawable.plus1));
        adapter.add(new FoodItem("치즈라면", 6000, 8, 1, R.drawable.plus1));
        adapter.add(new FoodItem("치즈라면", 6000, 8, 1, R.drawable.plus1));

// ... add more items ...

        ImageView myImageView = findViewById(R.id.goinfo);
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });


    }
}
