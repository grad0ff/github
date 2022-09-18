package com.github.tests.combo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.github.base.UiTestBase;
import com.github.javafaker.Faker;
import com.github.pages.EmailsComponent;
import com.github.pages.ProfilePage;
import com.github.spec.BaseSpec;
import com.github.spec.Spec;
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
import static com.github.spec.Spec.config;
import static com.github.spec.Spec.reqSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

    @Tag("COMBINED")
    @Owner("grad0ff")
    @Feature("Work with API and UI of site")
    @DisplayName("API and UI tests")
    public class ComboTests extends UiTestBase {

        @Test
        @Story("The user filters repositories")
        @DisplayName("Repositories filtering test")
        void filterRepoByVisibilityTest() {
            ProfilePage page = new ProfilePage();
            reqSpec.basePath("/user/repos");

            step("Create 1 public and 2 private repositories with API", () ->
                    createRepositories(1, 2));
            step("Open user's repositories page in browser", () -> {
                open(page.getRepoTabPath());
                WebDriverRunner.getWebDriver().manage().addCookie(userCookie);
                WebDriverRunner.getWebDriver().manage().addCookie(hostCookie);
                refresh();
            });
            step("Filter repositories by private access", () ->
                    page.repoTab.clickByTypeOption().selectPrivateFilter());
            step("Check that every repository contains 'Private' mark", () ->
                page.repoTab.repoList.should(allMatch("all 'Private'", item -> item.getText().equals("Private"))));
            cleanRepoList(); // imitate DB cleaning
        }

        private void createRepositories(int publicCount, int privateCount) {
            int repoCount = publicCount + privateCount;
            while (repoCount > 0) {
                given()
                        .spec(Spec.reqSpec)
                        .body(Map.of(
                                "name", "repository" + new Random().nextInt(),
                                "private", repoCount <= privateCount))
                        .when()
                        .post()
                        .then()
                        .spec(Spec.resSpec)
                        .statusCode(201);
                repoCount--;
            }
        }

    private void cleanRepoList() {
        String reqPath = String.format("/repos/%s/", config.login());
        List<String> repoNames = given()
                .spec(BaseSpec.reqSpec)
                .noFilters()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("name");
        repoNames.forEach(name -> given()
                .spec(BaseSpec.reqSpec)
                .noFilters()
                .basePath(reqPath)
                .when()
                .delete(name)
                .then()
                .statusCode(204));
    }

    @Test
    @Story("The user's email is visible in settings page")
    @DisplayName("Email visible test")
    void emailVisibleTest() {
        EmailsComponent emails = new EmailsComponent();
        Faker faker = new Faker();
        reqSpec.basePath("/user/emails");
        String newEmail = faker.internet().emailAddress();

        step("Add new user email with API", () -> {
            given()
                    .spec(Spec.reqSpec)
                    .body(Map.of("emails", new String[]{newEmail}))
                    .when()
                    .post()
                    .then()
                    .spec(Spec.resSpec)
                    .statusCode(201);
        });
        step("Open 'Emails' tab in user's profile settings page in browser", () -> {
            open(emails.ENDPOINT);
            WebDriverRunner.getWebDriver().manage().addCookie(userCookie);
            WebDriverRunner.getWebDriver().manage().addCookie(hostCookie);
            refresh();
        });
        step("Check that new email is visible in emails list", () -> {
            emails.newEmail.shouldHave(Condition.text(newEmail));
        });
        removeEmail(newEmail); // imitate DB cleaning
    }

    private void removeEmail(String email) {
        given()
                .spec(BaseSpec.reqSpec)
                .noFilters()
                .body(Map.of("emails", new String[]{email}))
                .when()
                .delete()
                .then()
                .statusCode(204);
    }
}
