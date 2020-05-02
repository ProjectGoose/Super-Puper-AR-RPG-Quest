package com.example.superpuper_ar_rpg.Network;

import com.example.superpuper_ar_rpg.AppObjects.MapQuest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServerAPI {


    //Делаем запрос без использования Json, кидаем в тело два стринга
    @FormUrlEncoded
    @POST("/user-controller/login")
    Call<LoginResponse> loginUser(@Field("login") String login, @Field("password") String password);
    //Запрос без джейсона с ответом в виде plain text
    @FormUrlEncoded
    @POST("/user-controller/login")
    Call<String> loginUserTest(@Field("login") String login, @Field("password") String password);
    @POST("/app/Quests")
    Call<ArrayList<MapQuest>> updateQuests(@Body QuestsBody questsBody);
    @POST("/user-controller/location-update")
    Call<String> sendLocation(@Header("token")String token, @Field("LatLng")LatLng location);

}

