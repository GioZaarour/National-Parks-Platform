package edu.usc.csci310.project;

import edu.usc.csci310.project.DTO.FavoriteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FavoriteRequestTest {
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    void setUp() {
        favoriteRequest = new FavoriteRequest();
    }

    @Test
    void testSetAndGetUsername() {
        String expected = "testUser";
        favoriteRequest.setUsername(expected);
        assertEquals(expected, favoriteRequest.getUsername(), "The username should match the set value.");
    }

    @Test
    void testSetAndGetParkName() {
        String expected = "Yellowstone";
        favoriteRequest.setParkName(expected);
        assertEquals(expected, favoriteRequest.getParkName(), "The park name should match the set value.");
    }

    @Test
    void testSetAndGetApiId() {
        String expected = "12345";
        favoriteRequest.setApiId(expected);
        assertEquals(expected, favoriteRequest.getApiId(), "The API ID should match the set value.");
    }

    @Test
    void testSetAndGetRanking() {
        int expected = 1;
        favoriteRequest.setRanking(expected);
        assertEquals(expected, favoriteRequest.getRanking(), "The ranking should match the set value.");
    }
}
