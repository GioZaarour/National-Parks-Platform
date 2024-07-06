Feature: Favorites Functionality fav
  Scenario: Go to favorites page fav
    Given I am on the Park Search fav
    When I click the "Favorites" button fav
    Then I should be taken to the favorites page fav

  Scenario: Add a park to favorites fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    When I click the park name option fav
    And I press the search button fav
    And I hover over a park card fav
    When I click the "+" button on a park card fav
    Then a success message should be displayed fav

  Scenario: Add a duplicate park to favorites fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    When I click the park name option fav
    And I press the search button fav
    And I hover over a park card fav
    When I click the "+" button on a park card fav
    Then a success message should be displayed fav
    When I click the "+" button on a park card fav
    Then an alert should be displayed stating that the park is already in my favorites list fav

  Scenario: Remove a park from favorites fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    And I click the park name option fav
    And I press the search button fav
    And I hover over the first park card fav
    And I click the "+" button on the first park card fav
    Then a success message should be displayed fav
    When I hover over the second park card fav
    And I click the "+" button on the second park card fav
    Then a success message should be displayed fav
    When I click the "Favorites" button fav
    Then I should be taken to the favorites page fav
    When I hover over a park card in the favorites list fav
    And I click the "x" button fav
    When I click "Yes" in the confirmation dialog fav
    Then the park should be removed from my favorites list fav

  Scenario: Remove all parks from favorites fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    And I click the park name option fav
    And I press the search button fav
    And I hover over the first park card fav
    And I click the "+" button on the first park card fav
    Then a success message should be displayed fav
    When I hover over the second park card fav
    And I click the "+" button on the second park card fav
    Then a success message should be displayed fav
    When I click the "Favorites" button fav
    When I click on the delete all favorites button fav
    When I click the confirm delete all favorites button
    Then I should see all favorites were deleted

  Scenario: Sort favorites by moving a park up fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    And I click the park name option fav
    And I press the search button fav
    And I hover over the first park card fav
    And I click the "+" button on the first park card fav
    Then a success message should be displayed fav
    When I hover over the second park card fav
    And I click the "+" button on the second park card fav
    Then a success message should be displayed fav
    When I click the "Favorites" button fav
    Then I should be taken to the favorites page fav
    When I hover over the 2nd  park card in the favorites list fav
    Then the position of the park in the favorites list should be moved up fav

  Scenario: Sort favorites by moving a park down fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    And I click the park name option fav
    And I press the search button fav
    And I hover over the first park card fav
    And I click the "+" button on the first park card fav
    Then a success message should be displayed fav
    When I hover over the second park card fav
    And I click the "+" button on the second park card fav
    Then a success message should be displayed fav
    When I click the "Favorites" button fav
    Then I should be taken to the favorites page fav
    When I hover over a park card in the favorites list fav
    Then the position of the park in the favorites list should be moved down fav

  Scenario: Navigate back to favorites list from park details fav
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    And I click the park name option fav
    And I press the search button fav
    And I hover over the first park card fav
    And I click the "+" button on the first park card fav
    Then a success message should be displayed fav
    When I click the "Favorites" button fav
    Then I should be taken to the favorites page fav
    When I hover over a park card in the favorites list fav
    And I click on a park card in the favorites list fav
    When I click the "Back to Favorites" button fav
    Then I should be taken back to the favorites list page fav

  Scenario: Toggle favorites list privacy fav private to public, public to private
    Given I am on the Park Search fav
    When I enter "Yosemite" into the search field fav
    And I click the park name option fav
    And I press the search button fav
    And I hover over the first park card fav
    And I click the "+" button on the first park card fav
    Then a success message should be displayed fav
    When I click the "Favorites" button fav
    Then I should be taken to the favorites page fav
    When I click the toggle button fav
    When I click the toggle button fav
    Then the favorites list should be displayed in private or public mode accordingly fav

  Scenario: Navigate to the Search Parks page
    Given I am on the Favorites Page fav
    When I click on the "Search" button in the navigation bars
    Then I should be taken to the "Search" page after

  Scenario: Navigate to the Suggest a Park page
    Given I am on the Favorites Page fav
    When I click on the "Suggest" button in the navigation bars
    Then I should be taken to the "Suggest" page after

  Scenario: Navigate to the Compare a Park page
    Given I am on the Favorites Page fav
    When I click on the "Compare" button in the navigation bars
    Then I should be taken to the "Compare" page after

  Scenario: Navigate to the Favorites a Park page
    Given I am on the Favorites Page fav
    When I click on the "Favorites" button in the navigation bars
    Then I should be taken to the "Favorites" page after

  Scenario: Logout from the application
    Given I am on the Favorites Page fav
    When I click on the "Login" button in the navigation bars
    Then I should be taken to the "Login" page after
