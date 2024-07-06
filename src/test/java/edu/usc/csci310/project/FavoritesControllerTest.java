package edu.usc.csci310.project;
import edu.usc.csci310.project.DTO.UsernameRequest;
import edu.usc.csci310.project.controller.FavoritesController;
import edu.usc.csci310.project.DTO.FavoriteRequest;
import edu.usc.csci310.project.DTO.IdDTO;
import edu.usc.csci310.project.model.Favorites;
import edu.usc.csci310.project.service.FavoritesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FavoritesControllerTest {

    @Mock
    private FavoritesService favoritesService;

    @InjectMocks
    private FavoritesController favoritesController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favoritesController).build();
    }

    @Test
    public void whenFetchUserFavorites_thenReturnsList() throws Exception {
        String username = "testUser";
        List<String> mockFavorites = Collections.singletonList("park1");
        given(favoritesService.fetchUserFavorites(username)).willReturn(mockFavorites);

        mockMvc.perform(get("/fetchUserFavorites")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().json("['park1']"));

        verify(favoritesService).fetchUserFavorites(username);
    }

    @Test
    public void whenAddFavorite_thenAddsFavorite() throws Exception {
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUsername("user1");
        favoriteRequest.setParkName("parkName1");
        favoriteRequest.setRanking(1);
        favoriteRequest.setApiId("apiId1");

        mockMvc.perform(post("/addFavorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"user1\", \"parkName\": \"parkName1\", \"ranking\": 1, \"apiId\": \"apiId1\" }"))
                .andExpect(status().isOk());

        verify(favoritesService).addFavorite("user1", "parkName1", 1, "apiId1");
    }

    // Add more tests for the updateFavorite, removeFavorite, fetchGroupFavorites, suggestFavorite, and any other methods
// ...

    @Test
    public void whenUpdateFavorite_thenUpdatesRanking() throws Exception {
        FavoriteRequest updateRequest = new FavoriteRequest();
        updateRequest.setUsername("user1");
        updateRequest.setParkName("parkName1");
        updateRequest.setRanking(2);

        mockMvc.perform(post("/updateFavorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"user1\", \"parkName\": \"parkName1\", \"ranking\": 2 }"))
                .andExpect(status().isOk());

        verify(favoritesService).updateFavoriteRanking("user1", "parkName1", 2);
    }

    @Test
    public void whenRemoveFavorite_thenFavoriteRemoved() throws Exception {
        int favoriteId = 1;

        mockMvc.perform(post("/removeFavorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1 }"))
                .andExpect(status().isOk());

        verify(favoritesService).removeFavorite(favoriteId);
    }

    @Test
    public void whenFetchGroupFavorites_thenReturnsFavoritesMap() throws Exception {
        List<String> usernames = List.of("user1", "user2");
        Map<String, List<String>> mockGroupFavorites = Map.of(
                "user1", List.of("park1", "park2"),
                "user2", List.of("park2", "park3")
        );

        given(favoritesService.fetchGroupFavorites(usernames)).willReturn(mockGroupFavorites);

        mockMvc.perform(post("/fetchGroupFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"user1\", \"user2\"]"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"user1\": [\"park1\", \"park2\"], \"user2\": [\"park2\", \"park3\"]}"));

        verify(favoritesService).fetchGroupFavorites(usernames);
    }

    @Test
    public void whenSuggestFavorite_thenReturnsSuggestedPark() throws Exception {
        List<String> usernames = List.of("user1", "user2");
        String suggestedPark = "park2";

        given(favoritesService.suggestFavoritePark(usernames)).willReturn(suggestedPark);

        mockMvc.perform(post("/suggestFavorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"user1\", \"user2\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string("park2"));

        verify(favoritesService).suggestFavoritePark(usernames);
    }

    @Test
    public void whenDeleteAllFavorites_thenAllFavoritesRemoved() throws Exception {
        UsernameRequest usernameRequest = new UsernameRequest();
        usernameRequest.setUsername("user1");

        mockMvc.perform(post("/deleteAllFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\"}"))
                .andExpect(status().isOk());

        verify(favoritesService).deleteAllFavorites("user1");
    }

}
