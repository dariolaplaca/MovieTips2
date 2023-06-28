package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.Favorite;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.repositories.FavoriteRepository;
import com.gruppo4java11.MovieTips.services.FavoriteService;
import com.gruppo4java11.MovieTips.services.MovieService;
import com.gruppo4java11.MovieTips.tmdbEntities.MovieTMDB;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    @Operation(summary = "Get a favorite by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite Successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), }),
            @ApiResponse(responseCode = "404", description = "Favorite Not Found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Favorite ID is Invalid", content = @Content)
    })
    @GetMapping("/{id}")
    public Favorite getFavorites(@Parameter(description = "ID of the Favorite to Retrieve") @PathVariable long id){
        return favoriteRepository.findById(id).orElse(null);
    }
    /**
     * This mapping retrieves all the favorites from the database
     * @return  a list of all the favorites in the database
     */
    @Operation(summary = "Get all favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorites Successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), })
    })
    @GetMapping("/all")
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    /**
     * Creates a new favorite for the specified account by ID
     * @param tmdb_id The TMDB ID of the item to add as a favorite
     * @param account_id The ID of the account to which the favorite will be added
     * @return ResponseEntity indicating the success status of the operation
     */
    @Operation(summary = "Create a new Favorite for the Specified Account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite Successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), }),
            @ApiResponse(responseCode = "400", description = "Favorite ID is Invalid", content = @Content)
    })
    @PostMapping("/{account_id}/id/{tmdb_id}")
    public ResponseEntity<String> createFavorites(@Parameter(description = "ID of the tmdb movie") @PathVariable int tmdb_id, @Parameter(description = "ID of the account") @PathVariable long account_id, @Parameter(description = "Name of user that is creating the favorite") @RequestParam String username) {
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
     * Creates a new favorite for the specified account by name
     * @param movieName The movie name of the record to add as a favorite
     * @param account_id The ID of the account to which the favorite will be added
     * @return ResponseEntity indicating the success status of the operation
     */

    @Operation(summary = "Create a new Favorite for the Specified Account by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite Successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), }),
            @ApiResponse(responseCode = "400", description = "Favorite name is Invalid", content = @Content)
    })
    @PostMapping("/{account_id}/title/{movieName}")
    public ResponseEntity<String> createFavorites(@Parameter(description = "Name of the TMDB Movie") @PathVariable String movieName, @Parameter(description = "ID of the account") @PathVariable long account_id, @Parameter(description = "Name of the user that is creating the favorite") @RequestParam String username) {
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
    @Operation(summary = "Edit a Favorite by ID from the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite Successfully edited",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), }),
            @ApiResponse(responseCode = "404", description = "Favorite Not Found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Favorite ID is Invalid", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateFavorites(@Parameter(description = "Body of the favorite to edit") @RequestBody Favorite favorite, @Parameter(description = "ID of the favorite to edit") @PathVariable long id, @Parameter(description = "Name of the account editing the favorite") @RequestParam String username){
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
    @Operation(summary = "Delete a Favorite by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite Successfully removed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), }),
            @ApiResponse(responseCode = "404", description = "Favorite Not Found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Favorite ID is Invalid", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavorite(@Parameter(description = "ID of the favorite to be deleted") @PathVariable long id){
        favoriteRepository.deleteById(id);
        return ResponseEntity.ok("Favorite removed!");
    }

    /**
     * Sets the status of a favorite identified by the given ID
     * @param id ID of the favorite to update
     * @return ResponseEntity containing a message indicating the updated status of the favorite
     */
    @Operation(summary = "Sets the status a Favorite by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite Status Successfully set",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Favorite.class)), }),
            @ApiResponse(responseCode = "404", description = "Favorite Not Found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Favorite ID is Invalid", content = @Content)
    })
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setFavoriteStatus(@Parameter(description = "ID of the favorite") @PathVariable long id, @Parameter(description = "Name of the user setting the status") @RequestParam String name){
        Favorite favoriteToChange = favoriteRepository.findById(id).orElseThrow(()-> new RuntimeException("Favorite not found!"));
        favoriteToChange.setModifiedOn(LocalDateTime.now());
        favoriteToChange.setModifiedBy(name);
        if(favoriteToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) favoriteToChange.setRecordStatus(RecordStatus.DELETED);
        else favoriteToChange.setRecordStatus(RecordStatus.ACTIVE);
        favoriteRepository.updateStatusById(favoriteToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Favorite with id " + id + " Status Updated to " + favoriteToChange.getRecordStatus());
    }

    /**
     *
     * @param id account's id
     * @return Suggested set of Movies based on the account's favorite
     * @throws IOException
     */
    @Operation(summary = "Get recommended movies based on account's favorite list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recommended movies successfully returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @GetMapping("/recommended-movies/{id}")
    public ResponseEntity<Set<MovieTMDB>> getRecommendedMoviesForTheAccount(@Parameter(description = "Id of the account") @PathVariable Long id) throws IOException {
        Set<MovieTMDB> recommendedMovies = favoriteService.getRecommendedMoviesByFavorites(id);
        if (recommendedMovies == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recommendedMovies);
    }
}
