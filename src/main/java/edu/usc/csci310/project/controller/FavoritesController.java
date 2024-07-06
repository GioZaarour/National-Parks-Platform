package edu.usc.csci310.project.controller;

import edu.usc.csci310.project.DTO.UsernameRequest;
import edu.usc.csci310.project.DTO.IdDTO;
import edu.usc.csci310.project.model.Favorites;
import edu.usc.csci310.project.service.FavoritesService;
import lombok.AllArgsConstructor;
import edu.usc.csci310.project.DTO.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import org.springframework.http.HttpStatus;

@RestController("FavoritesController")
@CrossOrigin
@AllArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;

    // Fetch favorite parks of a user
    @GetMapping("/fetchUserFavorites")
    public List<String> fetchUserFavorites(@RequestParam String username) {
        return favoritesService.fetchUserFavorites(username);
    }

    @GetMapping("/fetchUserFavoritesAndReturnFavoriteObject")
    public List<Favorites> fetchUserFavoritesAndReturnFavoriteObject(@RequestParam String username) {
        return favoritesService.fetchUserFavoritesAndReturnFavoriteObject(username);
    }

    // Fetch favorite parks of a group of users
    // POST mapping instead since it allows us to send a JSON body with the request
    @PostMapping("/fetchGroupFavorites")
    public Map<String, List<String>> fetchGroupFavorites(@RequestBody List<String> usernames) {
        return favoritesService.fetchGroupFavorites(usernames);
    }

    // Add a favorite park to a user
    @PostMapping("/addFavorite")
    public void addFavorite(@RequestBody FavoriteRequest favoriteRequest) {
    favoritesService.addFavorite(favoriteRequest.getUsername(), favoriteRequest.getParkName(),
            favoriteRequest.getRanking(), favoriteRequest.getApiId());
    }



    // Update the ranking of a favorite park for a user. Will shift all the other favorite rankings accordingly, assuming the rankings are dense
    @PostMapping("/updateFavorite")
    public ResponseEntity<?> updateFavorite(@RequestBody FavoriteRequest updateRequest) {
        try {
            favoritesService.updateFavoriteRanking(updateRequest.getUsername(), updateRequest.getParkName(), updateRequest.getRanking());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/deleteAllFavorites")
    public ResponseEntity<?> deleteAllFavorites(@RequestBody UsernameRequest request) {
        try {
            favoritesService.deleteAllFavorites(request.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/removeFavorite")
    public ResponseEntity<?> removeFavorite(@RequestBody IdDTO idDTO) {
        try {
            favoritesService.removeFavorite(idDTO.getId());
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Suggest a favorite park for a group of users
    @PostMapping("/suggestFavorite")
    public ResponseEntity<String> suggestFavorite(@RequestBody List<String> usernames) {
        try {
            String suggestedPark = favoritesService.suggestFavoritePark(usernames);
            return ResponseEntity.ok(suggestedPark);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
