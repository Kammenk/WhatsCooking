package com.example.whatscooking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("search")
    Call<Post> getPosts(
            @Query("q") String keyWord,
            @Query("app_id") String appId,
            @Query("app_key") String appKey
    );
}
