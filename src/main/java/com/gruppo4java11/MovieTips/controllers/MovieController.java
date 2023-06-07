package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import com.gruppo4java11.MovieTips.services.MovieService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(response.body());
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
}