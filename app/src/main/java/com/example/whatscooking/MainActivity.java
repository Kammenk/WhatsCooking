package com.example.whatscooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView mainImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mainImageView = findViewById(R.id.mainImageView);

        setRandomBackground();
        switchActivity();




    }

    public void switchActivity(){
        Timer timer = new Timer();
        int delay = 5000;

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
