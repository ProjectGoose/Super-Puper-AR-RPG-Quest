package com.example.superpuper_ar_rpg.Network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.superpuper_ar_rpg.AppObjects.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.User;
import com.example.superpuper_ar_rpg.LoginActivity;
import com.example.superpuper_ar_rpg.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//singleton
public class NetworkService  {

    //Базовый URL сервера
    private static String URL = "http://super-puper-ar-android-quest.herokuapp.com";
    private Retrofit retrofit;
    private ServerAPI api;
    //Для тестирования plain text response
    private Retrofit retrofitTest;
    private ServerAPI apiTest;


    private static NetworkService instance = new NetworkService();
    private NetworkService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(client.build()).build();
        api = retrofit.create(ServerAPI.class);
        retrofitTest = new Retrofit.Builder().baseUrl(URL).addConverterFactory(ScalarsConverterFactory.create()).client(client.build()).build();
        apiTest = retrofitTest.create(ServerAPI.class);
    }
    public static NetworkService getInstance(){
        return instance;
    }


    public Boolean login(String login, String password){
        Boolean[] flag = {false};
        Call<LoginResponse> call = api.loginUser(login, password);
        call.enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response){
                if(response.isSuccessful()){
                    Log.d("NETWORK", "Response code is " + response.body() );
                    if(!response.body().getToken().equals("null")) {
                        flag[0] = true;
                        try {
                            //User.firstSetInstance(response.body().getToken(), response.body().getNickname(), response.body().getLevel());
                            User.firstSetInstance(response.body().getToken(), login, 1);
                        } catch (NullPointerException e){
                            Log.d("NETWORKInf", "Exception: " + e.getMessage());
                        }
                    } else {
                        Log.d("NETWORKInf", "Wrong login or password");
                    }
                } else {
                    Log.d("NETWORKInf", "Something gone wrong");
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t){
                Log.d("NETWORKErr", "Auth can not be sent " + t.getMessage());
            }
        });
        return flag[0];
    }

    //Test, plain text response
    public void loginTest(Callback<String> callback, String login, String password){
        Call<String> stringCall = apiTest.loginUserTest(login, password);
        stringCall.enqueue(callback);
    }


    public void sendLocation(LatLng location){
        Call<String> stringCall = api.sendLocation(User.getInstance().getToken(), location);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Log.d("NETWORKInf", "Location sent successfully");
                } else {
                    Log.d("NETWORKInf", "Location sent unsuccessfully");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("NETWORKErr", "Location can not be sent");
            }
        });
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
