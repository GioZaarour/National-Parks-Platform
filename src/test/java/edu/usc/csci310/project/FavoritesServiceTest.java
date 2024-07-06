package edu.usc.csci310.project;

import edu.usc.csci310.project.DTO.UserParkPair;
import edu.usc.csci310.project.service.FavoritesService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.usc.csci310.project.model.Favorites;
import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.repository.FavoritesRepository;
import edu.usc.csci310.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FavoritesServiceTest {

    @Autowired
    private FavoritesService favoritesService;

    @MockBean
    private FavoritesRepository favoritesRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password");
        user.setId(1L);
    }

    @Test
    void testFetchUserFavorites() {
        when(favoritesRepository.findFavoritesByUsername("testUser")).thenReturn(Arrays.asList("Park1", "Park2"));
        List<String> favorites = favoritesService.fetchUserFavorites("testUser");
        assertEquals(2, favorites.size(), "Should fetch two favorites");
        assertTrue(favorites.contains("Park1"));
        assertTrue(favorites.contains("Park2"));
    }

    @Test
    void testFetchUserFavoritesAndReturnFavoriteObject() {
        Favorites favorite = new Favorites(user, "Park1", 1, "api123");
        when(favoritesRepository.fetchUserFavoritesAndReturnFavoriteObject("testUser")).thenReturn(Arrays.asList(favorite));
        List<Favorites> favorites = favoritesService.fetchUserFavoritesAndReturnFavoriteObject("testUser");
        assertNotNull(favorites, "Favorites list should not be null");
        assertEquals(1, favorites.size(), "Should return one favorite");
        assertEquals("Park1", favorites.get(0).getParkName());
    }

    @Test
    void testFetchGroupFavorites() {
        List<String> usernames = Arrays.asList("user1", "user2");
        when(favoritesRepository.findFavoritesByManyUsernames(usernames)).thenReturn(
                Arrays.asList(
                        new UserParkPair("user1", "Park1", 1, "api1"),
                        new UserParkPair("user1", "Park2", 2, "api2"),
                        new UserParkPair("user2", "Park1", 1, "api1")
                )
        );

        Map<String, List<String>> results = favoritesService.fetchGroupFavorites(usernames);

        assertNotNull(results, "Results should not be null");
        assertEquals(2, results.size(), "Should contain entries for two users");
        assertEquals(Arrays.asList("Park1", "Park2"), results.get("user1"), "Should return sorted parks for user1");
        assertEquals(Arrays.asList("Park1"), results.get("user2"), "Should return sorted parks for user2");
    }

    @Test
    void testAddFavorite() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        doAnswer(invocation -> {
            Favorites fav = invocation.getArgument(0);
            assertEquals("Park1", fav.getParkName());
            return null;
        }).when(favoritesRepository).save(any(Favorites.class));

        favoritesService.addFavorite("testUser", "Park1", 1, "api123");
        verify(favoritesRepository, times(1)).save(any(Favorites.class));
    }

    @Test
    void testUpdateFavoriteRanking() {
        List<Favorites> favoritesList = Arrays.asList(
                new Favorites(user, "Park1", 1, "api1"),
                new Favorites(user, "Park2", 2, "api2"),
                new Favorites(user, "Park3", 3, "api3")
        );

        when(favoritesRepository.findFavoritesByUsernameOrderedByRanking("testUser")).thenReturn(favoritesList);
        doAnswer(invocation -> {
            Favorites fav = invocation.getArgument(0);
            assertNotNull(fav);
            return null;
        }).when(favoritesRepository).save(any(Favorites.class));

        favoritesService.updateFavoriteRanking("testUser", "Park2", 3);

        assertEquals(3, favoritesList.get(1).getRanking(), "Park2 should now be at ranking 3");
        assertEquals(2, favoritesList.get(2).getRanking(), "Park3 should have moved up to ranking 2");
    }

    @Test
    void testRemoveFavorite() {
        doNothing().when(favoritesRepository).removeFromFavorites(1);

        favoritesService.removeFavorite(1);

        verify(favoritesRepository).removeFromFavorites(1);
    }

    @Test
    void testSuggestFavoritePark() {
        List<String> usernames = Arrays.asList("user1", "user2");
        when(favoritesRepository.findFavoritesByManyUsernames(usernames)).thenReturn(Arrays.asList(
                new UserParkPair("user1", "Park1", 1, "api1"),
                new UserParkPair("user2", "Park1", 1, "api1")
        ));

        String suggestedPark = favoritesService.suggestFavoritePark(usernames);
        assertEquals("api1", suggestedPark, "api1 should be the suggested park as it is common and has the lowest ranking");
    }

}
