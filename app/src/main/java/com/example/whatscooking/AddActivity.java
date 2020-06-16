package com.example.whatscooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your recipe...");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void addRecipe(View view){

        EditText image = findViewById(R.id.recipeImage);
        EditText title = findViewById(R.id.recipeTitle);
        EditText cookTime = findViewById(R.id.recipeCookTime);
        EditText quantity = findViewById(R.id.recipeQuantity);
        EditText calories = findViewById(R.id.recipeCalories);
        EditText dietLabels = findViewById(R.id.recipeDietLabel);
        EditText healthLabels = findViewById(R.id.recipeHealthLabel);
        EditText ingredients = findViewById(R.id.recipeIngredients);

        String dbImage = image.getText().toString().isEmpty() ? "https://images.pexels.com/photos/708488/pexels-photo-708488.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" : image.getText().toString();
        String dbTitle = title.getText().toString();
        int dbCookTime = Integer.parseInt(cookTime.getText().toString());
        int dbQuantity = Integer.parseInt(quantity.getText().toString());
        int dbCalories = Integer.parseInt(calories.getText().toString());
        String dbDiet = dietLabels.getText().toString().isEmpty() ? "None" : dietLabels.getText().toString();
        String dbHealth = healthLabels.getText().toString().isEmpty() ? "None" : healthLabels.getText().toString();
        String dbIngredients = ingredients.getText().toString().isEmpty() ? "None" : ingredients.getText().toString();

        if (dbTitle.isEmpty()){
            Toast.makeText(this,"Please fill the necessary fields",Toast.LENGTH_LONG);
        } else {
            SQLiteDatabase mainDB = FoodActivity.mainDB;
            mainDB.execSQL("INSERT INTO recipee (image, title , cookTime , quantity, calories, dietLabel , healthLabel , ingredients ) VALUES ('" + dbImage.toString() + " ', '" + dbTitle + " ' , '" + dbCookTime +" ', '"+ dbQuantity
                    +"', '" + dbCalories +" ','"+ dbDiet +" ', '" + dbHealth +" ','" + dbIngredients +" ')");
            Toast.makeText(this,"Recipe saved successfully!",Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }
}
