package com.example.superpuper_ar_rpg.Network;

import android.util.Log;

import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestBriefDto;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestDetailsDto;
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

    //логин
    //Test, plain text response
    public void loginTest(Callback<String> callback, String login, String password){
        Call<String> stringCall = apiTest.loginUserTest(login, password);
        stringCall.enqueue(callback);
    }

    //Отправляем свое мостоположение на сервер
    public void sendLocation(String token, LatLng location){
        Call<String> stringCall = api.sendLocation("token=" + token, new SendLocationBody(location.latitude, location.longitude));
        ArrayList<Boolean> flag = new ArrayList<>();
        flag.add(false);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body() == "true") {
                        Log.d("NETWORKInf", "Location sent successfully");
                    }
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

    //запрашиваем квесты для карты
    public void requestQuestsInRange(String token, QuestsRequestBody questsRequestBody, Callback callback){
        Call<ArrayList<MapQuestBriefDto>> parsedQuests = api.requestQuestsInRange("token="+token, questsRequestBody.getTop(), questsRequestBody.getBottom(), questsRequestBody.getLeft(), questsRequestBody.getRight());
        parsedQuests.enqueue(callback);
    }

    public void requestQuestBody(String token, long id, Callback callback){
        Call<MapQuestDetailsDto> mapQuestCall = api.requestQuestBody("token="+token, id);
        mapQuestCall.enqueue(callback);
    }
}
