package com.example.whatscooking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
* This interface holds the method used to call the api
* q = query i.e q='fish'
* app_id = part of the information we need so our call will be valid
* app_key = the second part of the app information we need for the api call
*/

public interface JsonPlaceHolderApi {

    @GET("search")
    Call<Post> getPosts(
            @Query("q") String keyWord,
            @Query("app_id") String appId,
            @Query("app_key") String appKey
    );
}
