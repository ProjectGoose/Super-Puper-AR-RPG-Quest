package com.example.superpuper_ar_rpg.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    /*@SerializedName("responseCode")
    @Expose
    private int responseCode;*/
    @SerializedName("token")
    @Expose
    private String token;
    /*@SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("level")
    @Expose
    private int level;*/

    /*public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }*/

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

   /* public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }*/

}
