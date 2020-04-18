package com.example.superpuper_ar_rpg;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//singleton
public class NetworkService  {

    public static String URL = "http://25.54.162.93:8080";
    Retrofit retrofit;
    PGServerAPI api;


    private static NetworkService instance = new NetworkService();
    private NetworkService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).client(client.build()).build();
        api = retrofit.create(PGServerAPI.class);
    }
    public static NetworkService getInstance(){
        return instance;
    }


    public Boolean requestLogin(LoginBody loginBody){
        Boolean[] flag = {false};
        Call<LoginResponse> call = api.loginUser(loginBody);
        call.enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response){
                if(response.isSuccessful()){
                    Log.d("NETWORK", "Response code is " + response.body() );
                    if(response.body().getResponseCode() == 1) {
                        flag[0] = true;
                        try {
                            //User.firstSetInstance(response.body().getToken(), response.body().getNickname(), response.body().getLevel());
                            User.firstSetInstance("12345","asdf", 1);
                        } catch (NullPointerException e){
                            Log.d("NETWORK", "Exception: " + e.getMessage());
                        }
                    } else {
                        Log.d("NETWORK_AUTH", "Wrong login or password");
                    }
                } else {
                    Log.d("NETWORK", "Something gone wrong");
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t){
                Log.d("NETWORK", "Sovsem pizda " + t.getMessage());
            }
        });
        return flag[0];
    }

    public ArrayList<MapQuest> requestQuests(QuestsBody questsBody){
        Call<ArrayList<MapQuest>> parsedQuests = api.updateQuests(questsBody);
        ArrayList<MapQuest> buffer = new ArrayList<>();
        parsedQuests.enqueue(new Callback<ArrayList<MapQuest>>() {
            @Override
            public void onResponse(Call<ArrayList<MapQuest>> call, Response<ArrayList<MapQuest>> response) {
                Log.d("NETWORK", "Server returned array with " + response.body().size() + " quests");
                buffer.addAll(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<MapQuest>> call, Throwable t) {

            }
        });
        return(buffer);
    }
}
