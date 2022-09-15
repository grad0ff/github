package com.github.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.github.config.SelenoidConfig;
import com.github.config.UiAuthConfig;
import com.github.utils.Attach;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.webdriver;

public class UiTestBase extends TestBase {

    protected static SelenoidConfig WdConfig = ConfigFactory.create(SelenoidConfig.class);
    protected static UiAuthConfig uiAuthConfig = ConfigFactory.create(UiAuthConfig.class);
    protected static Cookie userCookie = new Cookie("user_session", uiAuthConfig.cookie());
    protected static Cookie hostCookie = new Cookie("__Host-user_session_same_site", uiAuthConfig.cookie());
    protected static boolean isRemoteDriver = false;

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = baseConfig.baseUrl();
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        if (System.getProperty("host", "remote").equals("remote")) {
            isRemoteDriver = true;
            Configuration.browserCapabilities = getRemoteWDCapabilities();
            Configuration.remote = WdConfig.serverUrl();
        }
    }

    @AfterEach
    void afterEach() {
        if (!webdriver().driver().browser().isFirefox()) Attach.addLogs();
        Attach.addPageSource();
        Attach.addScreenshot();
        if (isRemoteDriver) Attach.addVideo(getVideoUrl());
        Selenide.closeWebDriver();
    }

    private static DesiredCapabilities getRemoteWDCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true));

        return capabilities;
    }

    private String getVideoUrl() {

        return String.format("%s/%s.mp4", WdConfig.videoPath(), Attach.getSessionId());
    }
}
