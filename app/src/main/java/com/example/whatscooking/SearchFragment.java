package com.example.whatscooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    TextView noResultText;
    private String appID = "c2f84b80";
    private String appKey = "73d477d26f4d7944598ac6e6332c992a";
    private static final String BASE_URL = "https://api.edamam.com/";
    private ArrayList<String> searchViewArrImages;
    private ArrayList<String> searchViewArrTitles;
    private ArrayList<Integer> searchViewArrQuantity;
    private ArrayList<Integer> searchViewArrCalories;
    private ArrayList<String> searchViewArrDietLabel;
    private ArrayList<String> searchViewArrHealthLabel;
    private ArrayList<String> searchViewArrIngredients;
    private ArrayList<Integer> searchViewArrTotalTime;
    private SearchViewAdapter searchViewAdapter;
    androidx.appcompat.widget.Toolbar homeToolbar;
    com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = rootView.findViewById(R.id.searchBar);
        listView = rootView.findViewById(R.id.listViewSearch);
        FoodActivity.bottomNavigationView.getMenu().getItem(1).setChecked(true);
        searchViewArrImages = new ArrayList<>();
        searchViewArrTitles = new ArrayList<>();
        searchViewArrQuantity = new ArrayList<>();
        searchViewArrCalories = new ArrayList<>();;
        searchViewArrDietLabel = new ArrayList<>();;
        searchViewArrHealthLabel = new ArrayList<>();;
        searchViewArrIngredients = new ArrayList<>();;
        searchViewArrTotalTime = new ArrayList<>();
        noResultText = rootView.findViewById(R.id.noResultText);
        bottomNavigationView = rootView.findViewById(R.id.bot_nav);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });

        searchView.setOnSearchClickListener(v -> {
            noResultText.setVisibility(View.GONE);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("Image",searchViewArrImages.get(position));
                intent.putExtra("Title",searchViewArrTitles.get(position));
                intent.putExtra("Quantity",searchViewArrQuantity.get(position));
                intent.putExtra("Calories",searchViewArrCalories.get(position));
                intent.putExtra("dietLabel",searchViewArrDietLabel.get(position));
                intent.putExtra("healthLabel",searchViewArrHealthLabel.get(position));
                intent.putExtra("ingredients",searchViewArrIngredients.get(position));
                intent.putExtra("totalTime", searchViewArrTotalTime.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }


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

                noResultText.setVisibility(View.GONE);
                assert response.body() != null;
                ArrayList<Hit> children = response.body().getHits();
                searchViewAdapter = new SearchViewAdapter();
                listView = getView().findViewById(R.id.listViewSearch);
                if(children.isEmpty()){
                    noResultText.setVisibility(View.VISIBLE);
                }
                searchViewArrImages.clear();
                searchViewArrTitles.clear();
                searchViewArrQuantity.clear();
                searchViewArrCalories.clear();
                searchViewArrDietLabel.clear();
                searchViewArrHealthLabel.clear();
                searchViewArrIngredients.clear();
                searchViewArrTotalTime.clear();
                searchViewAdapter.notifyDataSetChanged();
                for (int i = 0; i < children.size(); i++) {

                    String searchViewImage = children.get(i).getRecipe().getImage();
                    String searchViewTitle = children.get(i).getRecipe().getLabel();
                    int searchViewQuantity = children.get(i).getRecipe().getYield().intValue();
                    int searchViewCalories = children.get(i).getRecipe().getCalories().intValue();
                    List searchViewDietLabel = children.get(i).getRecipe().getDietLabels();
                    List searchViewHealthLabel = children.get(i).getRecipe().getHealthLabels();
                    List searchViewIngredients = children.get(i).getRecipe().getIngredientLines();
                    int searchViewTotalTime = children.get(i).getRecipe().getTotalTime().intValue();

                    searchViewArrImages.add(searchViewImage);
                    searchViewArrTitles.add(searchViewTitle);
                    searchViewArrQuantity.add(searchViewQuantity);
                    searchViewArrCalories.add(searchViewCalories);
                    searchViewArrDietLabel.add(searchViewDietLabel.toString().replace("[ ]"," "));
                    searchViewArrHealthLabel.add(searchViewHealthLabel.toString().replace("[ ]"," "));
                    searchViewArrIngredients.add(searchViewIngredients.toString().replace("[ ]"," "));
                    searchViewArrTotalTime.add(searchViewTotalTime);
                }
                listView.setAdapter(searchViewAdapter);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                noResultText.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }

    public class SearchViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return searchViewArrTitles.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int cookTimeTopMinimum = 60;
            int cookTimeBottomMinimum = 0;

            View view = getLayoutInflater().inflate(R.layout.searchview_list_item,null);

            ImageView searchViewImage = view.findViewById(R.id.searchViewImage);
            TextView searchViewTitle = view.findViewById(R.id.searchViewTitle);
            TextView searchViewServings = view.findViewById(R.id.searchViewServings);
            TextView searchViewCookTime = view.findViewById(R.id.searchViewCookTime);

            Picasso.get().load(searchViewArrImages.get(position)).fit().centerInside().into(searchViewImage);
            searchViewTitle.setText(searchViewArrTitles.get(position));
            searchViewServings.setText("Servings: " + searchViewArrQuantity.get(position));

            if (searchViewArrTotalTime.get(position) > cookTimeTopMinimum){
                searchViewCookTime.setText("Cook time: Over 60 minutes");
            } else if(searchViewArrTotalTime.get(position) <= cookTimeBottomMinimum) {
                searchViewCookTime.setText("Cook time: Not measured");
            } else {
                searchViewCookTime.setText("Cook time: " + searchViewArrTotalTime.get(position) + " minutes");
            }


            return view;
        }
    }
}
