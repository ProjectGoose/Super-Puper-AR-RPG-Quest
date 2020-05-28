package com.example.superpuper_ar_rpg.Network;

import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestBriefDto;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestDetailsDto;

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

    @POST("/user-controller/location-update")
    Call<String> sendLocation(@Header("Cookie")String token, @Body SendLocationBody locationBody);

    @FormUrlEncoded
    @POST("/quest/getAllInRange")
    Call<ArrayList<MapQuestBriefDto>> requestQuestsInRange(@Header("Cookie")String token, @Field("top")double top,
                                                           @Field("bottom")double bottom, @Field("left")double left, @Field("right")double right);

    @FormUrlEncoded
    @POST("/quest/getDeteils")
    Call<MapQuestDetailsDto> requestQuestBody(@Header("Cookie")String token, @Field("id")long id);
}

