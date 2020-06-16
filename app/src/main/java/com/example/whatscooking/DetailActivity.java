package com.example.whatscooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailQuantity = findViewById(R.id.detailQuantity);
        detailCalories = findViewById(R.id.detailCalories);
        detailDietLabel = findViewById(R.id.detailDietLabel);
        detailHealthLabel = findViewById(R.id.detailHealthLabel);
        detailIngredients = findViewById(R.id.detailIngredients);
        detailTotalTime = findViewById(R.id.detailTotalTime);

        intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("Title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTitle)));

        getData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(checkIfSaved()){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.remove_menu,menu);
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.add_menu,menu);
        }
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
        } else if (item.getItemId() == R.id.removeRecipe){
            System.out.println("CLICKED");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Are you sure?")
                    .setMessage("Do you really want to delete your recipe?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteRecipe())
                    .setNegativeButton("No", (dialog, which) -> {
                return;
            });
                alertDialog.show();
        }
        return false;
    }

    private void getData(){

        int cookTimeTopMinimum = 60;
        int cookTimeBottomMinimum = 0;

        String imageURL = intent.getStringExtra("Image");
        Picasso.get().load(imageURL).fit().centerInside().into(detailImage);
        detailTitle.setText(intent.getStringExtra("Title"));
        detailQuantity.setText("Servings: " + intent.getIntExtra("Quantity", Integer.parseInt("0")));
        detailCalories.setText("Calories: " + intent.getIntExtra("Calories", Integer.parseInt("0")));
        detailDietLabel.setText("Diet Label: " + intent.getStringExtra("dietLabel"));
        detailHealthLabel.setText("Health Label: " + intent.getStringExtra("healthLabel"));
        detailIngredients.setText("Ingredients: " + intent.getStringExtra("ingredients"));
        int cookTime = intent.getIntExtra("totalTime",0);
        detailTotalTime.setText("Cook time: " + cookTime);

        if (cookTime > cookTimeTopMinimum){
            detailTotalTime.setText("Cook time: Over 60 minutes");
        } else if(cookTime <= cookTimeBottomMinimum) {
            detailTotalTime.setText("Cook time: Not measured");
        } else {
            detailTotalTime.setText("Cook time: " + cookTime + " minutes");
        }
    }

    private void deleteRecipe(){
        FoodActivity.mainDB.execSQL("DELETE FROM recipee WHERE title = '" + detailTitle.getText().toString() + "'");
        Toast.makeText(this,"Recipe deleted!",Toast.LENGTH_SHORT);
        onBackPressed();
    }

    private boolean checkIfSaved() {
        Cursor cursor = FoodActivity.mainDB.rawQuery("SELECT * FROM recipee", null);

        int titleIndex = cursor.getColumnIndex("title");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(titleIndex);
                    System.out.println("TITLE: " + title);
                    System.out.println("GETTEXT: " + detailTitle.getText().toString());
                    System.out.println("equals: " + title.trim().equals(detailTitle.getText().toString().trim()));
                    if (title.trim().equals(detailTitle.getText().toString().trim())) {
                        Toast.makeText(this, "Recipe found!", Toast.LENGTH_SHORT);
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        }
        return false;
    }

    void saveRecipe(){

        SQLiteDatabase mainDB = FoodActivity.mainDB;

        String image = intent.getStringExtra("Image");
        String title = intent.getStringExtra("Title");
        int cookTime = intent.getIntExtra("totalTime",0);
        int quantity = intent.getIntExtra("Quantity",0);
        int calories = intent.getIntExtra("Calories",0);
        String diet = intent.getStringExtra("dietLabel");
        String health = intent.getStringExtra("healthLabel");
        String ingredients = intent.getStringExtra("ingredients");
        mainDB.execSQL("INSERT INTO recipee (image, title , cookTime , quantity, calories, dietLabel , healthLabel , ingredients ) VALUES ('" + image.toString() + " ', '" +title + " ' , '" +cookTime +" ', '"+ quantity
                +"', '"+calories +" ','"+ diet +" ', '"+health +" ','"+ ingredients +" ')");
        Toast.makeText(this,"Recipe saved successfully!",Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}
