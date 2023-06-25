package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.Favorite;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.repositories.FavoriteRepository;
import com.gruppo4java11.MovieTips.services.FavoriteService;
import com.gruppo4java11.MovieTips.services.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller of the favorites entities
 */
@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites API")
public class FavoriteController {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MovieService movieService;
    /**
     * @param favoriteRepository the repository used for accessing and managing favorites
     * @param favoritesService the service used for performing operations on favorites
     */
    public FavoriteController(FavoriteRepository favoriteRepository, FavoriteService favoritesService, MovieService movieService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoritesService;
        this.movieService = movieService;
    }
    /**
     * This mapping retrieves the favorites with the specified ID
     * @param id ID of the favorites to retrieve
     * @return the favorites with the specified ID
     */
    @GetMapping("/{id}")
    public Favorite getFavorites(@PathVariable long id){
        return favoriteRepository.findById(id).orElse(null);
    }
    /**
     * This mapping retrieves all the favorites from the database
     * @return  a list of all the favorites in the database
     */
    @GetMapping("/all")
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    /**
     * Creates a new favorite for the specified account
     * @param tmdb_id The TMDB ID of the item to add as a favorite
     * @param account_id The ID of the account to which the favorite will be added
     * @return ResponseEntity indicating the success status of the operation
     */
    @PostMapping("/{account_id}/id/{tmdb_id}")
    public ResponseEntity<String> createFavorites(@PathVariable int tmdb_id, @PathVariable long account_id, @RequestParam String username) {
        Account account = accountRepository.findById(account_id).orElse(null);
        if(account == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with id " + account_id + " was not found!");
        } else if (!movieService.checkIfTmdbIdIsInJson(tmdb_id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The movie with id " + tmdb_id + " was not found!");
        }
        Favorite favorite = new Favorite(account, tmdb_id);
        favorite.setCreatedBy(username);
        favorite.setCreatedOn(LocalDateTime.now());
        favorite.setModifiedBy(username);
        favorite.setModifiedOn(LocalDateTime.now());
        favoriteRepository.saveAndFlush(favorite);
        Long highestId = favoriteRepository.getHighestID();
        return ResponseEntity.ok("Favorite created successfully with id " + highestId);
    }


    /**
     * Creates a new favorite for the specified account
     * @param movieName The movie name of the record to add as a favorite
     * @param account_id The ID of the account to which the favorite will be added
     * @return ResponseEntity indicating the success status of the operation
     */
    @PostMapping("/{account_id}/title/{movieName}")
    public ResponseEntity<String> createFavorites(@PathVariable String movieName, @PathVariable long account_id, @RequestParam String username) {
        Account account = accountRepository.findById(account_id).orElse(new Account());
        Integer TMDB_id = movieService.getTMDBIdByName(movieName);
        if(account.getName() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with id " + account_id + " was not found!");
        } else if (TMDB_id == -1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The movie with name " + movieName + " was not found!");
        }
        Favorite favorite = new Favorite(account, TMDB_id);
        favorite.setCreatedBy(username);
        favorite.setCreatedOn(LocalDateTime.now());
        favorite.setModifiedBy(username);
        favorite.setModifiedOn(LocalDateTime.now());
        favoriteRepository.saveAndFlush(favorite);
        Long highestId = favoriteRepository.getHighestID();
        return ResponseEntity.ok("Favorite created successfully with id " + highestId);
    }


    /**
     * This mapping updates the favorite with the specified ID in the database
     * @param favorite updated favorite details
     * @param id ID of the favorite to update
     * @return  ResponseEntity indicating the success of the favorite update
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateFavorites(@RequestBody Favorite favorite, @PathVariable long id, @RequestParam String username){
        Favorite favoriteFromDB = favoriteRepository.findById(id).orElseThrow(()-> new RuntimeException("Favorite not found!"));
        favoriteFromDB.setAccount(favorite.getAccount());
        favoriteFromDB.setTMDB_ID(favorite.getTMDB_ID());
        favoriteFromDB.setModifiedBy(username);
        favoriteFromDB.setModifiedOn(LocalDateTime.now());
        favoriteRepository.saveAndFlush(favoriteFromDB);
        return ResponseEntity.ok("Favorite updated!");
    }
    /**
     * Deletes the favorite with the specified ID from the database
     * @param id ID of the favorite to delete
     * @return ResponseEntity indicating the success of the favorite deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable long id){
        favoriteRepository.deleteById(id);
        return ResponseEntity.ok("Favorite removed!");
    }

    /**
     * Sets the status of a favorite identified by the given ID
     * @param id ID of the favorite to update
     * @return ResponseEntity containing a message indicating the updated status of the favorite
     */
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setFavoriteStatus(@PathVariable long id, @RequestParam String name){
        Favorite favoriteToChange = favoriteRepository.findById(id).orElseThrow(()-> new RuntimeException("Favorite not found!"));
        favoriteToChange.setModifiedOn(LocalDateTime.now());
        favoriteToChange.setModifiedBy(name);
        if(favoriteToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) favoriteToChange.setRecordStatus(RecordStatus.DELETED);
        else favoriteToChange.setRecordStatus(RecordStatus.ACTIVE);
        favoriteRepository.updateStatusById(favoriteToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Favorite with id " + id + " Status Updated to " + favoriteToChange.getRecordStatus());
    }
}
