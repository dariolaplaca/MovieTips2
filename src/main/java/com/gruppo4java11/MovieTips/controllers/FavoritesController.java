package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Favorites;
import com.gruppo4java11.MovieTips.repositories.FavoritesRepository;
import com.gruppo4java11.MovieTips.services.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    @Autowired
    private FavoritesRepository favoritesRepository;
    private FavoritesService favoritesService;

    public FavoritesController(FavoritesRepository favoritesRepository, FavoritesService favoritesService) {
        this.favoritesRepository = favoritesRepository;
        this.favoritesService = favoritesService;
    }

    @GetMapping("/{id}")
    public Favorites getFavorites(@PathVariable long id){
        return favoritesRepository.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<Favorites> getAllFavorites() {
        return favoritesRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createFavorites(@RequestBody Favorites favorites) {
        favoritesRepository.saveAndFlush(favorites);
        return ResponseEntity.ok("Favorite created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFavorites(@RequestBody Favorites favorites, @PathVariable long id){
        Favorites favoritesFromDB = favoritesRepository.findById(id).orElseThrow(()-> new RuntimeException("Favorite not found!"));
        favoritesFromDB.setAccount(favorites.getAccount());
        favoritesFromDB.setTMDB_ID(favorites.getTMDB_ID());
        favoritesRepository.saveAndFlush(favoritesFromDB);
        return ResponseEntity.ok("Favorite updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable long id){
        favoritesRepository.deleteById(id);
        return ResponseEntity.ok("Favorite removed!");
    }




}
