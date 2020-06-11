package com.example.whatscooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        System.out.println("IMAGEURL  " + imageURL);
        Picasso.get().load(imageURL).fit().centerInside().into(detailImage);
        detailTitle.setText(intent.getStringExtra("Title"));
        detailQuantity.setText("Servings: " + intent.getIntExtra("Quantity", Integer.parseInt("0")));
        detailCalories.setText("Calories: " + intent.getIntExtra("Calories", Integer.parseInt("0")));
        detailDietLabel.setText("Diet Label: " + intent.getStringExtra("dietLabel"));
        detailHealthLabel.setText("Health Label: " + intent.getStringExtra("healthLabel"));
        detailIngredients.setText("Ingredients: " + intent.getStringExtra("ingredients"));
        int cookTime = intent.getIntExtra("totalTIme",Integer.parseInt("0"));

        getSupportActionBar().setTitle(intent.getStringExtra("Title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTitle)));

        if (cookTime > cookTimeTopMinimum){
            detailTotalTime.setText("Cook time: Over 60 minutes");
        } else if(cookTime <= cookTimeBottomMinimum) {
            detailTotalTime.setText("Cook time: Not measured");
        } else {
            detailTotalTime.setText("Cook time: " + cookTime + " minutes");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.addBtn){
            saveRecipe();
            return true;
        } else if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return false;
    }

    void saveRecipe(){
        SQLiteDatabase mainDB = FoodActivity.mainDB;
        String image = detailImage.getDrawable().toString();
        String title = detailTitle.getText().toString();
        String cookTime = detailTotalTime.getText().toString();
        String quantity = detailQuantity.getText().toString();
        String calories = detailCalories.getText().toString();
        String diet = detailDietLabel.getText().toString().split(": ")[1];
        String health = detailHealthLabel.getText().toString().split(": ")[1];
        String ingredients = detailIngredients.getText().toString().split(": ")[1];
        mainDB.execSQL("INSERT INTO recipe (image, title , cookTime , quantity, calories, dietLabel , healthLabel , ingredients ) VALUES ('" + image + " ', '" +title + " ' , '" +cookTime +" ', '"+ quantity
                +"', '"+calories +" ','"+ diet +" ', '"+health +" ','"+ ingredients +" ')");
        Toast.makeText(this,"Recipe saved successfully!",Toast.LENGTH_SHORT).show();
    }
}
