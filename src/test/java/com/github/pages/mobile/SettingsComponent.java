package com.github.pages.mobile;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumBy;

import static com.codeborne.selenide.Selenide.$;

public class SettingsComponent {

    public SelenideElement directMentionsSwitch = $(AppiumBy.linkText("Direct Mentions")).sibling(1);
    public SelenideElement reviewRequestedSwitch = $(AppiumBy.linkText("Review Requested")).sibling(1);
    public SelenideElement AssignedSwitch = $(AppiumBy.linkText("Assigned")).sibling(1);
    public SelenideElement deploymentReviewSwitch = $(AppiumBy.linkText("Deployment Review")).sibling(1);
    public SelenideElement pullRequestReviewSwitch = $(AppiumBy.linkText("Pull Request Review")).sibling(1);


    public SettingsComponent setDirectMentions() {
        directMentionsSwitch.click();
        return this;
    }

    public SettingsComponent setReviewRequested() {
        reviewRequestedSwitch.click();
        return this;
    }

    public SettingsComponent setAssigned() {
        AssignedSwitch.click();
        return this;
    }

    public SettingsComponent setDeploymentReview() {
        deploymentReviewSwitch.click();
        return this;
    }

    public SettingsComponent setPullRequestReview() {
        pullRequestReviewSwitch.click();
        return this;
    }
}
