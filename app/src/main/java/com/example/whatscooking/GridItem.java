package com.example.whatscooking;

/*
* GridItem holds the information we receive from the api call
*/

public class GridItem {

    private String mImage;
    private String mTitle;
    private int mQuantity;
    private int mCalories;
    private String mDietLabel;
    private String mHealthLabel;
    private String mIngredients;
    private int mTotalTime;

    public GridItem(String mImage, String mTitle, int mQuantity, int mCalories, String mDietLabel, String mHealthLabel, String mIngredients, int mTotalTime) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mQuantity = mQuantity;
        this.mCalories = mCalories;
        this.mDietLabel = mDietLabel;
        this.mHealthLabel = mHealthLabel;
        this.mIngredients = mIngredients;
        this.mTotalTime = mTotalTime;

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

    public String getmDietLabel() {
        return mDietLabel;
    }

    public String getmHealthLabel() {
        return mHealthLabel;
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public int getmTotalTime() {
        return mTotalTime;
    }
}
