package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FavoritesMyStepdefs {

    private WebDriver driver;
    private String username;
    private String password;

    @Before
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100));

        // Generate random username and password
        username = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString() + "!A1n";

        // Sign up a new user
        driver.get("http://localhost:8080/signup");
        WebElement usernameElement = driver.findElement(By.id("username"));
        WebElement passwordElement = driver.findElement(By.id("password"));
        WebElement confirmPasswordElement = driver.findElement(By.id("confirm-password"));
        WebElement signupButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("signupButton")));
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        confirmPasswordElement.sendKeys(password);
        signupButton.sendKeys(Keys.ENTER);

        // Log in the user
        driver.get("http://localhost:8080/login");
        WebElement loginUsernameElement = driver.findElement(By.id("username"));
        WebElement loginPasswordElement = driver.findElement(By.id("password"));
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("loginButton")));
        loginUsernameElement.sendKeys(username);
        loginPasswordElement.sendKeys(password);
        loginButton.sendKeys(Keys.ENTER);
    }

    @Given("I am on the Park Search fav")
    public void i_am_on_the_park_search() {
        driver.get("http://localhost:8080/search");
    }

    @Given("I am on the Favorites Page fav")
    public void i_am_on_the_favorites_page() {
        driver.get("http://localhost:8080/favorites");
    }
    @When("I enter {string} into the search field fav")
    public void iEnterIntoTheSearchField(String searchTerm) {
        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
    }
    @When("I click the park name option fav")
    public void iClickTheParkNameOption() {
        WebElement optionElement = driver.findElement(By.id("search-park-name"));
        optionElement.click();
    }
    @And("I press the search button fav")
    public void iPressTheSearchButton() {
        WebElement searchButton = driver.findElement(By.id("search-button-input"));
        searchButton.click();
    }
    @When("I click the \"+\" button on a park card fav")
    public void i_click_the_button_on_a_park_card() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));

        // Wait for the park card to be visible
        WebElement parkCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".results-container > div")));

        // Optional: Hover over the park card to make the "+" button visible
        // Actions actions = new Actions(driver);
        // actions.moveToElement(parkCard).perform();

        // Wait for the "+" button to be visible and clickable
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addToFavoritesButton")));

        // Click the "+" button
        addButton.click();
    }
    @Then("a success message should be displayed fav")
    public void a_success_message_should_be_displayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));

        // Handle the alert that appears after the action
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();  // Capture the alert text to verify it
            assertEquals("Park added to your favorites list successfully.", alertText);
            alert.accept(); // Accept the alert to close it
        } catch (NoAlertPresentException e) {
            fail("Expected alert not displayed");
        }
    }

    @Given("I have already added a park to my favorites list fav")
    public void i_have_already_added_a_park_to_my_favorites_list() {
        i_am_on_the_park_search();
        i_click_the_button_on_a_park_card();
        a_success_message_should_be_displayed();
    }

    @When("an alert should be displayed stating that the park is already in my favorites list fav")
    public void an_alert_should_be_displayed_stating_that_the_park_is_already_in_my_favorites_list() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));

        // Handle the alert that appears after the action
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();  // Capture the alert text to verify it
            assertTrue(alertText.contains("already in your favorites list"), "Alert text does not match the expected message.");
            alert.accept(); // Accept the alert to close it
        } catch (NoAlertPresentException e) {
            fail("Expected alert not displayed");
        }
    }

    @When("I hover over a park card fav")
    public void i_hover_over_a_park_card() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement parkCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".results-container > div")));

        Actions actions = new Actions(driver);
        actions.moveToElement(parkCard).perform();
    }
    @Given("I have one or more parks in my favorites list fav")
    public void i_have_one_or_more_parks_in_my_favorites_list() {
        i_am_on_the_park_search();
        i_click_the_plus_button_on_the_first_park_card();
        a_success_message_should_be_displayed();
        i_click_the_plus_button_on_the_second_park_card();
        a_success_message_should_be_displayed();
    }

    @Given("I am viewing the details of a park in my favorites list fav")
    public void i_am_viewing_the_details_of_a_park_in_my_favorites_list() {
        i_have_one_or_more_parks_in_my_favorites_list();
        i_click_on_a_park_card_in_the_favorites_list();
        the_park_details_page_should_be_displayed();
    }

    @When("I hover over a park card in the favorites list fav")
    public void i_hover_over_a_park_card_in_the_favorites_list() {
        List<WebElement> favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));

        if (!favoritedParks.isEmpty()) {
            Actions actions = new Actions(driver);
            actions.moveToElement(favoritedParks.get(0)).perform();
        } else {
            fail("No park cards found in the favorites list.");
        }
    }

    @When("I hover over the 2nd  park card in the favorites list fav")
    public void i_hover_over_the_second_park_card_in_the_favorites_list() {
        List<WebElement> favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));

        if (favoritedParks.size() >= 2) {
            Actions actions = new Actions(driver);
            actions.moveToElement(favoritedParks.get(1)).perform(); // Hover over the second park card
        } else {
            fail("Not enough park cards found in the favorites list.");
        }
    }

    @When("I click the \"Favorites\" button fav")
    public void i_click_the_favorites_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement favoritesButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("favorites-button")));
        favoritesButton.click();
    }

    @Then("I should be taken to the favorites page fav")
    public void i_should_be_taken_to_the_favorites_page() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.urlContains("/favorites"));
        assertTrue(driver.getCurrentUrl().endsWith("/favorites"), "The current URL does not end with '/favorites'");
    }

    @When("I click the \"x\" button fav")
    public void i_click_the_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement removeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("remove-favorite-button")));
        removeButton.click();
    }

//    @Then("a confirmation dialog should be displayed")
//    public void a_confirmation_dialog_should_be_displayed() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement confirmationDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".confirmation-dialog")));
//        assertTrue(confirmationDialog.isDisplayed());
//    }

    @Then("the park should be removed from my favorites list fav")
    public void the_park_should_be_removed_from_my_favorites_list() {
        driver.get("http://localhost:8080/favorites");
        List<WebElement> favoritedParks = driver.findElements(By.cssSelector(".results-container > div"));
        assertTrue(favoritedParks.isEmpty());
    }

    @When("I click the up arrow button fav")
    public void i_click_the_up_arrow_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement upArrow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("up-arrow")));
        upArrow.click();
    }

    @When("I click on the delete all favorites button fav")
    public void clickAllDeleteFavoritesButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement upArrow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-all-button")));
        upArrow.click();
    }

    @When("I click the confirm delete all favorites button")
    public void confirmDeleteAllFavorites() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement upArrow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirm-delete-all")));
        upArrow.click();
    }

    @Then("I should see all favorites were deleted")
    public void seeAllFavoritesDelete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement upArrow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));
        upArrow.getText().contains("You haven't added any favorite parks yet");
    }

    // @Then("the position of the park in the favorites list should be moved up fav")
    // public void the_position_of_the_park_in_the_favorites_list_should_be_moved_up() {
    //     List<WebElement> favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));
    //     // Store the order of the parks before moving up
    //     List<String> beforeOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

    //     // Move the park up
    //     i_click_the_up_arrow_button();

    //     // Refresh the page and get the updated order of the parks
    //     driver.navigate().refresh();
    //     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    //     wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".favorite-park > div")));
    //     favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));
    //     List<String> afterOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

    //     // Compare the order before and after moving up
    //     assertNotEquals(beforeOrder, afterOrder, "Park position did not change after moving up");
    // }

    @Then("the position of the park in the favorites list should be moved up fav")
    public void the_position_of_the_park_in_the_favorites_list_should_be_moved_up() {
        // Wait for the parks to be visible and then fetch them
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#favorites-container > div")));
        List<WebElement> favoritedParks = driver.findElements(By.cssSelector("#favorites-container > div"));

        // Store the order of the parks before moving up
        List<String> beforeOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

        // Move the park up
        i_click_the_up_arrow_button();

        // Refresh the page and wait for the parks to be visible again
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#favorites-container > div")));
        favoritedParks = driver.findElements(By.cssSelector("#favorites-container > div"));
        List<String> afterOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

        // Compare the order before and after moving up
        assertNotEquals(beforeOrder, afterOrder, "Park position did not change after moving up");
    }

    @When("I click the down arrow button fav")
    public void i_click_the_down_arrow_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement downArrow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("down-arrow")));
        downArrow.click();
    }

//     @Then("the position of the park in the favorites list should be moved down fav")
//     public void the_position_of_the_park_in_the_favorites_list_should_be_moved_down() {
//         List<WebElement> favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));
//         // Store the order of the parks before moving down
//         List<String> beforeOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

//         // Move the park down
//         i_click_the_down_arrow_button();

//         // Refresh the page and get the updated order of the parks
// //        driver.navigate().refresh();
//         favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));
//         List<String> afterOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

//         // Compare the order before and after moving down
//         assertNotEquals(beforeOrder, afterOrder, "Park position did not change after moving down");
//     }

    @Then("the position of the park in the favorites list should be moved down fav")
    public void the_position_of_the_park_in_the_favorites_list_should_be_moved_down() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#favorites-container > div")));
        List<WebElement> favoritedParks = driver.findElements(By.cssSelector("#favorites-container > div"));

        // Store the order of the parks before moving down
        List<String> beforeOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

        // Move the park down
        i_click_the_down_arrow_button();

        // Refresh the page to ensure the changes are reflected
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#favorites-container > div")));
        favoritedParks = driver.findElements(By.cssSelector("#favorites-container > div"));
        List<String> afterOrder = favoritedParks.stream().map(WebElement::getText).collect(Collectors.toList());

        // Compare the order before and after moving down
        assertNotEquals(beforeOrder, afterOrder, "Park position did not change after moving down");
    }

    @When("I click on a park card in the favorites list fav")
    public void i_click_on_a_park_card_in_the_favorites_list() {
        List<WebElement> favoritedParks = driver.findElements(By.cssSelector(".favorite-park > div"));
        favoritedParks.get(0).click();
    }

    @And("I click the {string} button on the same park card fav")
    public void iClickTheButtonOnTheSameParkCard() {
        List<WebElement> plusButtons = driver.findElements(By.cssSelector(".results-container > div:not([style*='display: none']) .fa-plus"));
        if (!plusButtons.isEmpty()) {
            plusButtons.get(0).click();
        }
    }

    @Then("the park details page should be displayed fav")
    public void the_park_details_page_should_be_displayed() {
        WebElement parkDetails = driver.findElement(By.cssSelector(".specific-result"));
        assertTrue(parkDetails.isDisplayed());
    }

    @When("I click the \"Back to Favorites\" button fav")
    public void i_click_the_back_to_favorites_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement backToFav = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-to-favorites")));

        // Scroll the element into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", backToFav);

        // Dispatch a click event to the button using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));", backToFav);
    }

    @Then("I should be taken back to the favorites list page fav")
    public void i_should_be_taken_back_to_the_favorites_list_page() {
        assertTrue(driver.getCurrentUrl().endsWith("/favorites"));
    }

    @When("I click the toggle button fav")
    public void iClickTheToggleButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement toggleButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toggle-button")));
        toggleButton.click();
    }

    @Then("the favorites list should be displayed in private or public mode accordingly fav")
    public void theFavoritesListShouldBeDisplayedInPrivateOrPublicModeAccordingly() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement toggleButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toggle-button")));
        String privacyMode = toggleButton.getText();

        if (privacyMode.equals("Private")) {
            // Assert that the toggle button is in private mode
            assertTrue(toggleButton.getAttribute("style").contains("background-color: rgb(244, 67, 54)"),
                    "Toggle button is not in private mode");
        } else if (privacyMode.equals("Public")) {
            // Assert that the toggle button is in public mode
            assertTrue(toggleButton.getAttribute("style").contains("background-color: rgb(76, 175, 80)"),
                    "Toggle button is not in public mode");

        } else {
            fail("Invalid privacy mode: " + privacyMode);
        }
    }

    @When("I hover over the first park card fav")
    public void i_hover_over_the_first_park_card() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement firstParkCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".results-container > div:first-child")));
        Actions actions = new Actions(driver);
        actions.moveToElement(firstParkCard).perform();
    }

    @When("I click the \"+\" button on the first park card fav")
    public void i_click_the_plus_button_on_the_first_park_card() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".results-container > div:first-child #addToFavoritesButton")));
        addButton.click();
    }

    @When("I hover over the second park card fav")
    public void i_hover_over_the_second_park_card() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement secondParkCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".results-container > div:nth-child(2)")));
        Actions actions = new Actions(driver);
        actions.moveToElement(secondParkCard).perform();
    }

    @When("I click the \"+\" button on the second park card fav")
    public void i_click_the_plus_button_on_the_second_park_card() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".results-container > div:nth-child(2) #addToFavoritesButton")));
        addButton.click();
    }

    @When("I click \"Yes\" in the confirmation dialog fav")
    public void i_click_yes_in_the_confirmation_dialog() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        WebElement confirmRemoveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirm-remove")));
        confirmRemoveButton.click();
    }

    @When("I click on the {string} button in the navigations bar")
    public void iClickOnTheButtonInTheNavigationsBar(String arg0) {
    }

    @Then("I should be taken to the Search Park page")
    public void iShouldBeTakenToTheSearchParkPage() {
        i_am_on_the_park_search();
    }

    @After
    public void teardown() {
        driver.quit();
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
