package com.example.whatscooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* This fragment lets you search different recipes using keywords
*/

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private TextView noResultText;
    private String appID = "c2f84b80";
    private String appKey = "73d477d26f4d7944598ac6e6332c992a";
    private static final String BASE_URL = "https://api.edamam.com/";

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<GridItem> linearList;
    RecyclerView.LayoutManager layoutManager;
    private int fragmentNum;

    com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //Initializing the actionbar
        searchView = rootView.findViewById(R.id.searchBar);
        FoodActivity.bottomNavigationView.getMenu().getItem(1).setChecked(true);

        //fragmentNum = the number of the fragment - search fragment is the first and main fragment thus getting the number 2
        //fragmentNum is used in the adapter when deciding what type of item we want to have in a specific fragment - grid item, list item etc.
        fragmentNum = 2;

        //Recycler view initialization
        recyclerView = rootView.findViewById(R.id.recyclerViewSearch);
        layoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        linearList = new ArrayList<>();
        adapter = new Adapter(getActivity(), linearList, fragmentNum);

        noResultText = rootView.findViewById(R.id.noResultText);
        bottomNavigationView = rootView.findViewById(R.id.bot_nav);

        ((FoodActivity) getActivity()).getSupportActionBar().setTitle("Search recipes");

        //Making the whole searchview clickable and not only the magnifying glass
        searchView.setOnClickListener(v -> searchView.setIconified(false));

        //Generating a new recipe list with the query we've used in the search view
        searchView.setOnQueryTextListener(queryTextListener);

        //Removing the "No resulst found" text view when clicking on the search view
        searchView.setOnSearchClickListener(v -> {
            noResultText.setVisibility(View.GONE);
        });

        return rootView;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            String searchQuery = searchView.getQuery().toString();

            generateList(searchQuery);

            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    //Using the Retrofit library we make an api call and extract a couple of the fields of the response
    public void generateList(String searchResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Post> call = jsonPlaceHolderApi.getPosts(searchResult, appID, appKey);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String query = null;
                ArrayList<Hit> children = response.body().getHits();
                if(response.body() == null){
                    noResultText.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
                    return;
                }
                for (int i = 0; i < children.size(); i++) {

                    query = response.body().getQ();
                    String image = children.get(i).getRecipe().getImage();
                    String title = children.get(i).getRecipe().getLabel();
                    int  quantity = children.get(i).getRecipe().getYield().intValue();
                    int calories = children.get(i).getRecipe().getCalories().intValue();
                    List<String> dietLabel = children.get(i).getRecipe().getDietLabels();
                    String dietLabelTrim = dietLabel.toString().replaceAll("[\\[\\]\"]", "").trim().isEmpty() ? "None" : dietLabel.toString().replaceAll("[\\[\\]\"]", "").trim();
                    List<String> healthLabel = children.get(i).getRecipe().getHealthLabels();
                    String healthLabelTrim = healthLabel.toString().replaceAll("[\\[\\]\"]", "").trim().isEmpty() ? "None" : healthLabel.toString().replaceAll("[\\[\\]\"]", "").trim();
                    List<String> ingredients = children.get(i).getRecipe().getIngredientLines();
                    String ingredientsTrim = ingredients.toString().replaceAll("[\\[\\]\"]", "").trim().isEmpty() ? "None" : ingredients.toString().replaceAll("[\\[\\]\"]", "").trim();
                    int totalTime = children.get(i).getRecipe().getTotalTime().intValue();

                    linearList.add(new GridItem(image, title, quantity, calories,dietLabelTrim,healthLabelTrim,ingredientsTrim,totalTime));
                }
                adapter = new Adapter(getActivity(), linearList,fragmentNum);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }
}
