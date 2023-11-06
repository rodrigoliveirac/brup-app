package com.brupapp.rodcollab;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BrainUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrainUpApplication.class, args);
    }

    @Autowired
    Environment environment;

    @Bean
    public CommandLineRunner commandLineRunner() {

        return args -> {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.wordnik.com/v4/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            WordInterface service = retrofit.create(WordInterface.class);

            Call<ArrayList<Word>> callSync = service.randomWords(5, environment.getProperty("api.key"));

            ArrayList<Word> randomWords = new ArrayList<Word>();

            callSync.enqueue(new Callback<ArrayList<Word>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<Word>> call, @NotNull Response<ArrayList<Word>> response) {
                    ArrayList<Word> words = response.body();
                    assert words != null;
                    randomWords.addAll(words);
                    System.out.println(randomWords);

                    ArrayList<ItemQuestion> wordAnswers = new ArrayList<ItemQuestion>();

                    for (int i = 0; i < randomWords.size(); i++) {

                        Call<List<JsonObject>> callAsync = service.definitions(randomWords.get(i).getWord(), environment.getProperty("api.key"));

                        int position = i;

                        callAsync.enqueue(new Callback<List<JsonObject>>() {
                            @Override
                            public void onResponse(@NotNull Call<List<JsonObject>> call, @NotNull Response<List<JsonObject>> response) {
                                List<JsonObject> definition = response.body();
                                assert definition != null;
                                for (JsonObject jsonObject : definition) {
                                    String tip = jsonObject.get("text").toString();
                                    ItemQuestion wordAnswer = new ItemQuestion(randomWords.get(position).getWord(), tip);
                                    wordAnswers.add(wordAnswer);
                                    System.out.println(tip);
                                }
                                System.out.println(wordAnswers);
                            }

                            @Override
                            public void onFailure(Call<List<JsonObject>> call, Throwable throwable) {
                                System.out.println(throwable);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Word>> call, Throwable throwable) {
                    System.out.println(throwable);
                }
            });


        };
    }

}
