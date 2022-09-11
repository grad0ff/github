package com.github.tests.api;

import com.github.base.TestBase;
import com.github.javafaker.Faker;
import com.github.models.RepositoryPojoModel;
import com.github.models.UserPojoModel;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.github.spec.Spec.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Owner("grad0ff")
@Feature("Work with API of site")
@DisplayName("API tests")
public class ApiTests extends TestBase {

    @Test
    @Description("Checks that user data matches required")
    @DisplayName("User data matching test")
    void userDataMatchingTest() {
        reqSpec.basePath("/users/");

        UserPojoModel user = given()
                .spec(reqSpec)
                .log().all()
                .when()
                .get(config.getLogin())
                .then()
                .statusCode(200)
                .spec(resSpec)
                .extract()
                .as(UserPojoModel.class);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getLogin()).isEqualTo(config.getLogin());
        assertThat(user.getEmail()).isEqualTo(config.getEmail());
        assertThat(user.getHtmlUrl()).isEqualTo(
                String.format("%s/%s", baseConfig.getBaseUrl(), config.getLogin()));
    }


    @Test
    @Description("Checks that repository creating is possible")
    @DisplayName("Repository creating test")
    void createNewRepoTest() {
        reqSpec.basePath("/user/repos");
        Faker faker = new Faker();
        RepositoryPojoModel repositoryModel = new RepositoryPojoModel()
                .name("repository" + new Random().nextInt())
                .privateType(true)
                .description(faker.chuckNorris().fact());

        RepositoryPojoModel repository = given()
                .spec(reqSpec)
                .log().all()
                .body(repositoryModel)
                .when()
                .post()
                .then()
                .statusCode(201)
                .spec(resSpec)
                .extract().as(RepositoryPojoModel.class);

        assertThat(repository.id()).isNotNull();
        assertThat(repository.name()).isEqualTo(repositoryModel.name());
//        assertThat(repository.owner()).isEqualTo(repositoryModel.name()); // TODO: 12.09.2022 вложенный json
        assertThat(repository.fullName()).isEqualTo(
                String.format("%s/%s", config.getLogin(), repository.name()));
        assertThat(repository.privateType()).isEqualTo(repositoryModel.privateType());
        assertThat(repository.htmlUrl()).isEqualTo(
                String.format("%s/%s/%s", baseConfig.getBaseUrl(), config.getLogin(), repository.name()));
        assertThat(repository.description()).isEqualTo(repositoryModel.description());

    }
}
