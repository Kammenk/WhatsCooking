package com.example.whatscooking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView mainImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mainImageView = findViewById(R.id.mainImageView);

        setRandomBackground();



    }

    public void setRandomBackground(){
        Random picNum = new Random();

        Integer[] backgroundImages = new Integer[]{R.drawable.foodbone,R.drawable.foodbtwo,
                R.drawable.foodbfour,R.drawable.foodbfive,R.drawable.foodbsix,R.drawable.foodbseven};

        mainImageView.setBackgroundResource(backgroundImages[picNum.nextInt(6)]);
    }
}
