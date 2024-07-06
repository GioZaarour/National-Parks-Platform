package edu.usc.csci310.project.repository;

import edu.usc.csci310.project.DTO.UserParkPair;
import edu.usc.csci310.project.model.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    @Query("SELECT f.parkName FROM Favorites f WHERE f.user.username = :username ORDER BY f.ranking ASC")
    List<String> findFavoritesByUsername(String username);

    @Query("SELECT new edu.usc.csci310.project.DTO.UserParkPair(f.user.username, f.parkName, f.ranking, f.apiId) FROM " +
            "Favorites f WHERE f" +
            ".user.username IN :usernames")
    List<UserParkPair> findFavoritesByManyUsernames(List<String> usernames);

    @Query("SELECT f FROM Favorites f WHERE f.user.username = :username ORDER BY f.ranking ASC")
    List<Favorites> findFavoritesByUsernameOrderedByRanking(String username);

    @Query("Select f from Favorites f where f.user.username = :username order by f.ranking asc")
    List<Favorites> fetchUserFavoritesAndReturnFavoriteObject(String username);

    @Modifying
    @Query("DELETE FROM Favorites f WHERE f.id = :id")
    void removeFromFavorites(int id);

    @Modifying
    @Query("DELETE FROM Favorites f WHERE f.user.id = (SELECT u.id FROM User u WHERE u.username = :username)")
    void deleteAllFavorites(@Param("username") String username);
}
