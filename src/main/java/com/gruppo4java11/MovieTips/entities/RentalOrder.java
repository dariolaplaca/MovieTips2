package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rental_order")
public class RentalOrder extends Record{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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

    public RentalOrder(long id, Movie movie, Account account, LocalDate orderTime, LocalDate returnTime, String orderStatus) {
        super(RecordStatus.ACTIVE);
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
