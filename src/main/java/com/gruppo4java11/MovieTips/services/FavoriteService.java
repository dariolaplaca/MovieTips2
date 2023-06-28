package com.gruppo4java11.MovieTips.services;

import com.gruppo4java11.MovieTips.entities.Favorite;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.repositories.FavoriteRepository;
import com.gruppo4java11.MovieTips.tmdbEntities.MovieTMDB;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that include all the Services and function of the Favorites entity
 */
@Service
public class FavoriteService {
    public static final Integer NUMBER_OF_RECOMMENDED_MOVIES_TO_RETURN = 3;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private AccountRepository accountRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MovieService movieService, AccountRepository accountRepository) {
        this.movieService = movieService;
        this.accountRepository = accountRepository;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * This function takes an account id and retrieve all the favorites from that account
     * Then thanks to the getRecommendedMovies it takes each list of recommended movies based on the favorite and
     * returns the set after assuring that there aren't any already favorite movies or duplicates inside
     * With a maximum of NUMBER_OF_RECOMMENDED_MOVIES_TO_RETURN
     * @param account_id id of the account
     * @return A set of recommended movies based on the account's favorites
     * @throws IOException
     */

    public Set<MovieTMDB> getRecommendedMoviesByFavorites(Long account_id) throws IOException {
        List<Favorite> accountsFavorites = favoriteRepository.findFavoriteByAccountId(account_id);
        if(accountsFavorites.size() == 0){
            return null;
        }
        List<MovieTMDB> recommendedMoviesByFavorites = new ArrayList<>();
        for(Favorite favorite : accountsFavorites){
            Set<MovieTMDB> movies = movieService.deserializeMovieJsonList(movieService.getRecommendedMovies(favorite.getTMDB_ID()));
            recommendedMoviesByFavorites.addAll(movies);
        }
        Set<Integer> moviesIdAlreadyInFavorite = accountsFavorites.stream().map(Favorite::getTMDB_ID).collect(Collectors.toSet());
        recommendedMoviesByFavorites = recommendedMoviesByFavorites.stream().filter(TMDbMovie -> !(moviesIdAlreadyInFavorite.contains(TMDbMovie.getId()))).distinct().collect(Collectors.toList());

        Set<MovieTMDB> recommendedMovies = new HashSet<>();
        if(recommendedMoviesByFavorites.size() < NUMBER_OF_RECOMMENDED_MOVIES_TO_RETURN){
            recommendedMovies.addAll(recommendedMoviesByFavorites);
            return recommendedMovies;
        }
        Random random = new Random();
        for(int i = 0; i < NUMBER_OF_RECOMMENDED_MOVIES_TO_RETURN; i++){
            int index = random.nextInt(recommendedMoviesByFavorites.size());
            MovieTMDB movie = recommendedMoviesByFavorites.get(index);
            recommendedMoviesByFavorites.remove(index);
            recommendedMovies.add(movie);
        }
        return recommendedMovies;
    }
}
