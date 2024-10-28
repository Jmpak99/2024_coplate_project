package com.example.review2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigation() {
        Button homeButton = findViewById(R.id.homeButton);
        Button favoriteButton = findViewById(R.id.favoriteButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (!(BaseActivity.this instanceof MainActivity)) {
                        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(BaseActivity.this instanceof FavoritesActivity)) {
                    Intent intent = new Intent(BaseActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
