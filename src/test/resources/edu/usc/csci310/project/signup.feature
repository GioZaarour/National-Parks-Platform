Feature: Signing up for the application
  Scenario: Signup with unused credentials
    Given I am on the Signup page
    When I sign up with new credentials
    Then I should be redirected to the "login" page from the signup page

  Scenario: Signup with an already taken username
    Given I am on the Signup page
    When I enter a used username
    And I enter a used password
    And I enter a used confirm password
    And I press the Signup button
    Then I can not sign up because the username is already in use

  Scenario: Signup with non-matching passwords
    Given I am on the Signup page
    When I enter a used username
    And I enter a used password
    And I enter a non-matching confirm password
    And I press the Signup button
    Then I should see an error saying "do not match." on the signup page

  Scenario: Signup with a password that does not meet the requirements
    Given I am on the Signup page
    When I enter a used username
    And I enter the password "Tommy1"
    And I enter the confirm password "Tommy1"
    And I press the Signup button
    Then I should see an error saying "must contain one symbol." on the signup page

  Scenario: Signup with cancel signup
    Given I am on the Signup page
    And I press the cancel signup button
    And I click the confirm cancel button
    Then I should be redirected to the "login" page from the signup page


  Scenario: Signup with cancel cancel signup
    Given I am on the Signup page
    And I press the cancel signup button
    And I click the cancel cancel button on Signup
    Then I should be still on the Signup Page