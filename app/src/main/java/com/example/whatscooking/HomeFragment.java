package com.example.whatscooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private String appID = "c2f84b80";
    private String appKey = "73d477d26f4d7944598ac6e6332c992a";
    private static final String BASE_URL = "https://api.edamam.com/";

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<GridItem> gridList;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    String yesterdaysDate;
    String lastQuery;
    androidx.appcompat.widget.Toolbar homeToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        homeToolbar = rootView.findViewById(R.id.toolbarHome);
        homeToolbar.setTitle(R.string.home_title);

        FoodActivity.bottomNavigationView.getMenu().getItem(0).setChecked(true);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager =  new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        gridList = new ArrayList<>();
        adapter = new Adapter(getActivity(), gridList);
        sharedPreferences = getActivity().getSharedPreferences("com.example.whatscooking", Context.MODE_PRIVATE);
        yesterdaysDate = sharedPreferences.getString("todaysDate","");
        lastQuery = sharedPreferences.getString("todaysQuery","");
        String todaysDate = getDate();



        if(todaysDate.compareTo(yesterdaysDate) != 0){
            generateList(foodGenerator());
        } else {
            generateList(lastQuery);
        }

        return rootView;
    }

    public String getDate(){
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(calendar);
        return formattedDate;
    }

    public void generateList(String searchResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        String currentDate = getDate();
        Call<Post> call = jsonPlaceHolderApi.getPosts(searchResult, appID, appKey);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String query = null;
                ArrayList<Hit> children = response.body().getHits();
                for (int i = 0; i < children.size(); i++) {

                    query = response.body().getQ();
                    String image = children.get(i).getRecipe().getImage();
                    String title = children.get(i).getRecipe().getLabel();
                    int  quantity = children.get(i).getRecipe().getYield().intValue();
                    int calories = children.get(i).getRecipe().getCalories().intValue();
                    List<String> dietLabel = children.get(i).getRecipe().getDietLabels();
                    List<String> healthLabel = children.get(i).getRecipe().getHealthLabels();
                    List<String> ingredients = children.get(i).getRecipe().getIngredientLines();


                    gridList.add(new GridItem(image, title, quantity, calories,dietLabel.toString(),healthLabel.toString(),ingredients.toString()));
                }
                sharedPreferences.edit().putString("todaysDate",currentDate).apply();
                sharedPreferences.edit().putString("todaysQuery",query).apply();
                adapter = new Adapter(getActivity(), gridList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }

    public String foodGenerator(){
        Random rand = new Random();
        String[] foodList = {"chicken","rice","bread","soup","pork","beef","turkey","ham","potato","salad"};

        return foodList[rand.nextInt(9)];
    }

}

