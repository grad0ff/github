package com.github.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class RepositoryPojoModel {

    private String id;
    private String name;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("private")
    private boolean privateType;
    @SerializedName("html_url")
    private String htmlUrl;
    private String description;
    private RepoOwner owner;

    static class RepoOwner {

        private String login;
        @SerializedName("html_url")
        private String htmlUrl;
    }
}
//"id": 1296269,
//  "name": "Hello-World",
//  "full_name": "octocat/Hello-World",
//  "owner": {
//    "login": "octocat",
//    "html_url": "https://github.com/octocat",
//  },
//  "private": false,
//  "html_url": "https://github.com/octocat/Hello-World",
//  "description": "This your first repo!",
//}