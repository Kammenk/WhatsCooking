package com.example.whatscooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    private SearchViewAdapter searchViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = rootView.findViewById(R.id.searchBar);
        listView = rootView.findViewById(R.id.listViewSearch);
        searchViewArrImages = new ArrayList<>();
        searchViewArrTitles = new ArrayList<>();
        noResultText = rootView.findViewById(R.id.noResultText);


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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noResultText.setVisibility(View.GONE);
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
                ArrayList<Hit> children = response.body().getHits();
                if(children.isEmpty()){
                    noResultText.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < children.size(); i++) {

                    String image = children.get(i).getRecipe().getImage();
                    String title = children.get(i).getRecipe().getLabel();
                    double quantity = children.get(i).getRecipe().getYield();

                    searchViewArrImages.add(image);
                    searchViewArrTitles.add(title);
                }
                searchViewAdapter = new SearchViewAdapter();
                listView = getView().findViewById(R.id.listViewSearch);
                listView.setAdapter(searchViewAdapter);
                //adapter = new Adapter(getActivity(), gridList);
                //recyclerView.setAdapter(adapter);
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

            View view = getLayoutInflater().inflate(R.layout.searchview_list_item,null);

            ImageView searchViewImage = view.findViewById(R.id.searchViewImage);
            TextView searchViewTitle = view.findViewById(R.id.searchViewTitle);

            Picasso.get().load(searchViewArrImages.get(position)).fit().centerInside().into(searchViewImage);
            searchViewTitle.setText(searchViewArrTitles.get(position));

            return view;
        }
    }
}
