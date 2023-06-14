package com.gruppo4java11.MovieTips.tmdbEntities;

/**
 * Genre class containing the name of the genre and the TMDB genre id of our Tmdb Movies
 */
public class Genre {
    private int id;
    private String name;

    /**
     *
     * @param id tmdb id of the genre
     * @param name name of the genre
     */
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
