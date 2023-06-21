package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.Favorites;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.repositories.FavoritesRepository;
import com.gruppo4java11.MovieTips.services.FavoritesService;
import com.gruppo4java11.MovieTips.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller of the favorites entities
 */
@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private MovieService movieService;
    /**
     * @param favoritesRepository the repository used for accessing and managing favorites
     * @param favoritesService the service used for performing operations on favorites
     */
    public FavoritesController(FavoritesRepository favoritesRepository, FavoritesService favoritesService, MovieService movieService) {
        this.favoritesRepository = favoritesRepository;
        this.favoritesService = favoritesService;
        this.movieService = movieService;
    }
    /**
     * This mapping retrieves the favorites with the specified ID
     * @param id ID of the favorites to retrieve
     * @return the favorites with the specified ID
     */
    @GetMapping("/{id}")
    public Favorites getFavorites(@PathVariable long id){
        return favoritesRepository.findById(id).orElse(null);
    }
    /**
     * This mapping retrieves all the favorites from the database
     * @return  a list of all the favorites in the database
     */
    @GetMapping("/all")
    public List<Favorites> getAllFavorites() {
        return favoritesRepository.findAll();
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
        Favorites favorites = new Favorites(account, tmdb_id);
        favorites.setCreatedBy(username);
        favorites.setCreatedOn(LocalDate.now());
        favorites.setModifiedBy(username);
        favorites.setModifiedOn(LocalDate.now());
        favoritesRepository.saveAndFlush(favorites);
        return ResponseEntity.ok("Favorite created successfully!");
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
        Favorites favorites = new Favorites(account, TMDB_id);
        favorites.setCreatedBy(username);
        favorites.setCreatedOn(LocalDate.now());
        favorites.setModifiedBy(username);
        favorites.setModifiedOn(LocalDate.now());
        favoritesRepository.saveAndFlush(favorites);
        return ResponseEntity.ok("Favorite created successfully!");
    }


    /**
     * This mapping updates the favorite with the specified ID in the database
     * @param favorites updated favorite details
     * @param id ID of the favorite to update
     * @return  ResponseEntity indicating the success of the favorite update
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateFavorites(@RequestBody Favorites favorites, @PathVariable long id, @RequestParam String username){
        Favorites favoritesFromDB = favoritesRepository.findById(id).orElseThrow(()-> new RuntimeException("Favorite not found!"));
        favoritesFromDB.setAccount(favorites.getAccount());
        favoritesFromDB.setTMDB_ID(favorites.getTMDB_ID());
        favoritesFromDB.setModifiedBy(username);
        favoritesFromDB.setModifiedOn(LocalDate.now());
        favoritesRepository.saveAndFlush(favoritesFromDB);
        return ResponseEntity.ok("Favorite updated!");
    }
    /**
     * Deletes the favorite with the specified ID from the database
     * @param id ID of the favorite to delete
     * @return ResponseEntity indicating the success of the favorite deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable long id){
        favoritesRepository.deleteById(id);
        return ResponseEntity.ok("Favorite removed!");
    }

    /**
     * Sets the status of a favorite identified by the given ID
     * @param id ID of the favorite to update
     * @return ResponseEntity containing a message indicating the updated status of the favorite
     */
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setFavoriteStatus(@PathVariable long id, @RequestParam String name){
        Favorites favoritesToChange = favoritesRepository.findById(id).orElseThrow(()-> new RuntimeException("Favorite not found!"));
        favoritesToChange.setModifiedOn(LocalDate.now());
        favoritesToChange.setModifiedBy(name);
        if(favoritesToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) favoritesToChange.setRecordStatus(RecordStatus.DELETED);
        else favoritesToChange.setRecordStatus(RecordStatus.ACTIVE);
        favoritesRepository.updateStatusById(favoritesToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Favorite with id " + id + " Status Updated to " + favoritesToChange.getRecordStatus());
    }
}
