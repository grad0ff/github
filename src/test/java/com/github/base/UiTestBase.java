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
import static io.restassured.RestAssured.given;

/*
 * Базовый класс для UI тестов
 */
public class UiTestBase extends TestBase {

    protected static SelenoidConfig WdConfig = ConfigFactory.create(SelenoidConfig.class);
    protected static UiAuthConfig uiAuthConfig = ConfigFactory.create(UiAuthConfig.class);
    protected static Cookie cookie1;
    protected static Cookie cookie2;
    protected static boolean isRemoteDriver = false;

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = baseConfig.getBaseUrl();
        Configuration.browser = System.getProperty("browser", "chrome"); // TODO: 11.09.2022 реализовать в Jenkins
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080"); // TODO: 11.09.2022 реализовать в Jenkins
        if (System.getProperty("webHost", "local").equals("remote")) { // TODO: 06.09.2022
            isRemoteDriver = true;
            Configuration.browserCapabilities = getRemoteWDCapabilities();
            Configuration.remote = WdConfig.getServerUrl();
        }
        getAuthCookies();
    }

    @AfterEach
    void afterEach() {
        if (!webdriver().driver().browser().isFirefox()) Attach.addLogs();
        Attach.addPageSource();
        Attach.addScreenshot();
        if (isRemoteDriver) Attach.addVideo(getVideoUrl());
        Selenide.clearBrowserLocalStorage();
        Selenide.clearBrowserCookies();
        Selenide.closeWebDriver();
    }

    private static DesiredCapabilities getRemoteWDCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true));
        return capabilities;
    }

    private static void getAuthCookies() {
        String cookie1Name = "user_session";
        String cookie2Name = "__Host-user_session_same_site";
        String reqPath = "/session";

        Map<String, String> cookies = given()
                .baseUri(baseConfig.getBaseUrl())
                .basePath(reqPath)
                .contentType(uiAuthConfig.getContentType())
                .cookie(uiAuthConfig.getCookie())
                .formParams(Map.of(
                        "commit", "Sign+in",
                        "authenticity_token", uiAuthConfig.getAuthenticityToken(),
                        "login", credentialsConfig.getLogin(),
                        "password", credentialsConfig.getPassword()))
                .when()
                .post()
                .then()
                .statusCode(302)
                .extract().cookies();
        cookie1 = new Cookie(cookie1Name, cookies.get(cookie1Name));
        cookie2 = new Cookie(cookie2Name, cookies.get(cookie2Name));
    }

    private String getVideoUrl() {
        return String.format("%s/%s.mp4", WdConfig.getVideoPath(), Attach.getSessionId());
    }
}