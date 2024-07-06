Feature: Suggest a park to visit among a group of friends
  Scenario: User suggests a park to visit with friends
    Given the user is on the search page
    When the user clicks on the suggest link in the navigation bar
    Then the user is redirected to the park suggestion page

  Scenario: User enters a large number of valid friend usernames (showing no limit on number of friends to search) and
  has the suggestion algorithm select the best match and clicks on the suggested park display
    Given the user is on the park suggestion page
    When the user enters a large number of valid friend usernames
    And clicks the search park button on suggest
    And clicks the suggest park button on suggest
    And a park has been suggested by the suggestion algorithm that is in the friends' lists and has the highest average among all friends' lists and displays the park name, location, and three pictures
    When the user clicks on the park display expanded details about the park should appear
    And when I click on back to suggested park the expanded details about the park disappear

  Scenario: User enters an invalid friend username
    Given the user is on the park suggestion page
    When the user enters an invalid friend username
    Then clicks the search park button on suggest and sees an error message indicating the invalid list

  Scenario: User enters a valid friend username
    Given the user is on the park suggestion page
    When the user enters a valid friend username
    And clicks the search park button on suggest
    And clicks the suggest park button on suggest
    Then a park has been suggested by the suggestion algorithm that is in the friends' lists and has the highest average among all friends' lists and displays the park name, location, and three pictures

  Scenario: User enters a friend username with a private list
    Given the user is on the park suggestion page
    When the user enters a friend username with a private list
    Then clicks the search park button on suggest and sees an error message indicating the private list

  Scenario: Navigate to the Search Parks page
    Given I am on the Suggest Search page
    When I click on the "Search" button in the navigation bars
    Then I should be taken to the "Search" page after

  Scenario: Navigate to the Suggest a Park page
    Given I am on the Suggest Search page
    When I click on the "Suggest" button in the navigation bars
    Then I should be taken to the "Suggest" page after

  Scenario: Navigate to the Compare a Park page
    Given I am on the Suggest Search page
    When I click on the "Compare" button in the navigation bars
    Then I should be taken to the "Compare" page after

  Scenario: Navigate to the Favorites a Park page
    Given I am on the Suggest Search page
    When I click on the "Favorites" button in the navigation bars
    Then I should be taken to the "Favorites" page after

  Scenario: Logout from the application
    Given I am on the Suggest Search page
    When I click on the "Login" button in the navigation bars
    Then I should be taken to the "Login" page after
