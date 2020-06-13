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

public class FavoritesFragment extends Fragment {

    Button addBtn;
    RecyclerView favoriteRecyclerView;
    private Adapter adapter;
    private ArrayList<GridItem> linearList;
    RecyclerView.LayoutManager layoutManager;
    private int fragmentNum;

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

        FoodActivity.bottomNavigationView.getMenu().getItem(2).setChecked(true);

        addBtn = rootView.findViewById(R.id.favorite_add);
        favoriteRecyclerView = rootView.findViewById(R.id.recyclerViewFavorite);
        fragmentNum = 3;
        layoutManager =  new LinearLayoutManager(getActivity());
        favoriteRecyclerView.setLayoutManager(layoutManager);
        linearList = new ArrayList<>();
        updateList();

        addBtn.setOnClickListener(openRecipe);




        return rootView;
    }
    private void updateList() {
        Cursor cursor = FoodActivity.mainDB.rawQuery("SELECT * FROM recipee",null);



        int imageIndex = cursor.getColumnIndex("image");

        int  titleIndex = cursor.getColumnIndex("title");

        int  quantityIndex = cursor.getColumnIndex("quantity");
        int caloriesIndex = cursor.getColumnIndex("calories");
        int  dietLabelTrimIndex = cursor.getColumnIndex("dietLabel");
        int healthLabelTrimIndex = cursor.getColumnIndex("healthLabel");
        int ingredientsTrimIndex = cursor.getColumnIndex("ingredients");
        int totalTimeIndex = cursor.getColumnIndex("cookTime");

        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    String image = cursor.getString(imageIndex);

                    String title = cursor.getString(titleIndex);
                    int quantity = cursor.getInt(quantityIndex);//.replaceAll("\\D+","");
                    int calories = cursor.getInt(caloriesIndex);//.replaceAll("\\D+","");;
                    String dietLabelTrim =  cursor.getString(dietLabelTrimIndex);
                    String healthLabelTrim = cursor.getString(healthLabelTrimIndex);
                    String ingredientsTrim =  cursor.getString(ingredientsTrimIndex);
                    int totalTime = cursor.getInt(totalTimeIndex);//.replaceAll("\\D+","");;

//                    if(totalTime.isEmpty()){
//                        totalTime = "0";
//                    }

                    linearList.add(new GridItem(image, title, quantity, calories,dietLabelTrim,healthLabelTrim,ingredientsTrim,totalTime));
                } while (cursor.moveToNext());
            }
            adapter = new Adapter(getActivity(), linearList, fragmentNum);
            favoriteRecyclerView.setAdapter(adapter);
    }
}
