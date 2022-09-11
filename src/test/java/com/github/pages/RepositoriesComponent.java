package com.github.pages;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RepositoriesComponent {

    public final String PATH = "?tab=repositories";
    public ElementsCollection repoList = $$("#user-repositories-list [itemprop='owns'] .Label");

    public RepositoriesComponent clickByTypeOption() {
        $("#type-options .btn").click();
        return this;
    }

    public RepositoriesComponent selectPrivateFilter() {
        $$("#type-options .SelectMenu-item").get(2).click();
        return this;
    }
}
