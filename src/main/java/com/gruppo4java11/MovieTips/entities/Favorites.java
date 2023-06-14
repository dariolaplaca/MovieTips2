package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.*;

/**
 * Favorites class representing all the favorites of users in our application
 */
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

    /**
     * Constructor for Favorites class
     * @param id id reference to database
     * @param account id reference of account
     * @param TMDB_ID id reference of external api
     * @param recordStatus Logical status of the user recod
     */
    public Favorites(Long id, Account account, Integer TMDB_ID, RecordStatus recordStatus) {
        super(recordStatus);
        this.id = id;
        this.account = account;
        this.TMDB_ID = TMDB_ID;
    }

    /**
     * Same constructor for Favorites class without id
     */
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
