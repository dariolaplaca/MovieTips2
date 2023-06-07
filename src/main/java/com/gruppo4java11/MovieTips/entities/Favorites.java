package com.gruppo4java11.MovieTips.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO Rimuovere Lombok ed utilizzare getter setter e costruttori
@Entity
@Table
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Account account;
    @Column(nullable = false)
    private Integer TMDB_ID;

    public Favorites(Long id, Account account, Integer TMDB_ID) {
        this.id = id;
        this.account = account;
        this.TMDB_ID = TMDB_ID;
    }

    public Favorites() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getTMDB_ID() {
        return TMDB_ID;
    }

    public void setTMDB_ID(Integer TMDB_ID) {
        this.TMDB_ID = TMDB_ID;
    }
}