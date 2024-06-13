package com.example.vnpr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search")
    Call<SearchResult> searchImages(
            @Query("apikey") String apiKey,
            @Query("engine") String engine,
            @Query("q") String query
    );
}
