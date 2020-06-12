package com.example.whatscooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Post {
    @SerializedName("q")
    @Expose
    private String q;
    @SerializedName("hits")
    @Expose
    private ArrayList<Hit> hits;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public ArrayList<Hit> getHits() {
        return hits;
    }

    public void setHits(ArrayList<Hit> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "Post{" +
                "q='" + q + '\'' +
                ", hits=" + hits +
                '}';
    }
}
