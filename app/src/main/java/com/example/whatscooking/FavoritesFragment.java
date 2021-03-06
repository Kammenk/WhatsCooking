package com.example.whatscooking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
* This fragment is used to store the recipes which have been marked as favorite
* The favorite recipes are extracted from the DB and populated in the fragment
*/

public class FavoritesFragment extends Fragment {

    Button addBtn;
    RecyclerView favoriteRecyclerView;
    private Adapter adapter;
    private ArrayList<GridItem> linearList;
    RecyclerView.LayoutManager layoutManager;
    private int fragmentNum;

    //Takes you to the activity where you can add your custom recipe
    private View.OnClickListener openRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),AddActivity.class);
            startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        //Initializing the actionbar
        FoodActivity.bottomNavigationView.getMenu().getItem(2).setChecked(true);
        ((FoodActivity) getActivity()).getSupportActionBar().show();
        ((FoodActivity) getActivity()).getSupportActionBar().setTitle("Favorite recipes");

        addBtn = rootView.findViewById(R.id.favorite_add);
        favoriteRecyclerView = rootView.findViewById(R.id.recyclerViewFavorite);

        //fragmentNum = the number of the fragment - favorite fragment is the third fragment thus getting the number 3
        //fragmentNum is used in the adapter when deciding what type of item we want to have in a specific fragment - grid item, list item etc.
        fragmentNum = 3;

        layoutManager =  new LinearLayoutManager(getActivity());
        favoriteRecyclerView.setLayoutManager(layoutManager);
        linearList = new ArrayList<>();

        addBtn.setOnClickListener(openRecipe);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Gets the recipes from the DB
        updateList();
    }

    //Gets all results from the DB and puts them in a new linear List
    private void updateList() {
        Cursor cursor = FoodActivity.mainDB.rawQuery("SELECT * FROM recipee", null);

        int imageIndex = cursor.getColumnIndex("image");
        int titleIndex = cursor.getColumnIndex("title");
        int quantityIndex = cursor.getColumnIndex("quantity");
        int caloriesIndex = cursor.getColumnIndex("calories");
        int dietLabelTrimIndex = cursor.getColumnIndex("dietLabel");
        int healthLabelTrimIndex = cursor.getColumnIndex("healthLabel");
        int ingredientsTrimIndex = cursor.getColumnIndex("ingredients");
        int totalTimeIndex = cursor.getColumnIndex("cookTime");

        linearList.clear();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String image = cursor.getString(imageIndex);
                    String title = cursor.getString(titleIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    int calories = cursor.getInt(caloriesIndex);
                    String dietLabelTrim = cursor.getString(dietLabelTrimIndex);
                    String healthLabelTrim = cursor.getString(healthLabelTrimIndex);
                    String ingredientsTrim = cursor.getString(ingredientsTrimIndex);
                    int totalTime = cursor.getInt(totalTimeIndex);

                    linearList.add(new GridItem(image, title, quantity, calories, dietLabelTrim, healthLabelTrim, ingredientsTrim, totalTime));
                } while (cursor.moveToNext());
            }
            adapter = new Adapter(getActivity(), linearList, fragmentNum);
            favoriteRecyclerView.setAdapter(adapter);
        }
    }
}
