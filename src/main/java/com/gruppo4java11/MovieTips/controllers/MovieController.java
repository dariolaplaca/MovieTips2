package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Favorites;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.exception.MovieErrorResponse;
import com.gruppo4java11.MovieTips.exception.MovieNotFoundException;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import com.gruppo4java11.MovieTips.services.MovieService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
/**
 * Controller of the movie entities
 */
@RestController
@RequestMapping("/movie")
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
    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable long id){
        if(movieRepository.findById(id).isEmpty()){
            return null;
        }
       return movieRepository.findById(id).get();
    }

    /**
     * This mapping retrieves all the movies from the database
     * @return a list of the all movies in the database
     */
    @GetMapping("/all")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    /**
     * Create a new movie in the database
     * @param movie movie to be created
     * @return ResponseEntity indicating the success of the movie creation
     */
    @PostMapping
    public ResponseEntity<String> addMovie(@RequestBody Movie movie, @RequestParam String username){
        movie.setCreatedBy(username);
        movie.setCreatedOn(LocalDate.now());
        movie.setModifiedOn(LocalDate.now());
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
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@RequestBody Movie movie, @PathVariable long id, @RequestParam String username){
        Movie movieFromDB = movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie does not exist!"));
        movieFromDB.setCostPerDay(movie.getCostPerDay());
        movieFromDB.setTmbdId(movie.getTmbdId());
        movieFromDB.setStockQuantity(movie.getStockQuantity());
        movieFromDB.setModifiedBy(username);
        movieFromDB.setModifiedOn(LocalDate.now());
        movieRepository.saveAndFlush(movieFromDB);
        return ResponseEntity.ok("Movie updated!");
    }
    /**
     * Deletes the movie with the specified ID from the database
     * @param id ID of the movie to delete
     * @return  ResponseEntity indicating the success of the movie deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable long id){
        movieRepository.deleteById(id);
        return ResponseEntity.ok("Movie Deleted!");
    }
    /**
     * Retrieves movie information from TMDB (The Movie Database) based on the provided movie name
     * @param name the name of the movie to search for on TMDB
     * @return ResponseEntity containing the movie information retrieved from TMDB
     */
    @GetMapping("/tmdb/title/{name}")
    public ResponseEntity<String> getMovieFromTMDBByName(@PathVariable String name){
        Response response = movieService.getMovieFromTMDBByName(name);
        String body = "Something went wrong";
        try{
            assert response.body() != null;
            body = response.body().string();
        }catch (IOException ioException){
            System.err.println("IOException");
            ioException.printStackTrace();
        } catch (NullPointerException nullPointerException){
            System.err.println("NullPointerException");
            nullPointerException.printStackTrace();
        }
        return ResponseEntity.ok(body);
    }

    /**
     * Retrieves movie information from TMDB (The Movie Database) based on the provided movie name
     * @param id the name of the movie to search for on TMDB
     * @return ResponseEntity containing the movie information retrieved from TMDB
     */
    @GetMapping("/tmdb/id/{id}")
    public ResponseEntity<String> getMovieFromTMDBById(@PathVariable Integer id) throws IOException {
        Response response = movieService.getMovieFromTMDBById(id);
        if (response == null || response.body() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found!");
        }
        return ResponseEntity.ok(response.body().string());
    }


    /**
     * Retrieves the list of movies currently playing in theaters
     * @return  ResponseEntity containing the list of movies currently playing in theaters
     * @throws IOException
     */
    @GetMapping("/now-playing")
    public ResponseEntity<String> nowPlayingInTheaters() throws IOException {
        Response response = movieService.nowPlayingInTheaters();
        assert response.body() != null;
        return ResponseEntity.ok(response.body().string());
    }

    /**
     * Sets the status of a movie identified by the given ID
     * @param id ID of the movie to update
     * @return ResponseEntity containing a message indicating the updated status of the movie
     */
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setMovieStatus(@PathVariable long id, @RequestParam String username){
        Movie movieToChange = movieRepository.findById(id).orElse(null);
        if(movieToChange == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id " + id + " not found!");
        }
        assert movieToChange != null;
        if(movieToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) movieToChange.setRecordStatus(RecordStatus.DELETED);
        else movieToChange.setRecordStatus(RecordStatus.ACTIVE);

        movieToChange.setModifiedBy(username);
        movieToChange.setModifiedOn(LocalDate.now());
        movieRepository.updateStatusById(movieToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Movie with id " + id + " Status Updated to " + movieToChange.getRecordStatus());
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
