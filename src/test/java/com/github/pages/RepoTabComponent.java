package com.github.pages;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RepoTabComponent {

    public final String PATH = "?tab=repositories";
    public ElementsCollection repoList = $$("#user-repositories-list [itemprop='owns'] .Label");

    public RepoTabComponent clickByTypeOption() {
        $("#type-options .btn").click();

        return this;
    }

    public RepoTabComponent selectPrivateFilter() {
        $$("#type-options .SelectMenu-item").get(2).click();

        return this;
    }
}
