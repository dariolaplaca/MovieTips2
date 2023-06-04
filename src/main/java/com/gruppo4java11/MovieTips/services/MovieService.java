package com.gruppo4java11.MovieTips.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public Integer getTMDBIdByName(String name){
        Map<String, Integer> movieMap = getMovieList();
        Integer movieId;
        movieId = movieMap.get(name);
        return movieId;
    }

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
}
