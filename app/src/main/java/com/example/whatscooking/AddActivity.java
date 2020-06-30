package com.example.whatscooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
* AddActivity is where we can add a custom recipe and save it
*/

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Initializing actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your recipe...");

    }

    //If the back icon is clicked we go to the previous activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    //Taking all the information from the field and saving it in a new recipe
    //If some of the field are empty we add sample data
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
        int dbCookTime = Integer.parseInt(cookTime.getText().toString().isEmpty() ? "0" : cookTime.getText().toString()) ;
        int dbQuantity = Integer.parseInt(quantity.getText().toString().isEmpty() ? "0" : cookTime.getText().toString());
        int dbCalories = Integer.parseInt(calories.getText().toString().isEmpty() ? "0" : cookTime.getText().toString());
        String dbDiet = dietLabels.getText().toString().isEmpty() ? "None" : dietLabels.getText().toString();
        String dbHealth = healthLabels.getText().toString().isEmpty() ? "None" : healthLabels.getText().toString();
        String dbIngredients = ingredients.getText().toString().isEmpty() ? "None" : ingredients.getText().toString();

        if (dbTitle.isEmpty() || dbCookTime == 0 || dbQuantity == 0){
            Toast.makeText(this,"Please fill the necessary fields *",Toast.LENGTH_LONG).show();
            return;
        }
        if (dbCalories == 0){
            dbCalories = 200;
        }
        SQLiteDatabase mainDB = FoodActivity.mainDB;
        mainDB.execSQL("INSERT INTO recipee (image, title , cookTime , quantity, calories, dietLabel , healthLabel , ingredients ) VALUES ('" + dbImage.toString() + " ', '" + dbTitle + " ' , '" + dbCookTime +" ', '"+ dbQuantity
                +"', '" + dbCalories +" ','"+ dbDiet +" ', '" + dbHealth +" ','" + dbIngredients +" ')");
        Toast.makeText(this,"Recipe saved successfully!",Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}
