package com.gruppo4java11.MovieTips.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class that include all the Services and function of the Movie entity
 */
@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    //TODO Rimuovere eccezioni
    //TODO Non utilizzare mappe ma creare oggetti per

    /**
     * This function retrieve the TMDB id from the map based on the name of the movie
     * @param name name of the movie
     * @return the id related to the name
     */
    public Integer getTMDBIdByName(String name){
        Map<String, Integer> movieMap = getMovieList();
        Optional<Integer> movieId;
        movieId = movieMap.get(name).describeConstable();
        if(movieId.isPresent()) return movieId.get();
        else throw new IllegalArgumentException("Id Not Found!");
    }

    /**
     * This function read the json containing all the names and ids of the movies present in the external api and save them in a map
     * @return a map containing all the name of the movie as key and their corresponding id as values
     */
    public Map<String, Integer> getMovieList(){
        Map<String, Integer> movieMap = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            movieMap = mapper.readValue(Paths.get("tmdb_ids.json").toFile(), Map.class);
            return movieMap;
        } catch (IOException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        }
        return movieMap;
    }

    /**
     * This function make an external API call to the TMDB database to retrieve all the movies currently in theaters
     * @return a Response containing a json with all the movies currently in theaters
     */
    public Response nowPlayingInTheaters() {
        String api_key = System.getenv("TMDB_API_KEY");
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1&region=US")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_key)
                .build();
        try {
           response = client.newCall(request).execute();
        } catch (IOException e) {
            System.err.println("Caught IOException");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * This function make an external API call to the TMDB database to retrieve all the info of a movie
     * @param movieName the name of the movie to search
     * @return a Response containing a json containing all the movie's info
     */
    public Response getMovieFromTMDBByName(String movieName){
        Integer tmbdId = getTMDBIdByName(movieName);
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        String url = "https://api.themoviedb.org/3/movie/" + tmbdId + "?language=en-US";
        String api_key = System.getenv("TMDB_API_KEY");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_key)
                .build();
        
        try {
            response = client.newCall(request).execute();
        } catch (IOException ioException){
            ioException.printStackTrace();
            System.err.println("IOException");
        }
        return response;
    }
}
