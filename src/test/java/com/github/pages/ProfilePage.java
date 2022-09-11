package com.github.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    public final String ENDPOINT = "/testGrad0ff";
    public RepositoriesComponent repoTab = new RepositoriesComponent();
    public SelenideElement sideBarForm = $(".Layout-sidebar .flex-column form");
    public SelenideElement nameField = $("#user_profile_name");
    public SelenideElement bioField = $("#user_profile_bio");
    public SelenideElement companyField = $("[name='user[profile_company]']");
    public SelenideElement loactionField = $("[name='user[profile_location]']");
    public SelenideElement blogField = $("[name='user[profile_blog]']");
    public SelenideElement twitterField = $("[name='user[profile_twitter_username]']");
    private SelenideElement editProfileBtn = $(".Layout-sidebar [name='button']");

    public String getRepoTabPath() {
        return ENDPOINT + repoTab.PATH;
    }

    public void clickByEditProfileBtn() {
        editProfileBtn.click();
    }

    public ProfilePage setName(String value) {
        nameField.setValue(value);
        return this;
    }

    public ProfilePage setBio(String value) {
        bioField.setValue(value);
        return this;
    }

    public ProfilePage setCompany(String value) {
        companyField.setValue(value);
        return this;
    }

    public ProfilePage setLocation(String value) {
        loactionField.setValue(value);
        return this;
    }

    public ProfilePage setBlog(String value) {
        blogField.setValue(value);
        return this;
    }

    public ProfilePage setTwitter(String value) {
        twitterField.setValue(value);
        return this;
    }

    public ProfilePage saveChanges() {
        $(".Layout-sidebar .flex-column [type='submit']").click();
        return this;
    }
}
