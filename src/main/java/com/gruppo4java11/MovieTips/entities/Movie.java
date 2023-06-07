package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.*;

@Entity
@Table (name = "movie")
public class Movie extends Record{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, unique = true, name = "tmdb_id")
    private int tmbdId;
    @Column(nullable = false, name = "cost_per_day")
    private double costPerDay;
    @Column(name = "stock_quantity")
    private int stockQuantity;

    public Movie(Long id, String name, int tmbdId, double costPerDay, int stockQuantity) {
        super(RecordStatus.ACTIVE);
        this.id = id;
        this.name = name;
        this.tmbdId = tmbdId;
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

    public int getTmbdId() {
        return this.tmbdId;
    }

    public void setTmbdId(int tmbdId) {
        this.tmbdId = tmbdId;
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
