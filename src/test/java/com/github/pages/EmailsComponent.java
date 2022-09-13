package com.github.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class EmailsComponent {

    public final String ENDPOINT = "/settings/emails";
    public SelenideElement newEmail = $$("#settings-emails .Box-row").get(1);
}
