package com.example.whatscooking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

/*
* This fragment is in charge of a grid list of recipes that is generated each day
* A random list of recipes is generated using an array of keywords
*/

public class HomeFragment extends Fragment {

    private String appID = "c2f84b80";
    private String appKey = "73d477d26f4d7944598ac6e6332c992a";
    private static final String BASE_URL = "https://api.edamam.com/";

    private RecyclerView recyclerView;
    private Adapter adapter;
    ArrayList<GridItem> gridList;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private String yesterdaysDate;
    private String lastQuery;
    private androidx.appcompat.widget.Toolbar homeToolbar;
    private int fragmentNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Initializing the actionbar
        FoodActivity.bottomNavigationView.getMenu().getItem(0).setChecked(true);
        ((FoodActivity) getActivity()).getSupportActionBar().hide();//.setTitle(R.string.home_title);

        //fragmentNum = the number of the fragment - home fragment is the first and main fragment thus getting the number 1
        //fragmentNum is used in the adapter when deciding what type of item we want to have in a specific fragment - grid item, list item etc.
        fragmentNum = 1;

        //Recycler view initialization
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager =  new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        gridList = new ArrayList<>();
        adapter = new Adapter(getActivity(), gridList, fragmentNum);

        //Getting the data stored in the shared preferences
        //yesterdaysDate = takes the date of today when generating a list - if todaysDate is not equal to yesterdaysDate this means a day has passed since last time we generated a list.
        // This means we need to generate a new list
        // If yesterdaysDate and todaysDate match this means that we have already generated a new recipe list today so we don't need to generate a new one with a whole new key word
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

    //Gets today's date
    public String getDate(){
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(calendar);
        return formattedDate;
    }

    //Using the Retrofit library we make an api call and extract a couple of the fields of the response
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
                    String title = children.get(i).getRecipe().getLabel().trim();
                    int  quantity = children.get(i).getRecipe().getYield().intValue();
                    int calories = children.get(i).getRecipe().getCalories().intValue();
                    List<String> dietLabel = children.get(i).getRecipe().getDietLabels();
                    String dietLabelTrim = dietLabel.toString().replaceAll("[\\[\\]\"]", "").trim().isEmpty() ? "None" : dietLabel.toString().replaceAll("[\\[\\]\"]", "").trim();
                    List<String> healthLabel = children.get(i).getRecipe().getHealthLabels();
                    String healthLabelTrim = healthLabel.toString().replaceAll("[\\[\\]\"]", "").trim().isEmpty() ? "None" : healthLabel.toString().replaceAll("[\\[\\]\"]", "").trim();
                    List<String> ingredients = children.get(i).getRecipe().getIngredientLines();
                    String ingredientsTrim = ingredients.toString().replaceAll("[\\[\\]\"]", "").trim().isEmpty() ? "None" : ingredients.toString().replaceAll("[\\[\\]\"]", "").trim();
                    int totalTime = children.get(i).getRecipe().getTotalTime().intValue();

                    gridList.add(new GridItem(image, title, quantity, calories,dietLabelTrim,healthLabelTrim,ingredientsTrim,totalTime));
                }
                sharedPreferences.edit().putString("todaysDate",currentDate).apply();
                sharedPreferences.edit().putString("todaysQuery",query).apply();
                adapter = new Adapter(getActivity(), gridList,fragmentNum);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }
    //Returns a random string/keyword that is used in the api call
    public String foodGenerator(){
        Random rand = new Random();
        String[] foodList = {"chicken","rice","bread","soup","pork","beef","turkey","ham","potato","salad","crab","pizza","pasta","lasagna"};

        return foodList[rand.nextInt(14)];
    }
}

