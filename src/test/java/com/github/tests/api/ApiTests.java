package com.github.tests.api;

import com.github.base.TestBase;
import com.github.javafaker.Faker;
import com.github.models.RepositoryPojoModel;
import com.github.models.UserPojoModel;
import com.github.spec.BaseSpec;
import com.github.spec.Spec;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.spec.Spec.config;
import static com.github.spec.Spec.reqSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("API")
@Owner("grad0ff")
@Feature("Work with API of site")
@DisplayName("API tests")
public class ApiTests extends TestBase {

    @Test
    @DisplayName("User data matching test")
    void userDataMatchingTest() {
        AtomicReference<UserPojoModel> userReference = new AtomicReference<>();
        reqSpec.basePath("/users/");

        step("Get user profile data with API", () ->
                userReference.set(given()
                        .spec(Spec.reqSpec)
                        .when()
                        .get(config.login())
                        .then()
                        .spec(Spec.resSpec)
                        .statusCode(200)
                        .extract()
                        .as(UserPojoModel.class)));
        step("Check that user data matches to before entered", () -> {
            UserPojoModel userData = userReference.get();
            assertThat(userData.getId()).isNotNull();
            assertThat(userData.getLogin()).isEqualTo(config.login());
            assertThat(userData.getEmail()).isEqualTo(config.email());
            assertThat(userData.getHtmlUrl()).isEqualTo(
                    String.format("%s/%s", baseConfig.baseUrl(), config.login()));
        });
    }

    @Test
    @DisplayName("Repository creating test")
    void createNewRepoTest() {
        Faker faker = new Faker();
        RepositoryPojoModel repositoryModel = new RepositoryPojoModel()
                .name("repository" + new Random().nextInt())
                .privateType(true)
                .description(faker.chuckNorris().fact());
        AtomicReference<RepositoryPojoModel> repoReference = new AtomicReference<>();
        reqSpec.basePath("/user/repos");

        step("Create user repository and get it info with API", () ->
                repoReference.set(given()
                        .spec(Spec.reqSpec)
                        .body(repositoryModel)
                        .when()
                        .post()
                        .then()
                        .spec(Spec.resSpec)
                        .statusCode(201)
                        .extract().body().as(RepositoryPojoModel.class)));
        step("Check that current repository info matches to before entered ", () -> {
            RepositoryPojoModel repoInfo = repoReference.get();
            assertThat(repoInfo.id()).isNotNull();
            assertThat(repoInfo.name()).isEqualTo(repositoryModel.name());
            assertThat(repoInfo.fullName()).isEqualTo(
                    String.format("%s/%s", config.login(), repoInfo.name()));
            assertThat(repoInfo.privateType()).isEqualTo(repositoryModel.privateType());
            assertThat(repoInfo.htmlUrl()).isEqualTo(
                    String.format("%s/%s/%s", baseConfig.baseUrl(), config.login(), repoInfo.name()));
            assertThat(repoInfo.description()).isEqualTo(repositoryModel.description());
            assertThat(repoInfo.owner().login()).isEqualTo(config.login());
            assertThat(repoInfo.owner().htmlUrl()).isEqualTo(
                    String.format("%s/%s", baseConfig.baseUrl(), config.login()));
        });
        cleanRepoList(); // imitate DB cleaning
    }

    private static void cleanRepoList() {
        String reqPath = String.format("/repos/%s/", config.login());
        List<String> repoNames = given()
                .spec(BaseSpec.reqSpec)
                .noFilters()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().get("name");
        repoNames.forEach(name -> given()
                .spec(BaseSpec.reqSpec)
                .noFilters()
                .basePath(reqPath)
                .when()
                .delete(name)
                .then()
                .statusCode(204));
    }

    @ParameterizedTest
    @ValueSource(strings = {"@email.com", "mailboxemail.com", "mailbox@.com", "mailbox@emailcom", "mailbox@email.",
            "mailbox@123.com", "mailbox@!%^.com", "mailbox@email.456", "mailbox@email./*>", "", "*&^", "0", "123", "(*&)"})
    @DisplayName("Adding invalid email test")
    void addEmailNegativeTest(String email) {
        AtomicInteger codReference = new AtomicInteger();
        reqSpec.basePath("/user/emails");

        step("Try to add invalid user email with API and validate response", () ->
                codReference.set(given()
                        .spec(Spec.reqSpec)
                        .body(Map.of("emails", new String[]{email}))
                        .when()
                        .post()
                        .then()
                        .spec(Spec.resSpec)
                        .extract().statusCode()));
        int statusCode = codReference.get();
        step("Check that an invalid email address has not been added", () -> {
            if (statusCode == 201) removeEmail(email); // imitate DB cleaning
            assertThat(statusCode).isEqualTo(422);
        });
    }

    private static void removeEmail(String email) {
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
