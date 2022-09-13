package com.github.tests.mobile;

import com.github.base.MobileTestBase;
import com.github.pages.mobile.Footer;
import com.github.pages.mobile.ProfilePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.bys.builder.AppiumByBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Tag("MOBILE")
@Owner("grad0ff")
@Feature("Work with App")
@DisplayName("Mobile tests")
public class MobileTests extends MobileTestBase {

    @Test
    @Story("User edits profile")
    @Description("Checks that user can set  profilePage.settings")
    @DisplayName("Notifications setting test")
    void setNotificationsTypesTest() {
        Footer footer = new Footer();
        ProfilePage profilePage = new ProfilePage();

        open();
        $(AppiumBy.id("com.github.android:id/login_button")).click();

        $(AppiumBy.id("com.huawei.android.internal.app:id/icon")).hover().click();
        TestUtils.
        $(AppiumBy.id("com.android.chrome:id/compositor_view_holder")).shouldBe(visible).click();
        $(AppiumBy.id("login_field")).sendKeys(credentialsConfig.getEmail());
        $(AppiumBy.accessibilityId("password")).hover().sendKeys(credentialsConfig.getPassword());

        $(AppiumBy.linkText("Sign in")).click();

        footer.tapByProfileIcon();

        profilePage.settings
                .setDirectMentions()
                .setReviewRequested()
                .setAssigned()
                .setDeploymentReview()
                .setPullRequestReview();

        profilePage.settings.directMentionsSwitch.shouldHave(attribute("chaecked", "true"));
        profilePage.settings.reviewRequestedSwitch.shouldHave(attribute("chaecked", "true"));
        profilePage.settings.AssignedSwitch.shouldHave(attribute("chaecked", "true"));
        profilePage.settings.deploymentReviewSwitch.shouldHave(attribute("chaecked", "true"));
        profilePage.settings.pullRequestReviewSwitch.shouldHave(attribute("chaecked", "true"));
    }
}
