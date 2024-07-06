Feature: Login authentication and management
  Scenario: Login with correct credentials
    Given I am on the Login page
    When I enter valid credentials
    And I submit the form
    Then I should be redirected to the "search" page from the login page

  Scenario: Login attempt with incorrect credentials
    Given I am on the Login page
    When I enter incorrect credentials
    And I submit the form
    Then I should see an error message saying "Invalid credentials. 2 attempt(s) remaining."

  Scenario: Lock out user after 3 failed login attempts within 30sec
    Given I am on the Login page
    And I have already failed to login 2 times within the last minute
    When I enter incorrect credentials for the third time
    Then I should see an error message saying "Too many failed attempts. Please try again later."
    And the login form should be disabled

  Scenario: Redirect to the Signup Page
    Given I am on the Login page
    And I click the go to signup button
    Then I should be redirected to the "signup" page from the login page

