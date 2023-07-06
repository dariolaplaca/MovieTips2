package com.gruppo4java11.MovieTips.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.exception.MovieErrorResponse;
import com.gruppo4java11.MovieTips.exception.MovieNotFoundException;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import com.gruppo4java11.MovieTips.services.MovieService;
import com.gruppo4java11.MovieTips.tmdbEntities.MovieTMDB;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller of the movie entities
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/movie")
@Tag(name = "Movies API")
public class MovieController {
    String api_key;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieService movieService;
    /**
     * @param movieRepository the repository used for accessing and managing movies
     * @param movieService the service using for performing operation of movie
     */
    public MovieController(MovieRepository movieRepository, MovieService movieService){
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        api_key = System.getenv("TMBD_API_KEY");
    }

    /**
     * This mapping retrieves the movies with the specified ID
     * @param id ID of the movie to retrieve
     * @return the movie with the specified ID, or null if not found
     */
    @Operation(summary = "Get a movie by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @GetMapping("/{id}")
    public Movie getMovie(@Parameter(description = "Id of the movie") @PathVariable long id){
        if(movieRepository.findById(id).isEmpty()){
            return null;
        }
       return movieRepository.findById(id).get();
    }

    /**
     * This mapping retrieves all the movies from the database
     * @return a list of the all movies in the database
     */
    @CrossOrigin(value = "http//localhost:4200")
    @Operation(summary = "Get all Movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Movies successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) })
    })
    @GetMapping("/all")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    /**
     * Create a new movie in the database
     * @param movie movie to be created
     * @return ResponseEntity indicating the success of the movie creation
     */
    @Operation(summary = "Add a new Movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "400", description = "Poorly formatted Request Body",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) })

    })
    @Tag(name = "A - DEMO")
    @PostMapping("/create")
    public ResponseEntity<String> addMovie(@Parameter(description = "Movie body with all the parameters")@RequestBody Movie movie, @Parameter(description = "Name of the user that is creating a new Movie")@RequestParam String username){
        movie.setCreatedBy(username);
        movie.setCreatedOn(LocalDateTime.now());
        movie.setModifiedOn(LocalDateTime.now());
        movie.setModifiedBy(username);
        movieRepository.saveAndFlush(movie);
        Long highestId = movieRepository.getHighestID();
        return ResponseEntity.ok("Movie added successfully with id " + highestId);
    }
    /**
     * Updates the movie with the specified ID in the database
     * @param movie the updated movie details
     * @param id ID of the movie to update
     * @return  ResponseEntity indicating the success of the movie update
     */
    @Operation(summary = "Update a movie by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Poorly formatted Request Body",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@Parameter(description = "Movie body with all the parameters")@RequestBody Movie movie, @Parameter(description = "Id of the movie to update")@PathVariable long id, @Parameter(description = "Name of the user that is updating the movie")@RequestParam String username){
        Movie movieFromDB = movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie does not exist!"));
        movieFromDB.setCostPerDay(movie.getCostPerDay());
        movieFromDB.setTmbdId(movie.getTmbdId());
        movieFromDB.setStockQuantity(movie.getStockQuantity());
        movieFromDB.setModifiedBy(username);
        movieFromDB.setModifiedOn(LocalDateTime.now());
        movieRepository.saveAndFlush(movieFromDB);
        return ResponseEntity.ok("Movie updated!");
    }
    /**
     * Deletes the movie with the specified ID from the database
     * @param id ID of the movie to delete
     * @return  ResponseEntity indicating the success of the movie deletion
     */
    @Operation(summary = "Delete a movie by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID",
                    content = @Content )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@Parameter(description = "Id of the movie to delete")@PathVariable long id){
        movieRepository.deleteById(id);
        return ResponseEntity.ok("Movie Deleted!");
    }
    /**
     * Retrieves movie information from TMDB (The Movie Database) based on the provided movie name
     * @param name the name of the movie to search for on TMDB
     * @return ResponseEntity containing the movie information retrieved from TMDB
     */
    @Operation(summary = "Get movies info from the external database through TMDb API by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TMDb Movie successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid Name", content = @Content)
    })
    @GetMapping("/tmdb/title/{name}")
    public ResponseEntity<MovieTMDB> getMovieFromTMDBByName(@Parameter(description = "Name of the movie to retrieve")@PathVariable String name) throws IOException {
        Response response = movieService.getMovieFromTMDBByName(name);
        if (response == null || response.body() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        MovieTMDB movie = objectMapper.readValue(response.body().string(), MovieTMDB.class);

        return ResponseEntity.ok(movie);
    }

    /**
     * Retrieves movie information from TMDB (The Movie Database) based on the provided movie name
     * @param id the name of the movie to search for on TMDB
     * @return ResponseEntity containing the movie information retrieved from TMDB
     */
    @Tag(name = "A - DEMO")
    @CrossOrigin(value = "http//localhost:4200")
    @Operation(summary = "Get movies info from the external database through TMDb API by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TMDb Movie successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID", content = @Content)
    })
    @GetMapping("/tmdb/id/{id}")
    public ResponseEntity<MovieTMDB> getMovieFromTMDBById(@Parameter(description = "Id of the External Database Movie")@PathVariable Integer id) throws IOException {
        Response response = movieService.getMovieFromTMDBById(id);
        if (response == null || response.body() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        MovieTMDB movie = objectMapper.readValue(response.body().string(), MovieTMDB.class);

        return ResponseEntity.ok(movie);
    }


    /**
     * Retrieves the list of movies currently playing in theaters
     * @return  ResponseEntity containing the list of movies currently playing in theaters
     * @throws IOException
     */
    @CrossOrigin(value = "http//localhost:4200")
    @Tag(name = "A - DEMO")
    @Operation(summary = "Get all the info of the movies that are currently in theaters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie in theaters list successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie List not found", content = @Content)
    })
    @GetMapping("/now-playing")
    public ResponseEntity<Set<MovieTMDB>> nowPlayingInTheaters() throws IOException {
        Response response = movieService.nowPlayingInTheaters();
        if(response.body() == null) {
            return ResponseEntity.internalServerError().build();
        }
        Set<MovieTMDB> movieList = movieService.deserializeMovieJsonList(response);
        return ResponseEntity.ok(movieList);
    }

    /**
     * Sets the status of a movie identified by the given ID
     * @param id ID of the movie to update
     * @return ResponseEntity containing a message indicating the updated status of the movie
     */
    @Operation(summary = "Set the logical status of an Movie's record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie status successfully changed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setMovieStatus(@Parameter(description = "Id of the movie")@PathVariable long id, @Parameter(description = "Name of the user that is changing the status")@RequestParam String username){
        Movie movieToChange = movieRepository.findById(id).orElse(null);
        if(movieToChange == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id " + id + " not found!");
        }
        assert movieToChange != null;
        if(movieToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) movieToChange.setRecordStatus(RecordStatus.DELETED);
        else movieToChange.setRecordStatus(RecordStatus.ACTIVE);

        movieToChange.setModifiedBy(username);
        movieToChange.setModifiedOn(LocalDateTime.now());
        movieRepository.updateStatusById(movieToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Movie with id " + id + " Status Updated to " + movieToChange.getRecordStatus());
    }

    @Operation(summary = "Get recommended movies based on another movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recommended movies successfully returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @GetMapping("recommended/{id}")
    public ResponseEntity<Set<MovieTMDB>> getRecommendedMovies(@Parameter(description = "Id of the tmdb movie")@PathVariable Integer id) throws IOException {
        if(!movieService.checkIfTmdbIdIsInJson(id)){
            return ResponseEntity.badRequest().build();
        }
        Response response = movieService.getRecommendedMovies(id);
        Set<MovieTMDB> recommendedMovies = movieService.deserializeMovieJsonList(response);
        return ResponseEntity.ok(recommendedMovies);
    }

    /**
     * Exception handler for handling MovieNotFoundException
     * @param mne  The MovieNotFoundException to be handled
     * @return  ResponseEntity containing a MovieErrorResponse indicating the error details
     */
    @ExceptionHandler
    public ResponseEntity<MovieErrorResponse> movieHandlerException(MovieNotFoundException mne){

        MovieErrorResponse errorResponse = new MovieErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessageError(mne.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
}
