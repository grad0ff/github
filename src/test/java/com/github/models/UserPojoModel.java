package com.github.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserPojoModel {

    private String login;
    private String id;
    @SerializedName("html_url")
    private String htmlUrl;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;
    @SerializedName("twitter_username")
    private String twitterNick;
}
