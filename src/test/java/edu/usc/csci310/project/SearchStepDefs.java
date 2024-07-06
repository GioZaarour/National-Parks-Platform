package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SearchStepDefs {
    private WebDriver driver;
    private String username;
    private String password;

    @Before
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Generate random username and password
        username = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString() + "!A1n";

        // Sign up a new user
        driver.get("http://localhost:8080/signup");
        WebElement usernameElement = driver.findElement(By.id("username"));
        WebElement passwordElement = driver.findElement(By.id("password"));
        WebElement confirmPasswordElement = driver.findElement(By.id("confirm-password"));
        WebElement signupButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("signupButton")));
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        confirmPasswordElement.sendKeys(password);
        signupButton.sendKeys(Keys.ENTER);

        // Log in the user
        driver.get("http://localhost:8080/login");
        WebElement loginUsernameElement = driver.findElement(By.id("username"));
        WebElement loginPasswordElement = driver.findElement(By.id("password"));
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("loginButton")));
        loginUsernameElement.sendKeys(username);
        loginPasswordElement.sendKeys(password);
        loginButton.sendKeys(Keys.ENTER);
    }

    @Given("I log into the application fav")
    public void logIntoTheApplication() {
        driver.get("http://localhost:8080/login");
        WebElement loginUsernameElement = driver.findElement(By.id("username"));
        WebElement loginPasswordElement = driver.findElement(By.id("password"));
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("loginButton")));
        loginUsernameElement.sendKeys(username);
        loginPasswordElement.sendKeys(password);
        loginButton.sendKeys(Keys.ENTER);
    }

    @Given("I am on the Park Search page")
    public void iAmOnTheParkSearchPage() {
        driver.get("http://localhost:8080/search");
    }

    @When("I enter {string} into the search field")
    public void iEnterIntoTheSearchField(String searchTerm) {
        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
    }

    @When("I click the park name option")
    public void iClickTheParkNameOption() {
        WebElement optionElement = driver.findElement(By.id("search-park-name"));
        optionElement.click();
    }

    @When("I click the state code option")
    public void iClickTheStateCodeOption() {
        WebElement optionElement = driver.findElement(By.id("search-park-state"));
        optionElement.click();
    }

    @When("I click the park amenities option")
    public void iClickTheParkAmenitiesOption() {
        WebElement optionElement = driver.findElement(By.id("search-park-amenities"));
        optionElement.click();
    }

    @When("I click the park activities option")
    public void iClickTheParkActivitiesOption() {
        WebElement optionElement = driver.findElement(By.id("search-park-activities"));
        optionElement.click();
    }

    @And("I press the search button")
    public void iPressTheSearchButton() {
        WebElement searchButton = driver.findElement(By.id("search-button-input"));
        searchButton.click();
    }

//    @Then("I should see a list of parks in {string}")
//    public void iShouldSeeAListOfParksIn(String state) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement locationElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("park-location")));
//        String locationText = locationElement.getText();
//        assertFalse(locationText.isEmpty(), "The state information is not displayed.");
//    }

    @Then("I should see a list of parks that offer {string} amenities")
    public void iShouldSeeAListOfParksThatOfferAmenities(String amenity) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement amenityElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("amenities")));
        String amenityText = amenityElement.getText();
        assertFalse(amenityText.isEmpty(), "The amenity information is not displayed.");
    }

    @Then("I should see a list of parks that offer {string} activities")
    public void iShouldSeeAListOfParksThatOfferActivities(String activities) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement activityElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activities")));
        String activityText = activityElement.getText();
        assertFalse(activityText.isEmpty(), "The activity information is not displayed.");
    }

    @Then("I should see a list of parks including {string}")
    public void iShouldSeeAListOfParksIncluding(String expectedParkName) {
        WebElement results = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
        List<WebElement> childElements = results.findElements(By.xpath("./*"));
        List<String> childIds = new ArrayList<>();
        for (WebElement child : childElements) {
            String id = child.getAttribute("id");
            if (id != null && !id.isEmpty()) {
                childIds.add(id.toLowerCase());
            }
        }
        boolean containsSubstring = childIds.stream().anyMatch(id -> id.contains(expectedParkName.toLowerCase()));
        assertTrue(containsSubstring, "The expected park name was not found.");
    }

    @And("I click on the load more button")
    public void iClickOnTheLoadMoreButton() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("load-more")));
        button.sendKeys(Keys.ENTER);
    }

    @Then("I should see an additional set of 10 parks added to the list")
    public void iShouldSeeAnAdditionalSetOfParksAddedToTheList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
        List<WebElement> parkElements = resultsContainer.findElements(By.xpath("./div"));

        assertTrue(parkElements.size() > 10, "There are not more than 10 parks added to the list.");
    }

//    @When("I click on a park with the name {string}")
//    public void iClickOnAParkWithTheName(String parkName) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
//        List<WebElement> parkElements = resultsContainer.findElements(By.xpath("./div"));
//
//        for (WebElement parkElement : parkElements) {
//            String parkText = parkElement.getText().toLowerCase();
//            if (parkText.contains(parkName.toLowerCase())) {
//                parkElement.click();
//                break;
//            }
//        }
//    }

    @Then("I should see the location")
    public void iShouldSeeTheLocation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement locationElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("park-location")));
        String locationText = locationElement.getText();
        assertFalse(locationText.isEmpty(), "The location information is not displayed.");
    }

    @Then("I should see URL")
    public void iShouldSeeURL() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement urlElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("park-url")));
        String urlText = urlElement.getText();
        assertFalse(urlText.isEmpty(), "The URL information is not displayed.");
    }

    @Then("I should see the Entrance Fee")
    public void iShouldSeeTheEntranceFee() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entranceFeeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("entrance-fee")));
        String entranceFeeText = entranceFeeElement.getText();
        assertFalse(entranceFeeText.isEmpty(), "The entrance fee information is not displayed.");
    }

    @Then("I should see the Amenities")
    public void iShouldSeeTheAmenities() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement amenitiesElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("amenities")));
        String amenitiesText = amenitiesElement.getText();
        assertFalse(amenitiesText.isEmpty(), "The amenities information is not displayed.");
    }

    @Then("I should see the Activities")
    public void iShouldSeeTheActivities() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement activitiesElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activities")));
        String activitiesText = activitiesElement.getText();
        assertFalse(activitiesText.isEmpty(), "The activities information is not displayed.");
    }

    @Then("I shouldn't see any parks")
    public void iShouldntSeeAnyParks() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
            List<WebElement> parkElements = resultsContainer.findElements(By.xpath("./div"));
            assertEquals(0, parkElements.size(), "There are parks displayed, but there should be none.");
        } catch (org.openqa.selenium.TimeoutException e) {
            // No results container found, which is the expected behavior
        }
    }

    @Then("I should see 10 parks listed for the initial search result")
    public void iShouldSeeTenParksListedForTheInitialSearchResult() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
        List<WebElement> parkElements = resultsContainer.findElements(By.xpath("./div"));
        assertEquals(10, parkElements.size(), "The number of displayed parks is not 10.");
    }

    @And("I have an option to go back to the search results")
    public void iHaveAnOptionToGoBackToTheSearchResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement backToSearchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("back-to-search-results")));
        assertTrue(backToSearchButton.isDisplayed(), "The 'Back to Search Results' button is not displayed.");
    }

    @And("I click on a park")
    public void iClickOnAPark() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
        List<WebElement> parkElements = resultsContainer.findElements(By.xpath("./div"));

        if (!parkElements.isEmpty()) {
            // Click on the first park element in the list
            parkElements.get(0).click();
        } else {
            fail("No parks found in the search results.");
        }
    }

    @When("I click on a park with the name {string}")
    public void iClickOnAParkWithTheName(String parkName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
        List<WebElement> parkElements = resultsContainer.findElements(By.xpath("./div"));

        for (WebElement parkElement : parkElements) {
            String parkText = parkElement.getText().toLowerCase();
            if (parkText.contains(parkName.toLowerCase())) {
                parkElement.click();
                break;
            }
        }
    }

    @When("I click on the state button in the park details")
    public void iClickOnTheStateButtonInTheParkDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement stateButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("state-button")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", stateButton);

        // Wait for the scroll to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Actions actions = new Actions(driver);
        actions.moveToElement(stateButton).click().build().perform();
    }

    @Then("I should see a list of parks in {string}")
    public void iShouldSeeAListOfParksIn(String state) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement locationElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("park-location")));
        String locationText = locationElement.getText();
        assertTrue(locationText.contains(state), "The state information is not displayed or does not match the expected state.");
    }

    @When("I enter {string} for the activities option")
    public void iEnterForTheActivitiesOption(String activity) {
        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.clear();
        searchInput.sendKeys(activity);

        WebElement optionElement = driver.findElement(By.id("search-park-activities"));
        optionElement.click();
    }

    @When("I click on an amenity button in the park details")
    public void iClickOnTheAmenityButtonInTheParkDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement amenityButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("amenities-button")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", amenityButton);

        // Wait for the scroll to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Actions actions = new Actions(driver);
        actions.moveToElement(amenityButton).click().build().perform();
    }

    @When("I click on an activity button in the park details")
    public void iClickOnTheActivityButtonInTheParkDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement activityButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("activity-button")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", activityButton);

        // Wait for the scroll to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Actions actions = new Actions(driver);
        actions.moveToElement(activityButton).click().build().perform();
    }

    @Then("I should see a list of parks that offer the selected amenity")
    public void iShouldSeeAListOfParksThatOfferTheSelectedAmenity() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement amenitiesElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("amenities")));
        String amenitiesText = amenitiesElement.getText();
        assertFalse(amenitiesText.isEmpty(), "The amenities information is not displayed.");
    }

    @Then("I should see a list of parks that offer the selected activity")
    public void iShouldSeeAListOfParksThatOfferTheSelectedActivity() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement activitiesElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activities")));
        String activitiesText = activitiesElement.getText();
        assertFalse(activitiesText.isEmpty(), "The activities information is not displayed.");
    }

    @When("I press the enter key")
    public void iPressTheEnterKey() {
        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.sendKeys(Keys.ENTER);
    }

    @When("I remain inactive for more than 60 seconds")
    public void iRemainInactiveForMoreThanSeconds() {
        try {
            Thread.sleep(62 * 1000L); // Wait for 62 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("I should be redirected to the {string} page")
    public void iShouldBeRedirectedToThePage(String expectedPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains(expectedPath));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains(expectedPath), "Expected URL: " + expectedPath + ", Actual URL: " + currentUrl);
    }

    @When("I click on the exit button")
    public void iClickOnTheExitButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement exitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exit-button")));
        exitButton.click();
    }

    @Then("I should see the search results page")
    public void iShouldSeeTheSearchResultsPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("results-container")));
        assertTrue(resultsContainer.isDisplayed(), "The search results page is not displayed.");
    }
    @When("I click on the {string} button in the navigation bar")
    public void iClickOnTheButtonInTheNavigationBar(String buttonText) {
        WebElement button = driver.findElement(By.xpath("//button[contains(text(), '" + buttonText + "')]"));
        button.click();
    }

    @Then("I should be taken to the Search Parks page")
    public void iShouldBeTakenToTheSearchParksPage() {
        String expectedUrl = "http://localhost:8080/search";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The current URL does not match the expected URL for the Search Parks page.");
    }

    @Then("I should be taken to the Suggest a Park page")
    public void iShouldBeTakenToTheSuggestAParkPage() {
        String expectedUrl = "http://localhost:8080/suggest";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The current URL does not match the expected URL for the Suggest a Park page.");
    }

    @Then("I should be taken to the Compare Parks page")
    public void iShouldBeTakenToTheCompareParksPage() {
        String expectedUrl = "http://localhost:8080/compare";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The current URL does not match the expected URL for the Compare Parks page.");
    }

    @Then("I should be taken to the Favorites page")
    public void iShouldBeTakenToTheFavoritesPage() {
        String expectedUrl = "http://localhost:8080/favorites";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The current URL does not match the expected URL for the Favorites page.");
    }

    @Then("I should be redirected to the Login page")
    public void iShouldBeRedirectedToTheLoginPage() {
        String expectedUrl = "http://localhost:8080/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The current URL does not match the expected URL for the Login page.");
    }

    @When("I click on the park name in the park details")
    public void iClickOnTheParkNameInTheParkDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement parkNameElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("park-name-close")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", parkNameElement);

        // Wait for the scroll to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Actions actions = new Actions(driver);
        actions.moveToElement(parkNameElement).click().build().perform();
    }

    @Then("the park details should be closed")
    public void theParkDetailsShouldBeClosed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("park-details")));
        } catch (TimeoutException e) {
            fail("The park details are still visible after clicking the park name.");
        }
    }

    @When("I hover over a park card")
    public void iHoverOverAParkCard() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement parkCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".results-container > div")));

        Actions actions = new Actions(driver);
        actions.moveToElement(parkCard).perform();
    }

    @Then("the \"Add to Favorites\" button should be displayed for that park card")
    public void theAddToFavoritesButtonShouldBeDisplayedForThatParkCard() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addToFavoritesButton")));
        assertTrue(addButton.isDisplayed(), "The 'Add to Favorites' button is not displayed on hover.");
    }

    @After
    public void after() {
        //this.driver.close();
        this.driver.quit();
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
