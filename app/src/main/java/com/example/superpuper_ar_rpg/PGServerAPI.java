package com.example.superpuper_ar_rpg;

import android.media.session.MediaSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PGServerAPI {

    @POST("/user/byId")
    Call<LoginResponse> loginUser(@Body LoginBody loginBody);
    @POST("/app/Quests")
    Call<ArrayList<MapQuest>> updateQuests(@Body QuestsBody questsBody);

}

