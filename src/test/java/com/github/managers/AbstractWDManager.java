package com.github.managers;

import com.codeborne.selenide.Selenide;
import com.github.utils.Attach;
import io.qameta.allure.selenide.AllureSelenide;

import static com.codeborne.selenide.logevents.SelenideLogger.addListener;

public abstract class AbstractWDManager {

    public void configureBeforeAll() {
    }

    public void configureBeforeEach() {
        addListener("AllureSelenide", new AllureSelenide());
    }

    public void configureAfterEach() {
        Attach.addPageSource();
        Attach.addScreenshot();
        Selenide.closeWebDriver();
    }

    public void configureAfterAll() {
    }
}
