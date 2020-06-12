package com.example.whatscooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("yield")
    @Expose
    private Double yield;
    @SerializedName("dietLabels")
    @Expose
    private List<String> dietLabels = null;
    @SerializedName("healthLabels")
    @Expose
    private List<String> healthLabels = null;
    @SerializedName("cautions")
    @Expose
    private List<String> cautions = null;
    @SerializedName("ingredientLines")
    @Expose
    private List<String> ingredientLines = null;
    @SerializedName("calories")
    @Expose
    private Double calories;
    @SerializedName("totalTime")
    @Expose
    private Double totalTime;

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public Double getYield() {
        return yield;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public List<String> getCautions() {
        return cautions;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "label='" + label + '\'' +
                ", image='" + image + '\'' +
                ", yield=" + yield +
                ", dietLabels=" + dietLabels +
                ", healthLabels=" + healthLabels +
                ", cautions=" + cautions +
                ", ingredientLines=" + ingredientLines +
                '}';
    }
}
