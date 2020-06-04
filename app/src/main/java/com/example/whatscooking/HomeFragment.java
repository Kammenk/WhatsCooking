package com.example.whatscooking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager =  new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false); //new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        gridList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Post> call = jsonPlaceHolderApi.getPosts(foodGenerator(), appID, appKey);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                ArrayList<Hit> children = response.body().getHits();
                for (int i = 0; i < children.size(); i++) {

                    String image = children.get(i).getRecipe().getImage();
                    String title = children.get(i).getRecipe().getLabel();
                    double quantity = children.get(i).getRecipe().getYield();

                    gridList.add(new GridItem(image, title, 1, 1));
                }
                adapter = new Adapter(getActivity(), gridList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });

        return rootView;
    }

    public String foodGenerator(){
        Random rand = new Random();
        String[] foodList = {"chicken","rice","bread","soup","pork","beef","turkey","ham","potato","salad"};

        return foodList[rand.nextInt(9)];
    }
}

