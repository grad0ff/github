package com.github.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class AvatarUploadingPopup {

    public SelenideElement modal = $("#avatar-crop-form");

    public void setAvatar() {
        $("#avatar-crop-form .btn").click();
    }
}
