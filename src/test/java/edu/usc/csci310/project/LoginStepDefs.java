package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.UUID;

public class LoginStepDefs {
    public WebDriver driver;
    public String username;
    public String correctPassword;

    @Before
    public void initChromedriver() {
        this.driver = new ChromeDriver();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100));
        this.driver.get("http://localhost:8080/signup");
        WebElement usernameElement = this.driver.findElement(By.id("username"));
        WebElement passwordElement = this.driver.findElement(By.id("password"));
        WebElement confirmPasswordElement = this.driver.findElement(By.id("confirm-password"));
        WebElement signupButton = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("signupButton")));
        this.username = UUID.randomUUID().toString();
        this.correctPassword = UUID.randomUUID().toString() + "!A1n";
        usernameElement.sendKeys(this.username);
        passwordElement.sendKeys(this.correctPassword);
        confirmPasswordElement.sendKeys(this.correctPassword);
        signupButton.sendKeys(Keys.ENTER);

    }

    @Given("I am on the Login page")
    public void onLogin() {
        this.driver.get("http://localhost:8080/login");
    }

    @When("I enter valid credentials")
    public void loginValidCreds() {
        WebElement usernameElement = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordElement = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        usernameElement.clear();
        passwordElement.clear();
        usernameElement.sendKeys(this.username);
        passwordElement.sendKeys(this.correctPassword);
    }

    @And("I submit the form")
    public void pressLogin() {
        WebElement button = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("loginButton")));
        button.sendKeys(Keys.ENTER);
    }

    @When("I enter incorrect credentials")
    public void incorrectLogin() {
        WebElement usernameElement = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordElement = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        usernameElement.clear();
        passwordElement.clear();
        usernameElement.sendKeys("Tommy00001");
        passwordElement.sendKeys("yoyo02!");
    }

    @And("I have already failed to login {int} times within the last minute")
    public void failedLoginAttempts(int times) {
        for (int i = 0; i < times; i++) {
            this.incorrectLogin();
            this.pressLogin();
        }
    }

    @When("I enter incorrect credentials for the third time")
    public void enterIncorrectCredentialsThirdTime() {
        this.incorrectLogin();
        this.pressLogin();
    }

    @Then("I should see an error saying login failed")
    public void verifyLoginFailedError() {
        WebElement error = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("error")));
        String expectedMessage = "Invalid credentials. 2 attempt(s) remaining.";
        Assertions.assertEquals(expectedMessage, error.getText());
    }

    @Then("I should see an error message saying {string}")
    public void verifyErrorMessage(String expectedMessage) {
        WebElement error = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("error")));
        Assertions.assertEquals(expectedMessage, error.getText());
    }

    @And("the login form should be disabled")
    public void verifyLoginFormDisabled() {
        WebElement loginButton = this.driver.findElement(By.id("loginButton"));
        Assertions.assertFalse(loginButton.isEnabled());
    }

    @When("I wait for {int} seconds")
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("I refresh the page")
    public void refreshPage() {
        this.driver.navigate().refresh();
    }

    @Then("I should still see the error message {string}")
    public void verifyErrorMessagePersists(String expectedMessage) {
        WebElement error = new WebDriverWait(this.driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("error")));
        Assertions.assertEquals(expectedMessage, error.getText());
    }

    @And("the login form should still be disabled")
    public void verifyLoginFormStillDisabled() {
        WebElement loginButton = this.driver.findElement(By.id("loginButton"));
        Assertions.assertFalse(loginButton.isEnabled());
    }

    @When("I wait for another {int} seconds \\(totaling {int} seconds since the last failed attempt)")
    public void waitForTotalSeconds(int additionalSeconds, int totalSeconds) {
        try {
            Thread.sleep(additionalSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("I should not see any error message")
    public void verifyNoErrorMessage() {
        boolean isErrorDisplayed = this.driver.findElements(By.id("error")).size() > 0;
        Assertions.assertFalse(isErrorDisplayed);
    }

    @And("the login form should be enabled")
    public void verifyLoginFormEnabled() {
        WebElement loginButton = this.driver.findElement(By.id("loginButton"));
        Assertions.assertTrue(loginButton.isEnabled());
    }

    @And("I should be able to enter credentials and submit the form")
    public void enterCredentialsAndSubmitForm() {
        this.loginValidCreds();
        this.pressLogin();
    }

    @Then("I should be redirected to the {string} page from the login page")
    public void loginSearchPage(String path) {
        driver.get("http://localhost:8080/" + path);
        new WebDriverWait(this.driver, Duration.ofSeconds(100)).until(ExpectedConditions.urlContains(path));
        Assertions.assertTrue(this.driver.getCurrentUrl().contains(path));
    }

    @And("I click the go to signup button")
    public void goToSignup() {
        WebElement loginButton = this.driver.findElement(By.className("signup-link"));
        loginButton.click();
    }

    @After
    public void after() {
        //this.driver.close();
        this.driver.quit();
    }
}