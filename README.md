<a href="https://github.com/"><img alt="github.com" height="50" src="readme_files/technologies/github.svg"/></a>
# Github. Автотесты на Java


## Содержание :bookmark_tabs:
* <a href="#stack">Cтек технологий</a>
* <a href="#objects">Объекты тестирования</a>
* <a href="#console">Запуск тестов из консоли</a>
* <a href="#code">Код</a>
    + <a href="#intelij">InteliJ IDEA, Java, JUnit 5, Selenide, Rest Assured</a>
    + <a href="#gradle">Gradle</a>
* <a href="#screenshot">Скриншоты и видео</a>
  + <a href="#selenoid">Selenoid</a>
  + <a href="#jenkins">Jenkins</a>
  + <a href="#allure">Allure TestOps, Allure Report</a>
  + <a href="#notifications">Telegram, Email</a>



<a id="stack"></a>
## Cтек технологий :hammer_and_wrench:

<div align="center">
<a href="https://www.jetbrains.com/idea/"><img alt="InteliJ IDEA" height="50" src="readme_files/technologies/intelij_idea.svg" width="50"/></a>
<a href="https://www.java.com/"><img alt="Java" height="50" src="readme_files/technologies/java.svg" width="50"/></a>
<a href="https://junit.org/junit5/"><img alt="JUnit 5" height="50" src="readme_files/technologies/junit5.svg" width="50"/></a>
<a href="https://selenide.org/"><img alt="Selenide" height="50" src="readme_files/technologies/selenide.svg" width="50"/></a>
<a href="https://rest-assured.io/"><img alt="Rest Assured" height="50" src="readme_files/technologies/rest_assured.svg" width="50"/></a>
<a href="https://aerokube.com/selenoid/"><img alt="Selenoid" height="50" src="readme_files/technologies/selenoid.svg" width="50"/></a>
<a href="https://gradle.org/"><img alt="Gradle" height="50" src="readme_files/technologies/gradle.svg" width="50"/></a>
<a href="https://www.jenkins.io/"><img alt="Jenkins" height="50" src="readme_files/technologies/jenkins.svg" width="50"/></a>
<a href="https://qameta.io/"><img alt="Allure TestOps" height="50" src="readme_files/technologies/allure_testops.svg" width="50"/></a>
<a href="https://github.com/allure-framework/"><img alt="Allure" height="50" src="readme_files/technologies/allure.svg" width="50"/></a>
<a href="https://github.com/"><img alt="GitHub" height="50" src="readme_files/technologies/github.svg" width="50"/></a>
</div>



<a id="objects"></a>
## Объекты тестирования :mag:

Разработаны автотесты для проверок:

* API:

:white_check_mark: корректность данных профиля пользователя  

:white_check_mark: добавление пользователем электронной почты с некорректным адресом

:white_check_mark: создание пользователем нового репозитория

* API + UI:

:white_check_mark: добавление дополнительного адреса электронной почты

:white_check_mark: фильтрация репозиториев пользователя при их просмотре 


* UI:

:white_check_mark: загрузка аватара пользователя

:white_check_mark: редактирование данных в профиле пользователя



<a id="console"></a>
## Запуск тестов из консоли :computer:

```bash
gradle clean 
-DtestType=${TEST_TYPE}
-Dbrowser=${BROWSER}
-DbrowserSize=${BROWSER_SIZE}
-Dhost=${HOST}

```

> `${TEST_TYPE}` - вид теста [ *test* <sub>(default)</sub> , *api_tests*, *combo_tests*, *ui_tests* ]
> 
> 
> `${BROWSER}` - браузер [ *chrome* <sub>(default)</sub> , *firefox*, *opera*]
> 
> `${BROWSER_SIZE}` - размер окна браузера  [ *1920x1080* <sub>(default)</sub> , *1366x768*, *1280x1024*]
>
> `${HOST}` - хост прогона тестов [ *remote* <sub>(default)</sub> , *local* ]



<a id="code"></a>
## Код :floppy_disk:

<a id="intelij"></a>
#### <img alt="InteliJ IDEA" height="50" src="readme_files/technologies/intelij_idea.svg" width="50"/>InteliJ IDEA</a><img alt="Java" height="50" src="readme_files/technologies/java.svg" width="50"/>Java</a><img alt="JUnit 5" height="50" src="readme_files/technologies/junit5.svg" width="50"/>JUnit 5</a><img alt="Selenide" height="50" src="readme_files/technologies/selenide.svg" width="50"/>Selenide</a>

> *Оформление кода автотестов*

```java

@Tag("API+AUI")
@Owner("grad0ff")
@Feature("Work with API and UI of site")
@DisplayName("API+UI tests")
public class ComboTests extends UiTestBase {

  @Test
  @Story("The user filters repositories")
  @Description("Checks that user can filter repositories")
  @DisplayName("Repositories filtering test")
  void filterRepoByVisibilityTest() {
    ProfilePage page = new ProfilePage();
    reqSpec.basePath("/user/repos");

    step("Create 1 public and 2 private repositories with API", () -> {
      int repoCount = 3;
      do {
        given()
                .spec(Spec.reqSpec)
                .body(Map.of(
                        "name", "repository" + new Random().nextInt(),
                        "private", repoCount < 3))
                .when()
                .post()
                .then()
                .spec(Spec.resSpec)
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
    step("Filter repositories by private access", () ->
            page.repoTab.clickByTypeOption().selectPrivateFilter());
    step("Check that every repository contains 'Private' mark", () -> 
            page.repoTab.repoList.should(allMatch("all 'Private'", item -> item.getText().equals("Private"))));
  }
}
```



<a id="gradle"></a>
#### <img alt="Gradle" height="50" src="readme_files/technologies/gradle.svg" width="50"/>Gradle</a>

> *Автоматическая сборка проекта и управление зависимостями*

```groovy
plugins {
  id 'java-library'
  id 'io.qameta.allure' version '2.10.0'
  id 'io.freefair.lombok' version '6.5.0.3'
}

repositories {
  mavenCentral()
}

def jUnitVersion = '5.8.2',
    appiumVersion = "8.0.0",
    selenideVersion = '6.5.2',
    restAssuredVersion = '5.1.1',
    ownerVersion = '1.0.12',
    gsonVersion = '2.9.1',
    javaFakerVersion = '1.0.2',
    assertjVersion = '3.23.1',
    allureVersion = '2.18.1',
    commonsIoVersion = "2.11.0",
    slf4jVersion = '1.7.36'

dependencies {
  testImplementation(
          "org.junit.jupiter:junit-jupiter:$jUnitVersion",
          "io.appium:java-client:$appiumVersion",
          "com.codeborne:selenide:$selenideVersion",
          "io.appium:java-client:$appiumVersion",
          "io.rest-assured:rest-assured:$restAssuredVersion",
          "org.aeonbits.owner:owner:$ownerVersion",
          "com.google.code.gson:gson:$gsonVersion",
          "com.github.javafaker:javafaker:$javaFakerVersion",
          "org.assertj:assertj-core:$assertjVersion",
          "io.qameta.allure:allure-selenide:$allureVersion",
          "io.qameta.allure:allure-rest-assured:$allureVersion",
          "commons-io:commons-io:$commonsIoVersion",
          "org.slf4j:slf4j-simple:$slf4jVersion",
  )
}
```


<a id="screenshot"></a>
## Скриншоты и видео :camera_flash:


<a id="selenoid"></a>
#### <img alt="Selenoid" height="50" src="readme_files/technologies/selenoid.svg" width="50"/>Selenoid</a>

> *Прогон UI автотестов на удаленном сервере*

<table>
     <tr>
        <td>
            <video src="https://user-images.githubusercontent.com/72714071/190106687-62bedabc-ebd1-4d1c-8ac2-e7dcb4b980b4.mp4" controls="controls" style="max-width:    730px;" poster="https://github.com/grad0ff/github/blob/master/readme_files/technologies/selenoid.svg">
Видео недоступно.
            </video>
        </td>
        <td>
            <video src="https://user-images.githubusercontent.com/72714071/190108058-1ca65372-db7f-4ad7-8b5c-19110cf65a25.mp4" controls="controls" style="max-width:    730px;" poster="https://github.com/grad0ff/github/blob/master/readme_files/technologies/selenoid.svg">
Видео недоступно.
            </video>
        </td>
    </tr>
</table>


<a id="jenkins"></a>
#### <img alt="Jenkins" height="50" src="readme_files/technologies/jenkins.svg" width="50"/>Jenkins</a>

> *Решение комплекса задач по сборке проекта, прогону автотестов, получению отчетов и отправке уведомлений по
результатам сборки*

<a href="https://jenkins.autotests.cloud/job/013-grad0ff-github/">
<img src="https://user-images.githubusercontent.com/72714071/190110963-6cd5a218-8a34-4bbe-9137-73af274d4aab.png" alt="Jenkins">
</a>



<a id="allure"></a>
#### <img alt="Allure" height="50" src="readme_files/technologies/allure_testops.svg" width="50"/>Allure TestOps</a><img alt="Allure" height="50" src="readme_files/technologies/allure.svg" width="50"/>Allure Report</a>

> *Формирование отчетов по результам прогона автотестов*

<table>
     <tr>
        <td>
        <a href="https://allure.autotests.cloud/project/1579/dashboards">
        <img src="https://user-images.githubusercontent.com/72714071/190111941-3557dd8b-7577-4cc2-8dfa-31baf5407208.png">
        </a>
        </td>
        <td>
        <a href="https://allure.autotests.cloud/project/1579/test-cases/12193?treeId=3022">
        <img src="https://user-images.githubusercontent.com/72714071/190112291-12574fd2-8ec6-40d0-ae68-c1d514759419.png">
        </a>
        </td>
    </tr>
    <tr>
        <td>
        <a href="https://jenkins.autotests.cloud/job/013-grad0ff-github/allure/">
        <img src="https://user-images.githubusercontent.com/72714071/190112805-8a1de909-ad70-42a0-a77b-d61f8f6315ce.png">
        </a>
        </td>
        <td>
        <a href="https://jenkins.autotests.cloud/job/013-grad0ff-github/allure/#suites/8296116fb5ecdcc50ae710349f189d84">
        <img src="https://user-images.githubusercontent.com/72714071/190113647-6d3fa879-7738-4b57-8dc6-fe3666deb04c.png">
        </a>
        </td>
    </tr>
    <tr>
        <td>
        <a href="https://jenkins.autotests.cloud/job/013-grad0ff-github/allure/#suites/9ae7943627b178f740fc1bf87589174c/f69d1b013c7c0036/">
        <img src="https://user-images.githubusercontent.com/72714071/190114014-33ab5a9a-8087-4d2b-b07e-0430c108fad2.png">
        </a>
        </td>
        <td>
        <a href="https://jenkins.autotests.cloud/job/013-grad0ff-github/allure/#graph">
        <img src="https://user-images.githubusercontent.com/72714071/190114161-e05363e0-79a9-4d9f-9301-e25bf42c3bd3.png">
        </a>
        </td>
</table>



<a id="notifications"></a>
#### <img alt="Telegram" height="50" src="readme_files/technologies/telegram.svg" width="50"/>Telegram</a> <img alt="Email" height="50" src="readme_files/technologies/yandex_mail.png" width="50"/>Email</a>

> *Предоставление оперативной информации о результатах прогона автотестов*

<table>
     <tr>
        <td>
        <img src="https://user-images.githubusercontent.com/72714071/190114686-f4e89064-8932-4fd2-9125-650521d93de2.png" alt="Telegram">
        </a>
        </td>
        <td>
        <img src="https://user-images.githubusercontent.com/72714071/190115032-5665b927-957e-4c65-9589-9118a8c30b5e.png" alt="Email">
        </a>
        </td>
    </tr>
 </table>   
