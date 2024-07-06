Feature: Comparing favorite parks

  Scenario: Search and select a user to compare favorites com
    And I am on the Compare Favorites page com
    When I enter the one username into the search com
    And I press on the search button com
    Then I should see the first username with a compare button com

  Scenario: Search and select a group to compare favorites com
    Given I am on the Compare Favorites page com
    When I enter the first and second username into the search com
    And I press on the search button com
    Then I should see the group with a compare button com

  Scenario: No results found for the search com
    Given I am on the Compare Favorites page com
    When I enter a user or group name not in the database com
    And I press on the search button com
    Then I should see an error message

  Scenario: searching for private user
    Given I am on the Compare Favorites page com
    When I enter a user that is private
    And I press on the search button com
    Then I should see a alert telling me the user is private

  Scenario: navigating to search page
    Given I am on the Compare Favorites page com
    When I press on the search page button
    Then I am on the search page

  Scenario: navigating to favorites page
    Given I am on the Compare Favorites page com
    When I press on the favorites page button
    Then I am on the favorites page

  Scenario: navigating to suggest page
    Given I am on the Compare Favorites page com
    When I press on the suggest page button
    Then I am on the suggest page

  Scenario: logging out
    Given I am on the Compare Favorites page com
    When I press on the logout  button
    Then I am logged out

  Scenario: Compare favorites for a selected user com
    Given I am on the Compare Favorites page com
    When I enter the one username into the search com
    And I press on the search button com
    And I press the compare button com
    Then I should see all of the parks that logged in user has with first user

  Scenario: Compare favorites for a selected group com
    Given I am on the Compare Favorites page com
    When I enter the first and second username into the search com
    And I press on the search button com
    And I press the compare button com
    Then I should see all of the parks that the group "group1" have in common com

  Scenario: Close the favorite view of a park com
    Given I am on the Compare Favorites page com
    When I enter the one username into the search com
    And I press on the search button com
    And I press the compare button com
    And I press on the ratio of a park com
    And I click the "x" button on the card com
    Then the card should close com
    And I should return to the comparison results list com

  Scenario: close the detailed view of park com
    Given I am on the Compare Favorites page com
    When I enter the one username into the search com
    And I press on the search button com
    And I press the compare button com
    And I click on a park com
    And I click on the "Back to Search Results" button com
    Then I should return to the comparison results list com

  Scenario: View detailed results Location com
    Given I am on the Compare Favorites page com
    When I enter the one username into the search com
    And I press on the search button com
    And I press the compare button com
    And I click on a park com
    Then I should see park's location com

  Scenario: Navigate to the Search Parks page
    Given I am on the Compare Search page
    When I click on the "Search" button in the navigation bars
    Then I should be taken to the "Search" page after

  Scenario: Navigate to the Suggest a Park page
    Given I am on the Compare Search page
    When I click on the "Suggest" button in the navigation bars
    Then I should be taken to the "Suggest" page after

  Scenario: Navigate to the Compare a Park page
    Given I am on the Compare Search page
    When I click on the "Compare" button in the navigation bars
    Then I should be taken to the "Compare" page after

  Scenario: Navigate to the Favorites a Park page
    Given I am on the Compare Search page
    When I click on the "Favorites" button in the navigation bars
    Then I should be taken to the "Favorites" page after

  Scenario: Logout from the application
    Given I am on the Compare Search page
    When I click on the "Login" button in the navigation bars
    Then I should be taken to the "Login" page after

=======

Feature: Comparing favorite parks
  Scenario: Search and select a user to compare favorites
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    Then I should see the user "user1" with a compare button

  Scenario: Search and select a group to compare favorites
    Given I am on the Compare Favorites page
    When I enter "group1" into the search
    And I press on the search button
    Then I should see the group "group1" with a compare button

  Scenario: Compare favorites for a selected user
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    Then I should see all of the parks that user "user1" has in common

  Scenario: Compare favorites for a selected group
    Given I am on the Compare Favorites page
    When I enter "group1" into the search
    And I press on the search button
    And I press the compare button
    Then I should see all of the parks that the group "group1" have in common

  Scenario: View detailed results Location
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I click on a park
    Then I should see park's location

  Scenario: View detailed  results URL
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I click on a park
    Then I should see park's URL

  Scenario: View detailed  results entrance fee
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I click on a park
    Then I should see park's entrance fee

  Scenario: View detailed  results amenities
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I click on a park
    Then I should see park's amenities

  Scenario: View detailed  results activities
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I click on a park
    Then I should see park's activities

  Scenario: No results found for the search
    Given I am on the Compare Favorites page
    When I enter an "unknown" user or group name
    And I press on the search button
    Then I shouldn't see any users or groups

  Scenario: See who favorited park
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I press on the ratio of a park
    Then I should see the who favorited the park

  Scenario: Close the favorite view of a park
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I press on the ratio of a park
    And I click the "x" button on the card
    Then the card should close
    And I should return to the comparison results list

  Scenario: close the detailed view of park
    Given I am on the Compare Favorites page
    When I enter "user1" into the search
    And I press on the search button
    And I press the compare button
    And I click on a park
    And I click on the "Back to Search Results" button
    Then I should return to the comparison results list
