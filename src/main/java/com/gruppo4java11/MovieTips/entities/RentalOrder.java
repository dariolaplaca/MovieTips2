package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * RentalOrder class representing all the orders that have been made in our application to
 * rent movies.
 */
@Entity
@Table(name = "rental_order")
public class RentalOrder extends Record{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Account account;
    @Column(nullable = false, name = "order_time")
    private LocalDate orderTime;
    @Column(nullable = false, name = "return_time")
    private LocalDate returnTime;
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
     * @param recordStatus the logical status of the order within the DB i.e: Active or Deleted
     */
    public RentalOrder(long id, Movie movie, Account account, LocalDate orderTime, LocalDate returnTime, String orderStatus, RecordStatus recordStatus) {
        super(recordStatus);
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

    public LocalDate getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDate orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDate getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDate returnTime) {
        this.returnTime = returnTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
