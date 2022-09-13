package com.github.pages.mobile;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumBy;

import static com.codeborne.selenide.Selenide.$;

public class Footer {

    private SelenideElement profileIcon = $(AppiumBy.id("com.github.android:id/profile"));

    public Footer tapByProfileIcon() {
        profileIcon.click();
        return this;
    }
}
