package com.example.whatscooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodActivity extends AppCompatActivity {

    private TextView textViewRes;
    private TextView textViewRes2;
    private String appID = "c2f84b80";
    private String appKey = "73d477d26f4d7944598ac6e6332c992a";
    private static final String BASE_URL = "https://api.edamam.com/";

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<GridItem> gritList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        getSupportActionBar().hide();

       // textViewRes = findViewById(R.id.apiText);
        //textViewRes = findViewById(R.id.newText);
       //textViewRes2 = findViewById(R.id.newText2);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        setHomeFragment();

//        recyclerView = findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        gritList = new ArrayList<>();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        Call<Post> call = jsonPlaceHolderApi.getPosts("chicken",appID,appKey);
//
//        call.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                Log.i("INFO","Server response: " + response.toString());
//                Log.i("INFO","Received info " + response.body().toString());
//
//                ArrayList<Hit> children = response.body().getHits();
//                for(int i = 0; i < children.size(); i++){
//
//                    String image = children.get(i).getRecipe().getImage();
//                    String title = children.get(i).getRecipe().getLabel();
//                    double quantity = children.get(i).getRecipe().getYield();
//
//                    gritList.add(new GridItem(image,title,1,1));
//
//                    Log.i("INFO","RESPONSE: \n" +
//                            "Label: " + children.get(i).getRecipe().getLabel() + "\n" +
//                            "Image: " + children.get(i).getRecipe().getImage() + "\n" +
//                            "Yield: " + children.get(i).getRecipe().getYield() + "\n\n");
//
//                }
//
//                adapter = new Adapter(FoodActivity.this,gritList);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                Log.i("INFO","Something went wrong" + t.getMessage());
//                Toast.makeText(FoodActivity.this,"Something went wrong",Toast.LENGTH_SHORT);
//            }
//        });


    }

    private void setHomeFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_favorites:
                    selectedFragment = new FavoritesFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

}
