package com.gruppo4java11.MovieTips.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * RentalOrder class representing all the orders that have been made in our application to
 * rent movies.
 */
@Where(clause = "record_status = 'ACTIVE'")
@Entity
@Table(name = "rental_order")
public class RentalOrder extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Account account;
    @Column(nullable = false, name = "order_time")
    private LocalDateTime orderTime;
    @Column(nullable = false, name = "return_time")
    private LocalDateTime returnTime;
    @Column(nullable = false, name = "order_status")
    private String orderStatus;

    /**
     * Constructor for our RentalOrder class
     * @param id  id that references our database
     * @param movie name of the movie that was ordered
     * @param account account of the user that made the order
     * @param orderTime the time in which the order was made
     * @param returnTime the time in which the movie was returned
     * @param orderStatus whether the movie is still in possession of the user or if it has been returned
     */
    public RentalOrder(long id, Movie movie, Account account, LocalDateTime orderTime, LocalDateTime returnTime, String orderStatus) {
        this.id = id;
        this.movie = movie;
        this.account = account;
        this.orderTime = orderTime;
        this.returnTime = returnTime;
        this.orderStatus = orderStatus;
    }

    public RentalOrder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
