package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

/**
 * Movie class representing all the movies within our application's database
 */
@Where(clause = "record_status = 'ACTIVE'")
@Entity
@Table (name = "movie")
public class Movie extends AuditableEntity {
    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, unique = true, name = "tmdb_id")
    private Integer tmdbId;
    @Column(nullable = false, name = "cost_per_day")
    private Double costPerDay;
    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    /**
     *Constructor for the Movie class
     * @param id id that references movies in the database
     * @param name name of the movie in the databse or in "stock"
     * @param tmbdId ID reference of our external API (database that has all the SPECIFIC info of our movies)
     * @param costPerDay daily cost for renting the movie
     * @param stockQuantity how many of each movie we have in "stock" in our "storefront"
     */
    public Movie(Long id, String name, int tmdbId, double costPerDay, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.tmdbId = tmdbId;
        this.costPerDay = costPerDay;
        this.stockQuantity = stockQuantity;
    }

    public Movie() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTmdbId() {
        return this.tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public double getCostPerDay() {
        return this.costPerDay;
    }

    public void setCostPerDay(double costPerDay) {
        this.costPerDay = costPerDay;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
