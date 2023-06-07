package com.gruppo4java11.MovieTips.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//TODO Rimuovere Lombok ed utilizzare getter setter e costruttori
//TODO Aggiungere le column name e table name
//TODO Creare un enumerato per lo record status con (D, A)
//TODO Vedere filtro globale o query custom per il where
//TODO Auditable Interface createdBy, createdOn, modifyBy, modifyOn (Account and LocalDate)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate birthday;
    @Column(nullable = false, name = "user_role")
    private String userRole;
}

