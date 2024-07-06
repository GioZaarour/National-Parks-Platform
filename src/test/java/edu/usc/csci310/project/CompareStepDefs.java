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

public class CompareStepDefs {

    private static WebDriver driver = new ChromeDriver();
    private static String username = "";
    private static String password = "";
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> passwords = new ArrayList<>();
    private static int track = 0;
    private FavoritesMyStepdefs favoritesteps;



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



    @And("I am on the Compare Favorites page com")
    public void iAmOnTheCompareFavoritesPage() {
        driver.get("http://localhost:8080/compare");
    }

    @When("I enter the one username into the search com")
    public void iEnterTheFirstRegisteredUsernameIntoTheSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-bar")));
        searchInput.clear();
        searchInput.sendKeys(usernames.get(0));
    }


    @Given("I am on the Compare Search page")
    public void iAmOnTheCompareSearchPage() {
            driver.get("http://localhost:8080/compare");
    }

    @And("I press on the search button com")
    public void iPressOnTheSearchButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-button-input")));
        searchButton.click();
    }

    @Then("I should see the first username with a compare button com")
    public void iShouldSeeTheLoggedInUserWithACompareButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement compareButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("compare-button-input")));
        assertTrue(compareButton.isDisplayed(), "Compare button is not displayed for the user.");
    }

    @When("I enter the first and second username into the search com")
    public void iEnterAnUnknownUserOrGroupName() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-bar")));
        searchInput.clear();
        searchInput.sendKeys(usernames.get(0) + ", " + usernames.get(1));
    }

    @And("I press the compare button com")
    public void iPressTheCompareButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        try {
            WebElement compareButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("compare-button-input")));
            compareButton.click();
        } catch (TimeoutException e) {
            throw new AssertionError("Compare button was not clickable or found within the expected time.", e);
        }
    }

    @Then("I should see the group with a compare button com")
    public void iShouldSeeTheGroupWithACompareButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement compareButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("compare-button-input")));
        assertTrue(compareButton.isDisplayed(), "Compare button is not displayed for the group.");
    }

    @Then("I should see all of the parks that logged in user has with first user")
    public void iShouldSeeAllOfTheParksThatUserHasInCommon() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(600));
        List<WebElement> parks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.className("compare-result")));
        assertFalse(parks.isEmpty(), "No parks are displayed on the page.");
    }

    @When("I enter a user or group name not in the database com")
    public void iEnterAUserOrGroupNameNotInTheDatabaseCom() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-bar")));
        searchInput.clear();
        searchInput.sendKeys(UUID.randomUUID().toString());
    }

    @Then("I should see an error message")
    public void iShouldSeeAMessageSaying() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();

            assertTrue(alertText.contains("The following users do not exist:"), "Alert message does not contain expected text.");

        } catch (TimeoutException e) {
            fail("No alert was present when one was expected.");
        }
    }

    @When("I enter a user that is private")
    public void iEnterAUserThatIsPrivate() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-bar")));
        searchInput.clear();
        searchInput.sendKeys(usernames.get(2));
    }

    @Then("I should see a alert telling me the user is private")
    public void iShouldSeeAAlertTellingMeTheUserIsPrivate() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();

        assertTrue(alertText.contains("private"));
    }

    @When("I press on the search page button")
    public void iPressOnTheSearchPageButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-button")));
        searchButton.click();
    }

    @When("I press on the favorites page button")
    public void iPressOnTheFavoritesPageButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("favorites-button")));
        searchButton.click();
    }

    @When("I press on the suggest page button")
    public void iPressOnTheSuggestPageButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("suggest-button")));
        searchButton.click();
    }

    @When("I press on the logout  button")
    public void iPressOnTheLogoutButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button")));
        searchButton.click();
    }

    @Then("I am on the search page")
    public void iAmOnTheSearchPage() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/search"), "The current URL should end with /search");
    }

    @Then("I am on the favorites page")
    public void iAmOnTheFavoritesPage() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/favorites"), "The current URL should end with /favorites");
    }

    @Then("I am logged out")
    public void iAmLoggedOut() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/login"), "The current URL should end with /login");
    }

    @Then("I am on the suggest page")
    public void iAmOnTheSuggestPage() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/suggest"), "The current URL should end with /suggest");
    }

//////////should work but need compare button to work to check


    @Then("I should see detailed information about that park com")
    public void iShouldSeeDetailedInformationAboutThatPark() {
        WebElement parkDetails = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("park-details")));
        assertTrue(parkDetails.isDisplayed(), "Park details are not displayed");
    }



    @And("I click on a park com")
    public void iClickOnAPark() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement firstPark = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("compare-result")));

        if (firstPark != null) {
            firstPark.click();
        } else {
            throw new RuntimeException("No park found to click.");
        }
    }

    @Then("I should see all of the parks that the group {string} have in common com")
    public void iShouldSeeAllOfTheParksThatTheGroupHaveInCommon(String groupName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        try {
            List<WebElement> parks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.className("compare-result")));
            assertFalse(parks.isEmpty(), "No parks are displayed on the page.");

        } catch (Exception e) {
            fail("Failed to verify the presence of parks for group '" + groupName + "': " + e.getMessage());
        }
    }

    @Then("I should see park's location com")
    public void iShouldSeeParkSLocation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement locationElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("park-location")));
        String locationText = locationElement.getText();
        assertFalse(locationText.isEmpty(), "The location information is not displayed.");
    }

    @And("I press on the ratio of a park com")
    public void iPressOnTheRatioOfAPark() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement firstPark = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("result-ratio")));
        if (firstPark != null) {
            firstPark.click();
        } else {
            throw new RuntimeException("No park found to click.");
        }
    }

    @And("I click the \"x\" button on the card com")
    public void iClickTheXButtonOnTheCard() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        try {
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.className("close-card-button")));
            closeButton.click();
        } catch (Exception e) {
            fail("Failed to click the 'x' button on the card: " + e.getMessage());
        }
    }

    @Then("the card should close com")
    public void theCardShouldClose() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.className("visible-card")));
        } catch (TimeoutException e) {
            fail("The card did not close as expected.");
        }
    }

    @And("I should return to the comparison results list com")
    public void iShouldReturnToTheComparisonResultsList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.id("back-to-search-results")));
        } catch (Exception e) {
            fail("Did not return to the comparison results list: " + e.getMessage());
        }
    }

    @And("I click on the \"Back to Search Results\" button com")
    public void iClickOnTheBackToSearchResultsButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        try {
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("back-to-search-results")));  // Update the XPath if necessary
            backButton.click();
        } catch (Exception e) {
            fail("Failed to click the 'Back to Search Results' button: " + e.getMessage());
        }
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
    @Given("I am on the Suggest Search page")
    public void iAmOnTheSuggestSearchPage() {
      driver.get("http://localhost:8080/suggest");
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
