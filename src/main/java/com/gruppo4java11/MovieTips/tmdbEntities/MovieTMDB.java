package com.gruppo4java11.MovieTips.tmdbEntities;

import java.time.LocalDate;
import java.util.List;

/**
 * Class to save the info from TMDB in a custom movie Object
 */
public class MovieTMDB {
    private String title;
    private Integer id;
    //TODO aggiungere modificatore di accesso
    List<Genre> genres;
    private String description;
    private LocalDate releaseDate;
    private String tagLine;
    private String posterPath;


    public MovieTMDB(String title, int id, List<Genre> genres, String description, LocalDate releaseDate, String tagLine, String posterPath) {
        this.title = title;
        this.id = id;
        this.genres = genres;
        this.description = description;
        this.releaseDate = releaseDate;
        this.tagLine = tagLine;
        this.posterPath = posterPath;  //https://image.tmdb.org/t/p/original/{posterPathUrl}
    }

    public MovieTMDB() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTmdb_id() {
        return this.id;
    }

    public void setTmdb_id(int id) {
        this.id = id;
    }

    public List<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTagLine() {
        return this.tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
}

