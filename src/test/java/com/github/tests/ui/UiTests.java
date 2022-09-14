package com.github.tests.ui;

import com.codeborne.selenide.WebDriverRunner;
import com.github.base.UiTestBase;
import com.github.javafaker.Faker;
import com.github.pages.ProfilePage;
import com.github.pages.PublicProfileComponent;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static io.qameta.allure.Allure.step;

@Tag("UI")
@Owner("grad0ff")
@Feature("Work with UI of site")
@DisplayName("UI tests")
public class UiTests extends UiTestBase {

    @Test
    @Story("User edits profile")
    @Description("Checks that user can edit own profile data")
    @DisplayName("Profile editing test")
    void editProfileTest() {
        ProfilePage page = new ProfilePage();
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String bio = faker.book().title();
        String company = faker.company().name();
        String location = faker.address().fullAddress();
        String blog = faker.internet().emailAddress();
        String twitter = faker.cat().name();

        step("Open user's profile page in browser", () -> {
            open(page.ENDPOINT);
            WebDriverRunner.getWebDriver().manage().addCookie(sessionCookie);
            refresh();
        });
        step("Click by 'Edit profile' button to left of page", () -> {
            page.clickByEditProfileBtn();
            page.sideBarForm.shouldBe(visible);
        });
        step("Fill in the fields with new data", () -> {
            page.setName(name)
                    .setBio(bio)
                    .setCompany(company)
                    .setLocation(location)
                    .setBlog(blog)
                    .setTwitter(twitter);
        });
        step("Save changes", page::saveChanges);
        step("Check that user's data has been updated", () -> {
            page.nameField.shouldHave(value(name));
            page.bioField.shouldHave(value(bio));
            page.companyField.shouldHave(value(company));
            page.loactionField.shouldHave(value(location));
            page.blogField.shouldHave(value(blog));
            page.twitterField.shouldHave(value(twitter));
        });
    }

    @Test
    @Story("User uploads avatar")
    @Description("Checks that user can upload avatar to profile")
    @DisplayName("Profile avatar uploading test")
    void uploadPhotoTest() {
        PublicProfileComponent profile = new PublicProfileComponent();
        int randInt = new Random().nextInt(4) + 1;
        File file = new File(String.format("src/test/resources/img/avatar%s.jpg", randInt));
        String assertText = "Your profile picture has been updated. " +
                "It may take a few moments to update across the site.";

        step("Open 'Public profile' tab in user's profile settings page in browser", () -> {
            open("");
            WebDriverRunner.getWebDriver().manage().addCookie(sessionCookie);
            open(profile.ENDPOINT);
        });
        step("Click by 'Edit' button to right of page", profile::clickByEditProfileBtn);
        step("Set new Avatar", () -> {
            profile.uploadFile(file);
            profile.uploadingPopup.modal.shouldBe(visible);
            profile.uploadingPopup.setAvatar();
        });
        step("Check that avatar uploading is successful", () ->
                profile.flash.shouldBe(visible).shouldHave(text(assertText)));
    }
}
