package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.security.core.parameters.P;

public class SuggestStepDefs {
    private static WebDriver driver = new ChromeDriver();
    private static String username = "";
    private static String password = "";
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> passwords = new ArrayList<>();
    private static int track = 0;

    public static void createUser(Boolean first) {
        String tempUsername = UUID.randomUUID().toString();
        String tempPassword = UUID.randomUUID().toString() + "!A1n";
        if (first) {
            username = tempUsername;
            password = tempPassword;
        }
        driver.get("http://localhost:8080/signup");
        WebElement usernameElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        WebElement confirmPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-password")));
        WebElement signupButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.id("signupButton")));
        usernameElement.sendKeys(tempUsername);
        passwordElement.sendKeys(tempPassword);
        confirmPasswordElement.sendKeys(tempPassword);
        signupButton.sendKeys(Keys.ENTER);
        if (!first) {
            usernames.add(tempUsername);
            passwords.add(tempPassword);
            System.out.println(usernames);
            System.out.println(password);
        }
    }

    public static void setUserToPublicAndCreateFavorites(String username, String password, String searchTerm,
                                                         Boolean private_user) {
        driver.get("http://localhost:8080/login");
        WebElement loginUsernameElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement loginPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));
        loginUsernameElement.sendKeys(username);
        loginPasswordElement.sendKeys(password);
        loginButton.sendKeys(Keys.ENTER);
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("search-input")));
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
        WebElement searchButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("search-button-input")));
        searchButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement parkCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".results-container > div")));
        Actions actions = new Actions(driver);
        actions.moveToElement(parkCard).perform();
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addToFavoritesButton")));
        addButton.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        if (!private_user)  {
            driver.get("http://localhost:8080/favorites");
            System.out.println("before privacy button");
            WebElement privacyButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toggle-button")));
            System.out.println(privacyButton);
            if (!privacyButton.getText().contains("Public")) {
                privacyButton.click();
                new WebDriverWait(driver, Duration.ofSeconds(300)).until(ExpectedConditions.textToBePresentInElement(privacyButton, "Public"));
            }
        }
    }

    public static void masterLogIn() {
        driver.get("http://localhost:8080/login");
        WebElement loginUsernameElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement loginPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));
        loginUsernameElement.sendKeys(username);
        loginPasswordElement.sendKeys(password);
        loginButton.sendKeys(Keys.ENTER);
    }

    @Before
    public static void setup() {
        if (track < 4) {
            createUser(true);
            track += 1;
            createUser(false);
            setUserToPublicAndCreateFavorites(usernames.get(0), passwords.get(0), "test", false);
            track += 1;
            createUser(false);
            setUserToPublicAndCreateFavorites(usernames.get(1), passwords.get(1), "test", false);
            track += 1;
            createUser(false);
            setUserToPublicAndCreateFavorites(usernames.get(2), passwords.get(2), "test", true);
            masterLogIn();
            track += 1;
        }

    }



    @When("the user enters a large number of valid friend usernames")
    public void enterLargeNumberOfValidFriendUsernames() {
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        searchInput.clear();
        System.out.println("Current usernames for large number: " + usernames);
        searchInput.sendKeys(usernames.get(0) + ", " + usernames.get(1));
    }

    @When("the user enters an invalid friend username")
    public void enterInvalidFriendUsername() {
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        searchInput.clear();
        searchInput.sendKeys("invalid");
    }

    @When("the user enters a valid friend username")
    public void enterValidFriendUsername() {
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        searchInput.clear();
        searchInput.sendKeys(usernames.get(0));
    }

    @When("the user enters a friend username with a private list")
    public void enterFriendUsernameWithPrivateList() {
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        searchInput.clear();
        searchInput.sendKeys(usernames.get(2));
    }

    @Then("a park has been suggested by the suggestion algorithm that is in the friends' lists and has the highest average among all friends' lists and displays the park name, location, and three pictures")
    public void seeParkSuggested() {
        WebElement suggestedPark = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("suggested-park")));
        assertTrue((suggestedPark != null));
    }

    @Given("the user is on the search page")
    public void onSearchPageSuggest() {
        driver.get("http://localhost:8080/search");
    }

    @When("the user clicks on the suggest link in the navigation bar")
    public void clickSuggestSuggest() {
        WebElement signupButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.id("suggest-button")));
        signupButton.click();
    }

    @Then("the user is redirected to the park suggestion page")
    public void redirectToParkSuggestionPage() {
        new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.urlContains("suggest"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("suggest"));
    }

    @Given("the user is on the park suggestion page")
    public void onSuggestPage() {
        driver.get("http://localhost:8080/suggest");
    }

    @And("clicks the search park button on suggest")
    public void clickSearchParkButtonOnSuggest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement exitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-button-input")));
        exitButton.click();
    }

    @And("clicks the suggest park button on suggest")
    public void clickSuggestParkButtonOnSuggest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement exitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("suggest-button-input")));
        exitButton.click();
    }

    @Then("clicks the search park button on suggest and sees an error message indicating the private list")
    public void clicksSuggestParkButtonOnSuggestPrivate() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement exitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-button-input")));
        exitButton.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        assertTrue(alertText.contains("private"));
    }

    @Then("clicks the search park button on suggest and sees an error message indicating the invalid list")
    public void clicksSuggestParkButtonInvalidList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement exitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-button-input")));
        exitButton.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        assertTrue(alertText.contains("invalid"));
    }

    @When("I click on the {string} button in the navigation bar on the suggest page")
    public void iClickOnTheButtonInTheNavigationBar(String buttonText) {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(200))
                .until(ExpectedConditions.presenceOfElementLocated((By.id(buttonText + "-button"))));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", button);
    }

    @Given("I am on the Park Search page suggest")
    public void onParkSearchPageSuggest() {
        driver.get("http://localhost:8080/search");
    }

    @Then("the user should be redirected to the park suggestion page")
    public void iShouldBeRedirectedToThePageSuggestion() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.urlContains("suggest"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("suggest"));
    }

    @Then("the user should be redirected to the compare parks page")
    public void userRedirectedToCompareParksPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.urlContains("compare"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("suggest"));
    }

    @Then("the user should be redirected to the search page from suggest")
    public void redirectedFromSearchPageFromSuggestCheck() {
        new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.urlContains("search"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("search"));
    }

    @Then("the user should be redirected to the favorites page from suggest")
    public void redirectedFromFavoritesPageFromSuggestCheck() {
        new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.urlContains("search"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("favorites"));
    }

    @Given("I am on the Park suggest page suggest")
    public void onParkSuggestPageSuggest() {
        driver.get("http://localhost:8080/suggest");
    }

    @When("the user clicks on the park display expanded details about the park should appear")
    public void clickOnParkDisplayExpandedDetails() {
        WebElement suggestedPark = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.id("suggested-park")));
        suggestedPark.click();
        WebElement details = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("specific-result")));
        assertTrue((details != null));
    }

    @When("when I click on back to suggested park the expanded details about the park disappear")
    public void clickOnBackToSuggestedPark() {
        WebElement backToSuggestedPark = new WebDriverWait(driver, Duration.ofSeconds(200))
                .until(ExpectedConditions.elementToBeClickable(By.id("back-to-suggested-park")));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", backToSuggestedPark);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.invisibilityOfElementLocated(By.className("specific-result")));
        List<WebElement> results = driver.findElements(By.className("specific-result"));
        assertTrue(results.isEmpty(), "The 'specific-result' should not be present after clicking back.");
    }

    @Then("I should be logged out of the application and redirected to the Login page")
    public void checkLogoutSuggest() {
        String expectedUrl = "http://localhost:8080/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The current URL does not match the expected URL for the Login page.");
    }

    @Given("I am on the Suggest Search page")
    public void iAmOnTheSuggestSearchPage() {
                driver.get("http://localhost:8080/suggest");
    }

    @After
    public void tearDown() {
        // Remove the driver.quit() call from here
    }

    @AfterAll
    public static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    @When("I click on the {string} button in the navigation bars")
    public void iClickOnTheButtonInTheNavigationBarTest(String arg0) {
        {
            driver.get("http://localhost:8080/" + arg0);
        }
    }

    @Then("I should be taken to the {string} page after")
    public void iShouldBeTakenToTheFavoritesPageAfter(String arg0) {
        driver.get("http://localhost:8080/" + arg0);
    }

}
