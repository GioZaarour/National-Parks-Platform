package edu.usc.csci310.project;

import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.model.Favorites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FavoritesTest {

    private User user;
    private Favorites favorites;

    @BeforeEach
    void setUp() {
        // Assuming User class is correctly implemented and working
        user = new User("testUser", "password123");
        user.setId(1L); // Mocking the ID which would normally be set by the database

        favorites = new Favorites(user, "Yosemite National Park", 1, "api123");
    }

    @Test
    void testFavoritesConstructorAndProperties() {
        // Check that the constructor has set all properties correctly
        assertNotNull(favorites.getUser(), "User should not be null");
        assertEquals("Yosemite National Park", favorites.getParkName(), "Park name should be set by constructor");
        assertEquals(1, favorites.getRanking(), "Ranking should be set by constructor");
        assertEquals("api123", favorites.getApiId(), "API ID should be set by constructor");

        // Test setter and getters
        favorites.setApiId("newApi123");
        assertEquals("newApi123", favorites.getApiId(), "Setter for apiId should work correctly");

        favorites.setRanking(2);
        assertEquals(2, favorites.getRanking(), "Setter for ranking should work correctly");

        // Ensure the User link works correctly
        assertEquals(user.getUsername(), favorites.getUser().getUsername(), "User object should link correctly");
    }
}
