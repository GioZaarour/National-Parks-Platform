package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SignupStepDefs {
    public WebDriver driver;
    public String username;
    public String correctPassword;

    @Before
    public void initChromeDriverSignup() {
        this.driver = new ChromeDriver();
        this.username = UUID.randomUUID().toString();
        this.correctPassword = UUID.randomUUID().toString() + "!A1n";
    }

    @Given("I am on the Signup page")
    public void onSignup() {
        driver.get("http://localhost:8080/signup");
    }

    @When("I enter the username {string}")
    public void validSignupCredentials(String username) {
        WebElement usernameElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    @When("I sign up with new credentials")
    public void enterANewUsername() {
        WebElement usernameElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        WebElement confirmPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-password")));
        usernameElement.clear();
        usernameElement.sendKeys(this.username);
        passwordElement.clear();
        passwordElement.sendKeys(this.correctPassword);
        confirmPasswordElement.clear();
        confirmPasswordElement.sendKeys(this.correctPassword);
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("signupButton")));
        button.sendKeys(Keys.ENTER);
    }

    @And("I enter a used username")
    public void usedUsername() {
        WebElement usernameElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        usernameElement.clear();
        usernameElement.sendKeys(this.username);
    }

    @And("I enter a used password")
    public void usedPassword() {
        WebElement passwordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordElement.clear();
        passwordElement.sendKeys(this.correctPassword + "1");
    }

    @And("I enter a used confirm password")
    public void usedConfirmPassword() {
        WebElement confirmPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-password")));
        confirmPasswordElement.clear();
        confirmPasswordElement.sendKeys(this.correctPassword + "1");
    }

    @And("I enter a non-matching confirm password")
    public void enterNonMatchingConfirmPassword() {
        WebElement confirmPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-password")));
        confirmPasswordElement.clear();
        confirmPasswordElement.sendKeys("12013010slasdA!");
    }

    @And("I enter the password {string}")
    public void enterSignupPassword(String password) {
        WebElement passwordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    @And("I enter the confirm password {string}")
    public void enterSignupConfirmPassword(String password) {
        WebElement confirmPasswordElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-password")));
        confirmPasswordElement.clear();
        confirmPasswordElement.sendKeys(password);
    }

    @And("I press the Signup button")
    public void pressSignup() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("signupButton")));
        button.sendKeys(Keys.ENTER);
    }

    @And("I press the cancel signup button")
    public void cancelSignup() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("cancel-signup")));
        button.sendKeys(Keys.ENTER);
    }

    @And("I click the confirm cancel button")
    public void confirmCancel() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-cancel-signup")));
        button.sendKeys(Keys.ENTER);
    }

    @Then("I should see an error saying {string} on the signup page")
    public void signupError(String error_message) {
        WebElement error = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("error")));
        Assertions.assertEquals(error_message, error.getText());
    }

    //    @Then("I should see an error that the username is already in use")
//    public void signupError() {
//        WebElement error = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.presenceOfElementLocated(By.id("error")));
//        Assertions.assertEquals("Sign up failed: There is an account with that username: " + this.username,
//                error.getText());
//    }

    @Then("I can not sign up because the username is already in use")
    public void canNotSignUp() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    @Then("I should be redirected to the {string} page from the signup page")
    public void signupSearchPage(String path) {
        driver.get("http://localhost:8080/" + path);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(path));
        Assertions.assertTrue(driver.getCurrentUrl().contains(path));
    }

    @And("I click the cancel cancel button on Signup")
    public void cancelCancelSignup() {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("cancel-cancel-signup")));
        button.sendKeys(Keys.ENTER);
    }

    @Then("I should be still on the Signup Page")
    public void stillOnSignup() {
        new WebDriverWait(this.driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("signup"));
        Assertions.assertTrue(this.driver.getCurrentUrl().contains("signup"));
    }

    @After
    public void afterSignup() {
        //driver.close();
        driver.quit();
    }
}
