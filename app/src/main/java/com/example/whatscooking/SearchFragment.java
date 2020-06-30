package com.example.whatscooking;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
    private ImageView noResultImage;
    private String appID = "c2f84b80";
    private String appKey = "73d477d26f4d7944598ac6e6332c992a";
    private static final String BASE_URL = "https://api.edamam.com/";

    TextView carouselItemOne;
    TextView carouselItemTwo;
    TextView carouselItemThree;
    TextView carouselItemFour;
    TextView carouselItemFive;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<GridItem> linearList;
    RecyclerView.LayoutManager layoutManager;
    private int fragmentNum;
    ArrayList<Hit> children;

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
        noResultImage = rootView.findViewById(R.id.imageSearch);
        bottomNavigationView = rootView.findViewById(R.id.bot_nav);

        ((FoodActivity) getActivity()).getSupportActionBar().show();
        Drawable d=getResources().getDrawable(R.drawable.toolbarpicthree);
        ((FoodActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(d);
        ((FoodActivity) getActivity()).getSupportActionBar().setTitle("Search recipes");

        //Making the whole searchview clickable and not only the magnifying glass
        searchView.setOnClickListener(v -> searchView.setIconified(false));

        //Generating a new recipe list with the query we've used in the search view
        searchView.setOnQueryTextListener(queryTextListener);

        //Removing the "No resulst found" text view when clicking on the search view
        searchView.setOnSearchClickListener(v -> {
            noResultText.setVisibility(View.GONE);
            noResultImage.setVisibility(View.GONE);
        });

        searchView.setOnCloseListener(() -> {
            if(children != null){
                return false;
            } else {
                noResultText.setVisibility(View.VISIBLE);
                noResultImage.setVisibility(View.VISIBLE);
                return false;
            }
        });

        carouselItemOne = rootView.findViewById(R.id.carouselTitleOne);
        carouselItemTwo = rootView.findViewById(R.id.carouselTitleTwo);
        carouselItemThree = rootView.findViewById(R.id.carouselTitleThree);
        carouselItemFour = rootView.findViewById(R.id.carouselTitleFour);
        carouselItemFive = rootView.findViewById(R.id.carouselTitleFive);

        carouselItemOne.setOnClickListener(carouselOnClickListener);
        carouselItemTwo.setOnClickListener(carouselOnClickListener);
        carouselItemThree.setOnClickListener(carouselOnClickListener);
        carouselItemFour.setOnClickListener(carouselOnClickListener);
        carouselItemFive.setOnClickListener(carouselOnClickListener);

        return rootView;
    }

    //Generates a list of recipes when an item in the carousel is clicked
    private TextView.OnClickListener carouselOnClickListener = v -> {
        noResultText.setVisibility(View.GONE);
        noResultImage.setVisibility(View.GONE);
        switch (v.getId()){
            case R.id.carouselTitleOne:
                generateList(carouselItemOne.getText().toString().toLowerCase());
                break;
            case R.id.carouselTitleTwo:
                generateList(carouselItemTwo.getText().toString().toLowerCase());
                break;
            case R.id.carouselTitleThree:
                generateList(carouselItemThree.getText().toString().toLowerCase());
                break;
            case R.id.carouselTitleFour:
                generateList(carouselItemFour.getText().toString().toLowerCase());
                break;
            case R.id.carouselTitleFive:
                generateList(carouselItemFive.getText().toString().toLowerCase());
                break;
            default:
                break;
        }
    };
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
                children = response.body().getHits();
                if(response.body() == null){
                    noResultText.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                linearList.clear();
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
