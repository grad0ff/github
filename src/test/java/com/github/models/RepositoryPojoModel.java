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

    @Data
    @Accessors(fluent = true)
    public static class RepoOwner {

        private String login;
        @SerializedName("html_url")
        private String htmlUrl;
    }
}
