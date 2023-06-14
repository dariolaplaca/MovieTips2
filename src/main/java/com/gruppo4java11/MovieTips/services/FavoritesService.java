package com.gruppo4java11.MovieTips.services;

import com.gruppo4java11.MovieTips.repositories.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class that include all the Services and function of the Favorites entity
 */
@Service
public class FavoritesService {
    @Autowired
    private FavoritesRepository favoritesRepository;

    public FavoritesService(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }
}
