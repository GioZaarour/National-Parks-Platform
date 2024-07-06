package edu.usc.csci310.project;

import edu.usc.csci310.project.DTO.UserParkPair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserParkPairTest {

    @Test
    void testConstructorAndGetter() {
        // Define the test data
        String username = "testUser";
        String parkName = "Yosemite National Park";
        int ranking = 1;
        String apiId = "1234abcd";

        // Create an instance of UserParkPair
        UserParkPair userParkPair = new UserParkPair(username, parkName, ranking, apiId);

        // Assert that the getters return the correct values
        assertEquals(username, userParkPair.getUsername(), "The username should match the constructor input.");
        assertEquals(parkName, userParkPair.getParkName(), "The park name should match the constructor input.");
        assertEquals(apiId, userParkPair.getApiId(), "The API ID should match the constructor input.");
        assertEquals(ranking, userParkPair.getRanking(), "The ranking should match the constructor input.");
    }
}
