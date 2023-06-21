package com.gruppo4java11.MovieTips.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruppo4java11.MovieTips.dto.MovieDTO;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

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

    /**
     * This function retrieve the TMDB id from the map based on the name of the movie
     * @param name name of the movie
     * @return the id related to the name
     */
    public Integer getTMDBIdByName(String name){
        Set<MovieDTO> movieSet = getMovieList();
        List<MovieDTO> movieToReturn = movieSet.stream().filter(movie -> movie.getTitle().equals(name)).toList();
        if(movieToReturn.size() == 0){
            return -1;
        }
        return movieToReturn.get(0).getId();
    }

    /**
     * This function checks if the id exists in the local json
     * @param id tmdb id of the movie
     * @return a boolean that checks if the id is present in the local tmdb_ids json
     */
    public Boolean checkIfTmdbIdIsInJson(Integer id){
        return getMovieList().stream().anyMatch(movie -> Objects.equals(movie.getId(), id));
    }

    /**
     * This function read the json containing all the names and ids of the movies present in the external api and save them in a map
     * @return a set containing all movies titles and ids stored as movie dto's
     */
    public Set<MovieDTO> getMovieList(){
        Set<MovieDTO> movieSet = new HashSet<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Integer> movieMap= mapper.readValue(Paths.get("tmdb_ids.json").toFile(), Map.class);
            for (Map.Entry<String, Integer> entry : movieMap.entrySet()){
                MovieDTO movieDTO = new MovieDTO();
                movieDTO.setTitle(entry.getKey());
                movieDTO.setId(entry.getValue());
                movieSet.add(movieDTO);
            }
        } catch (IOException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        }
        return movieSet;
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
        return getResponseFromTMDB(tmbdId);
    }

    /**
     * This function make an external API call to the TMDB database to retrieve all the info of a movie
     * @param id id of the tmdb movie
     * @return a Response containing a json containing all the movie's info
     */
    public Response getMovieFromTMDBById(Integer id){
        if(!checkIfTmdbIdIsInJson(id)){
            return null;
        }
        return getResponseFromTMDB(id);
    }

    @Nullable
    private static Response getResponseFromTMDB(Integer tmbdId) {
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
