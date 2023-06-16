package com.gruppo4java11.MovieTips.entities;

import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

/**
 * Account class representing all the users of our application
 */
@Where(clause = "record_status = 'ACTIVE'")
@Entity
@Table(name = "account")
public class Account extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "surname")
    private String surname;
    @Column(nullable = false, unique = true, name = "email")
    private String email;
    @Column(nullable = false, name = "password")
    private String password;
    @Column(nullable = false, name = "birthday")
    private LocalDate birthday;
    @Column(nullable = false, name = "user_role")
    private String userRole;

    /**
     * Constructor for Account class
     * @param id id reference to database
     * @param name Name of the user
     * @param surname Surname of the user
     * @param email Email of the user
     * @param password Password of the user
     * @param birthday Birthday of the user
     * @param userRole Role privileges of the user
     * @param recordStatus Logical status of the user record
     */
    public Account(long id, String name, String surname, String email, String password, LocalDate birthday, String userRole, RecordStatus recordStatus) {
        super(recordStatus);
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.userRole = userRole;
    }

    public Account() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}


