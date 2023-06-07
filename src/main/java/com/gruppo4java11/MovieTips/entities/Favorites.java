package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.*;

//TODO Rimuovere Lombok ed utilizzare getter setter e costruttori
@Entity
@Table(name = "favorites")
public class Favorites extends Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Account account;
    @Column(nullable = false, name = "tmdb_id")
    private Integer TMDB_ID;

    public Favorites(Long id, Account account, Integer TMDB_ID, RecordStatus recordStatus) {
        super(recordStatus);
        this.id = id;
        this.account = account;
        this.TMDB_ID = TMDB_ID;
    }

    public Favorites(Account account, Integer TMDB_ID, RecordStatus recordStatus) {
        super(recordStatus);
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
