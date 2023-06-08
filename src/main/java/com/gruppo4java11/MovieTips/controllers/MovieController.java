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
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    String api_key;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieService movieService;

    public MovieController(MovieRepository movieRepository, MovieService movieService){
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        api_key = System.getenv("TMBD_API_KEY");
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable long id){
        if(movieRepository.findById(id).equals(null)){
            throw new MovieNotFoundException("Movie id not found" + id);
        }
       return movieRepository.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addMovie(@RequestBody Movie movie){
        movieRepository.saveAndFlush(movie);
        return ResponseEntity.ok("Movie Added!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@RequestBody Movie movie, @PathVariable long id){
        Movie movieFromDB = movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie does not exist!"));
        movieFromDB.setCostPerDay(movie.getCostPerDay());
        movieFromDB.setTmbdId(movie.getTmbdId());
        movieFromDB.setStockQuantity(movie.getStockQuantity());
        movieRepository.saveAndFlush(movieFromDB);
        return ResponseEntity.ok("Movie updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable long id){
        movieRepository.deleteById(id);
        return ResponseEntity.ok("Movie Deleted!");
    }

    @GetMapping("/tmdb/{name}")
    public ResponseEntity<String> getMovieFromTMDB(@PathVariable String name){
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

    @GetMapping("/now-playing")
    public ResponseEntity<String> nowPlayingInTheaters(){
        Response response = movieService.nowPlayingInTheaters();
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

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setMovieStatus(@PathVariable long id){
        Movie movieToChange = movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie not found!"));
        if(movieToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) movieToChange.setRecordStatus(RecordStatus.DELETED);
        else movieToChange.setRecordStatus(RecordStatus.ACTIVE);
        movieRepository.updateStatusById(movieToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Movie with id " + id + " Status Updated to " + movieToChange.getRecordStatus());
    }

    @ExceptionHandler
    public ResponseEntity<MovieErrorResponse> movieHandlerException(MovieNotFoundException mne){

        MovieErrorResponse errorResponse = new MovieErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessageError(mne.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
}
