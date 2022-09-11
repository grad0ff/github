package com.github.pages;

import com.codeborne.selenide.SelenideElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class PublicProfileComponent {

    public final String ENDPOINT = "/settings/profile";
    public AvatarUploadingPopup uploadingPopup = new AvatarUploadingPopup();
    public SelenideElement flash = $("#js-flash-container");
    private SelenideElement editProfileBtn = $(".avatar-upload .octicon-pencil");

    public void clickByEditProfileBtn() {
        editProfileBtn.click();
    }

    public void uploadFile(File file) {
        $("#avatar_upload").uploadFile(file);
    }
}
