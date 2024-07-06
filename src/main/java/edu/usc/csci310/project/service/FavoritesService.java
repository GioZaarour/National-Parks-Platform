package edu.usc.csci310.project.service;

import edu.usc.csci310.project.DTO.UserParkPair;
import edu.usc.csci310.project.model.Favorites;
import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.repository.FavoritesRepository;
import edu.usc.csci310.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;

    public List<String> fetchUserFavorites(String username) {
        return favoritesRepository.findFavoritesByUsername(username);
    }

    public List<Favorites> fetchUserFavoritesAndReturnFavoriteObject(String username) {
        return favoritesRepository.fetchUserFavoritesAndReturnFavoriteObject(username);
    }

    // public Map<String, List<String>> fetchGroupFavorites(List<String> usernames) {
    //     List<UserParkPair> pairs = favoritesRepository.findFavoritesByManyUsernames(usernames);
    //     Map<String, List<String>> groupedFavorites = new HashMap<>();

    //     pairs.forEach(pair -> {
    //         groupedFavorites.computeIfAbsent(pair.getUsername(), k -> new ArrayList<>()).add(pair.getParkName());
    //     });

    //     return groupedFavorites;
    // }

    // returns a list of ordered favorites for every username in a given list of usernames
    public Map<String, List<String>> fetchGroupFavorites(List<String> usernames) {
        List<UserParkPair> pairs = favoritesRepository.findFavoritesByManyUsernames(usernames);
        Map<String, List<UserParkPair>> groupedFavorites = new HashMap<>();

        // Group UserParkPair objects by username
        pairs.forEach(pair -> {
            groupedFavorites.computeIfAbsent(pair.getUsername(), k -> new ArrayList<>()).add(pair);
        });

        // Sort each user's list of UserParkPair objects by ranking
        for (List<UserParkPair> userFavorites : groupedFavorites.values()) {
            userFavorites.sort(Comparator.comparingInt(UserParkPair::getRanking));
        }

        // transform the sorted lists of UserParkPair objects into lists of park names.
        Map<String, List<String>> sortedGroupedFavorites = new HashMap<>();
        groupedFavorites.forEach((username, userParkPairs) -> {
            List<String> parkNames = userParkPairs.stream()
                    .map(UserParkPair::getParkName)
                    .collect(Collectors.toList());
            sortedGroupedFavorites.put(username, parkNames);
        });

        return sortedGroupedFavorites;
    }

    public void addFavorite(String username, String parkName, int ranking, String apiId) {
        Logger LOGGER =
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Favorites favorite = new Favorites(user, parkName, ranking, apiId);
            // Fetch all favorites for the user ordered by ranking
            List<Favorites> userFavorites = favoritesRepository.findFavoritesByUsernameOrderedByRanking(username);
            int maxRanking = userFavorites.isEmpty() ? 0 : userFavorites.get(userFavorites.size() - 1).getRanking();
            // Assign the new favorite the bottom of the list
            favorite.setRanking(maxRanking + 1);
            favoritesRepository.save(favorite);
        } else {
            LOGGER.log(Level.INFO, "user is null");
            // Handle the case where the user does not exist
            throw new IllegalArgumentException("User does not exist");
        }
    }
    /* NOTE: Here, if the newRanking value is lower than the old ranking (i.e. it was moved to a better ranking),
   then all the favorites with values greater than or equal to newRanking and less than the old ranking should have
   their rankings incremented (i.e. they are shifted down to a worse ranking). If the newRanking value is higher than
   the old ranking, (i.e. it was moved to a worse ranking), then all the favorites with values less than or equal to
   newRanking and greater than old ranking should have their rankings decremented (i.e. they are shifted up to a better ranking).  */
    public void updateFavoriteRanking(String username, String parkName, int newRanking) {
        // Fetch all favorites for the user ordered by ranking
        List<Favorites> favorites = favoritesRepository.findFavoritesByUsernameOrderedByRanking(username);
        Integer oldRanking = null;

        // Find the favorite to update to get the old ranking
        for (Favorites favorite : favorites) {
            if (favorite.getParkName().equals(parkName)) {
                oldRanking = favorite.getRanking();
                break;
            }
        }

        if (oldRanking == null) {
            throw new IllegalStateException("Favorite not found old ranking = null" + favorites + "\n" + username);
        }

        // Determine the direction of the shift
        if (newRanking < oldRanking) {
            // Moved to a better ranking: Increment rankings of those in the new range
            for (Favorites favorite : favorites) {
                if (favorite.getRanking() >= newRanking && favorite.getRanking() < oldRanking) {
                    favorite.setRanking(favorite.getRanking() + 1);
                    favoritesRepository.save(favorite);
                }
            }
        } else if (newRanking > oldRanking) {
            // Moved to a worse ranking: Decrement rankings of those in the new range
            for (Favorites favorite : favorites) {
                if (favorite.getRanking() <= newRanking && favorite.getRanking() > oldRanking) {
                    favorite.setRanking(favorite.getRanking() - 1);
                    favoritesRepository.save(favorite);
                }
            }
        }

        // Finally, update the ranking of the targeted favorite
        Favorites favoriteToUpdate = favorites.stream()
                .filter(f -> f.getParkName().equals(parkName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Favorite not found favorite update"));
        favoriteToUpdate.setRanking(newRanking);
        favoritesRepository.save(favoriteToUpdate);
    }

    @Transactional
    public void removeFavorite(int id) {
        try {
            favoritesRepository.removeFromFavorites(id);
        }
        catch (Error error) {
            throw new IllegalStateException("error deleting favorites from database");
        }
    }

    @Transactional
    public void deleteAllFavorites(String username) {
        favoritesRepository.deleteAllFavorites(username);
    }

    public String suggestFavoritePark(List<String> usernames) {
        if (usernames.isEmpty()) {
            return "No users provided";
        }

        // Fetch all pairs for the given usernames
        List<UserParkPair> pairs = favoritesRepository.findFavoritesByManyUsernames(usernames);
        Map<String, Integer> parkCounts = new HashMap<>();
        Map<String, Double> parkAverageRankings = new HashMap<>();

        // Initialize commonParks with parks from the first user
        Set<String> commonParks = pairs.stream()
                .filter(pair -> pair.getUsername().equals(usernames.get(0)))
                .map(UserParkPair::getApiId)
                .collect(Collectors.toSet());

        // Calculate counts and average rankings for all parks
        for (UserParkPair pair : pairs) {
            parkCounts.put(pair.getApiId(), parkCounts.getOrDefault(pair.getApiId(), 0) + 1);
            parkAverageRankings.merge(pair.getApiId(), (double) pair.getRanking(), (a, b) -> a + b);
        }
        parkAverageRankings.forEach((key, value) -> parkAverageRankings.put(key, value / parkCounts.get(key)));

        // Refine commonParks to include only those parks that are common to all users
        for (String username : usernames) {
            Set<String> userParks = pairs.stream()
                    .filter(pair -> pair.getUsername().equals(username))
                    .map(UserParkPair::getApiId)
                    .collect(Collectors.toSet());
            commonParks.retainAll(userParks);
        }

        // Find the park with the lowest average ranking among common parks
        String suggestedPark = commonParks.stream()
                .min(Comparator.comparingDouble(parkAverageRankings::get))
                .orElse(null);

        if (suggestedPark != null) {
            return suggestedPark;
        }

        // If no common park, find the most common park
        suggestedPark = parkCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // If still no park found, return the first park of the first user's favorites
        if (suggestedPark == null && !pairs.isEmpty()) {
            suggestedPark = pairs.stream()
                    .filter(pair -> pair.getUsername().equals(usernames.get(0)))
                    .findFirst()
                    .map(UserParkPair::getApiId)
                    .orElse("No common park found");
        }

        return suggestedPark;
    }

}




