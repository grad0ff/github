package com.github.tests.combo;

import com.codeborne.selenide.WebDriverRunner;
import com.github.base.UiTestBase;
import com.github.pages.ProfilePage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.allMatch;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.github.spec.Spec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Tag("combo")
@Owner("grad0ff")
@Feature("Work with API and UI of site")
@DisplayName("API+UI tests")
public class ComboTests extends UiTestBase {

    ProfilePage page = new ProfilePage();

    @Test
    @Story("The user filters repositories")
    @Description("Checks that user can filter repositories")
    @DisplayName("Repositories filtering test")
    void filterRepoByVisibilityTest() {
        reqSpec.basePath("/user/repos");

        cleanRepoList(); // имитируем чистку данных
        step("Create some public and private repositories with API", () -> {
            int repoCount = 3;
            do {
                given()
                        .spec(reqSpec)
                        .body(Map.of(
                                "name", "repository" + new Random().nextInt(),
                                "private", repoCount < 3))
                        .log().all()
                        .when()
                        .post()
                        .then()
                        .spec(resSpec)
                        .statusCode(201);
                repoCount--;
            }
            while (repoCount > 0);
        });
        step("Open user's repositories page in browser", () -> {
            open(page.getRepoTabPath());
            WebDriverRunner.getWebDriver().manage().addCookie(cookie1);
            WebDriverRunner.getWebDriver().manage().addCookie(cookie2);
            refresh();
        });
        step("Filter repositories by private access", () -> {
            page.repoTab.clickByTypeOption().selectPrivateFilter();
        });
        step("Check that every repository contains 'Private' mark", () -> {
            page.repoTab.repoList.should(allMatch("all 'Private'", item -> item.getText().equals("Private")));
        });
    }

    private static void cleanRepoList() {
        String reqPath = String.format("/repos/%s/", config.getLogin());
        List<String> repoNames = given()
                .spec(reqSpec)
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().get("name");
        repoNames.forEach(name -> given()
                .spec(reqSpec)
                .basePath(reqPath)
                .delete(name)
                .then()
                .statusCode(204));
    }
}
