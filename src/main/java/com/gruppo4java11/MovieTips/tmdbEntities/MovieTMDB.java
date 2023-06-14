package com.gruppo4java11.MovieTips.tmdbEntities;

import java.time.LocalDate;
import java.util.List;

/**
 * Class to save the info from TMDB in a custom movie Object
 */
public class MovieTMDB {
    private String title;
    private Integer id;
    private List<Genre> genres;
    private String description;
    private LocalDate releaseDate;
    private String tagLine;
    private String posterPath;

    /**
     * Constructor of the TMDB Movie
     * @param title movie title
     * @param id tmdb id
     * @param genres genre of the movie
     * @param description description of the movie
     * @param releaseDate release date of the movie
     * @param tagLine line in the poster of the movie
     * @param posterPath path of the poster image
     * Link to get the poster image: https://image.tmdb.org/t/p/original/{posterPathUrl}
     */
    public MovieTMDB(String title, int id, List<Genre> genres, String description, LocalDate releaseDate, String tagLine, String posterPath) {
        this.title = title;
        this.id = id;
        this.genres = genres;
        this.description = description;
        this.releaseDate = releaseDate;
        this.tagLine = tagLine;
        this.posterPath = posterPath;
    }

    public MovieTMDB() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
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

    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}

