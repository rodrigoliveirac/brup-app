package com.brupapp.rodcollab;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.ArrayList;
import java.util.List;

public interface WordInterface {

    @Autowired
    @GET("words.json/randomWords")
    public Call<ArrayList<Word>> randomWords(
            @Query("limit") int number,
            @Query("api_key") @Value("${api.key}") String apiKey);

    @GET("word.json/{word}/definitions?limit=1")
    public Call<List<JsonObject>> definitions(@Path("word") String word, @Query("api_key") String api_key);

}
