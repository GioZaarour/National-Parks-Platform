Feature: Searching for a park

  Scenario: Search for a park by name
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    And I press the search button
    Then I should see a list of parks including "Yosemite"

  Scenario: Search for a park by state
    Given I am on the Park Search page
    When I enter "CA" into the search field
    When I click the state code option
    And I press the search button
    And I click on a park
    Then I should see a list of parks in "CA"

  Scenario: Search for a park by amenity
    Given I am on the Park Search page
    When I enter "Camping" into the search field
    When I click the park amenities option
    And I press the search button
    And I click on a park

    Then I should see a list of parks that offer "Camping" amenities

  Scenario: Search for a park by activity
    Given I am on the Park Search page
    When I enter "Hiking" into the search field
    When I click the park activities option
    And I press the search button
    And I click on a park

    Then I should see a list of parks that offer "Hiking" activities

  Scenario: Load more results on the park search page
    Given I am on the Park Search page
    When I enter "CA" into the search field
    When I click the state code option
    And I press the search button
    And I click on the load more button
    Then I should see an additional set of 10 parks added to the list

  Scenario: Viewing details of a selected park
    Given I am on the Park Search page
    When I enter "CA" into the search field
    When I click the state code option
    And I press the search button
    And I click on a park
    Then I should see the location
    And I should see URL
    And I should see the Entrance Fee
    And I should see the Amenities
    And I should see the Activities
    And I have an option to go back to the search results

  Scenario: No results found for the search
    Given I am on the Park Search page
    When I enter "4704472784087" into the search field
    When I click the park name option
    And I press the search button
    Then I shouldn't see any parks

  Scenario: 10 parks show up when I search
    Given I am on the Park Search page
    When I enter "Hiking" for the activities option
    And I press the search button
    Then I should see 10 parks listed for the initial search result

  Scenario: Searching by state from park details
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    And I click the park name option
    And I press the search button
    And I click on a park with the name "Yosemite"
    Then I should see the location
    When I click on the state button in the park details
    Then I should see a list of parks in "CA"

  Scenario: Searching by amenity from park details
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    And I click the park name option
    And I press the search button
    And I click on a park with the name "Yosemite"
    Then I should see the Amenities
    When I click on an amenity button in the park details
    Then I should see a list of parks that offer the selected amenity

  Scenario: Searching by activity from park details
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    And I click the park name option
    And I press the search button
    And I click on a park with the name "Yosemite"
    Then I should see the Activities
    When I click on an activity button in the park details
    Then I should see a list of parks that offer the selected activity


  Scenario: Search for a park by name using enter
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    When I click the park name option
    And I press the enter key
    Then I should see a list of parks including "Yosemite"

  Scenario: Minimize park details by clicking the park name
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    And I click the park name option
    And I press the search button
    And I click on a park with the name "Yosemite"
    Then I should see the location
    When I click on the park name in the park details
    Then the park details should be closed

  Scenario: Hovering over park cards while details page is open
    Given I am on the Park Search page
    When I enter "Yosemite" into the search field
    And I click the park name option
    And I press the search button
    And I click on a park with the name "Yosemite"
    Then I should see the location
    When I hover over a park card
    Then the "Add to Favorites" button should be displayed for that park card

  Scenario: Navigate to the Search Parks page
    Given I am on the Park Search page
    When I click on the "Search" button in the navigation bars
    Then I should be taken to the "Search" page after

  Scenario: Navigate to the Suggest a Park page
    Given I am on the Park Search page
    When I click on the "Suggest" button in the navigation bars
    Then I should be taken to the "Suggest" page after

  Scenario: Navigate to the Compare a Park page
    Given I am on the Park Search page
    When I click on the "Compare" button in the navigation bars
    Then I should be taken to the "Compare" page after

  Scenario: Navigate to the Favorites a Park page
    Given I am on the Park Search page
    When I click on the "Favorites" button in the navigation bars
    Then I should be taken to the "Favorites" page after

  Scenario: Logout from the application
    Given I am on the Park Search page
    When I click on the "Login" button in the navigation bars
    Then I should be taken to the "Login" page after
