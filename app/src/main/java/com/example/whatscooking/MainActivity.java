package com.example.whatscooking;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*
* This app calls an API in order to generate all sorts of recipe lists.
* The recipes can be saved and stored in an SQLite DB and then retrieved from there in the "Favorites" section
*/

public class MainActivity extends AppCompatActivity {

    ImageView mainImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mainImageView = findViewById(R.id.mainImageView);

        //Generating a random background picture from an array
        setRandomBackground();
        //Moving to FoodActivity
        switchActivity();

    }

    public void switchActivity(){
        Timer timer = new Timer();
        int delay = 3000;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                intent.putExtra("newActivity", "");
                startActivity(intent);
            }
        },delay);
    }

    public void setRandomBackground(){
        Random picNum = new Random();

        Integer[] backgroundImages = new Integer[]{R.drawable.foodbone,R.drawable.foodbtwo,
                R.drawable.foodbfour,R.drawable.foodbfive,R.drawable.foodbsix,R.drawable.foodbseven};

        mainImageView.setBackgroundResource(backgroundImages[picNum.nextInt(6)]);
    }
}
