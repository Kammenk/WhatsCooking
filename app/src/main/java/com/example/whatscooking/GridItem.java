package com.example.whatscooking;

public class GridItem {

    private String mImage;
    private String mTitle;
    private int mQuantity;
    private int mCalories;

    public GridItem(String mImage, String mTitle, int mQuantity, int mCalories) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mQuantity = mQuantity;
        this.mCalories = mCalories;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public int getmCalories() {
        return mCalories;
    }
}
