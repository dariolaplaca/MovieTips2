package com.gruppo4java11.MovieTips.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//TODO Rimuovere Lombok ed utilizzare getter setter e costruttori
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class RentalOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Account account;
    @Column(nullable = false)
    private LocalDate orderTime;
    @Column(nullable = false)
    private LocalDate returnTime;
    @Column(nullable = false)
    private String orderStatus;
}
