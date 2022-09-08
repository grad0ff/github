package com.github.tests.api;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

@Tag("combo")
@Owner("grad0ff")
@Feature("Работа с API и UI сайта")
@DisplayName("Тесты API+UI")
public class ComboTests {

    String cookie1 = "user_session=7v7ISa3SSnmCjyeBZHdSxjTydLeJct0KnvOxMCh7kWTbzVt3;";
    String cookie2 = "__Host-user_session_same_site=7v7ISa3SSnmCjyeBZHdSxjTydLeJct0KnvOxMCh7kWTbzVt3;";
    Cookie wdCookie1 = makeWdCookie(cookie1);
    Cookie wdCookie2 = makeWdCookie(cookie2);

    private Cookie makeWdCookie(String cookie) {
        String[] splitCookie = cookie.replace(";", "").split("=");
        return new Cookie(splitCookie[0], splitCookie[1]);
    }

    @Test
    @Story("Пользователь редактирует профиль")
    @Description("Проверяется редактирование данных в профиле пользователя")
    @DisplayName("Тест редактирования профиля")
    void editProfileTest() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String bio = faker.chuckNorris().fact();
        String company = faker.company().name();
        String location = faker.address().fullAddress();
        String blog = faker.internet().emailAddress();
        String twitter = faker.cat().name();
        String reqToken = "hgAUfKFjvmAMkAyCAEcOtPk07k7RHrIlnL0k9o8hQI4nmylY_TyZvcx9Psvvc12xoTWaKfnvCJ_aK4uN4QfmjQ";

        given()
                .header("x-requested-with", "XMLHttpRequest")
                .cookie(cookie1)
                .cookie(cookie2)
                .formParams(Map.of(
                        "_method", "put",
                        "authenticity_token", reqToken,
                        "user[profile_name]", name,
                        "user[profile_bio]", bio,
                        "user[profile_company]", company,
                        "user[profile_location]", location,
                        "user[profile_blog]", blog,
                        "user[profile_twitter_username]", twitter))
                .log().all()
                .when()
                .post("https://github.com/users/testGrad0ff")
                .then()
                .statusCode(200);

        open("https://github.com/testGrad0ff");
        WebDriverRunner.getWebDriver().manage().addCookie(wdCookie1);
        WebDriverRunner.getWebDriver().manage().addCookie(wdCookie2);
        refresh();

        $("[itemprop='name']").shouldHave(Condition.text(name));
        $("[data-bio-text]").shouldHave(Condition.text(bio));
        $("[itemprop='worksFor']").shouldHave(Condition.text(company));
        $("[itemprop='homeLocation']").shouldHave(Condition.text(location));
        $("[itemprop='url']").shouldHave(Condition.text(blog));
        $("[itemprop='twitter']").shouldHave(Condition.text(twitter));
    }
}
