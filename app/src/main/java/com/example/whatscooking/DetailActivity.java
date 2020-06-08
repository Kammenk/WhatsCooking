package com.example.whatscooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailTitle;
    TextView detailQuantity;
    TextView detailCalories;
    TextView detailDietLabel;
    TextView detailHealthLabel;
    TextView detailIngredients;
    TextView detailTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().hide();

        int cookTimeTopMinimum = 60;
        int cookTimeBottomMinimum = 0;

        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailQuantity = findViewById(R.id.detailQuantity);
        detailCalories = findViewById(R.id.detailCalories);
        detailDietLabel = findViewById(R.id.detailDietLabel);
        detailHealthLabel = findViewById(R.id.detailHealthLabel);
        detailIngredients = findViewById(R.id.detailIngredients);
        detailTotalTime = findViewById(R.id.detailTotalTime);

        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("Image");
        Picasso.get().load(imageURL).fit().centerInside().into(detailImage);
        detailTitle.setText(intent.getStringExtra("Title"));
        detailQuantity.setText("Servings: " + intent.getIntExtra("Quantity", Integer.parseInt("0")));
        detailCalories.setText("Calories: " + intent.getIntExtra("Calories", Integer.parseInt("0")));
        detailDietLabel.setText("Diet Label: " + intent.getStringExtra("dietLabel"));
        detailHealthLabel.setText("Health Label: " + intent.getStringExtra("healthLabel"));
        detailIngredients.setText("Ingredients: " + intent.getStringExtra("ingredients"));
        int cookTime = intent.getIntExtra("totalTIme",Integer.parseInt("0"));

        if (cookTime > cookTimeTopMinimum){
            detailTotalTime.setText("Cook time: Over 60 minutes");
        } else if(cookTime <= cookTimeBottomMinimum) {
            detailTotalTime.setText("Cook time: Not measured");
        } else {
            detailTotalTime.setText("Cook time: " + cookTime + " minutes");
        }

    }

    public void goBack(View view){
        onBackPressed();
    }
}
