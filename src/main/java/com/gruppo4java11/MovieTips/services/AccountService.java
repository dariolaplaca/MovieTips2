package com.gruppo4java11.MovieTips.services;

import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.entities.RentalOrder;
import com.gruppo4java11.MovieTips.exception.MovieNotFoundException;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import com.gruppo4java11.MovieTips.repositories.RentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Class that include all the Services and function of the Account entity
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RentalOrderRepository rentalOrderRepository;

    public AccountService (AccountRepository accountRepository, MovieRepository movieRepository, RentalOrderRepository rentalOrderRepository) {
        this.accountRepository = accountRepository;
        this.movieRepository = movieRepository;
        this.rentalOrderRepository = rentalOrderRepository;
    }

    /**
     * Allows the user to select a specific movie to rent by ID from the TMDB external Database
     * @param id
     * @return
     */
    public Movie findMovieByID (Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            return movie.get();
        } else {
            throw new MovieNotFoundException("Movie not found with ID: " + id);
        }
    }

    /**
     * This returns the total rental cost of the selected movie by inputting an amount of days and also considering a penalty fee for not respecting the max limit of days.
     * @param movieID
     * @param rentalDays
     * @return
     */
    public double getRentalCost (RentalOrder rentalOrder) {
        Movie movie = rentalOrder.getMovie();
        Long rentalDays = Duration.between(rentalOrder.getOrderTime(), rentalOrder.getReturnTime()).toDays();
        return movie.getCostPerDay() * rentalDays;
    }

    /**
     * This confirms the order and sets the status of the selected movie as "IN PROGRESS" or RENTED if the movie is in stock. Otherwise it will return an error
     * @param movieID
     */
    public RentalOrder confirmRentalOrder (Long movieID, Integer rentalDays, Long accountId) {  //controller
        Movie movie = findMovieByID(movieID);
        RentalOrder rentalOrder = new RentalOrder();
        if (movie.getStockQuantity() > 0) {

            rentalOrder.setMovie(movie);
            rentalOrder.setOrderTime(LocalDateTime.now());
            rentalOrder.setOrderStatus("IN_PROGRESS");
            rentalOrder.setReturnTime(LocalDateTime.now().plusDays(rentalDays));
            rentalOrder.setAccount(accountRepository.findById(accountId).get());


            movie.setStockQuantity(movie.getStockQuantity() - 1);
            movieRepository.saveAndFlush(movie);

            paymentCheckout(rentalOrder);
        } else {
            throw new MovieNotFoundException("Movie is currently out of stock");
        }
        return rentalOrder;
    }

    /**
     * This applies a penalty fee in the event that the user returns the movie beyond the selected amount of days by 1.5 * per day the movie results unreturned.
     * @param rentalOrder
     * @return
     */
    public double getPenaltyFee (RentalOrder rentalOrder) {
        if (rentalOrder.getReturnTime().isBefore(LocalDateTime.now()) && rentalOrder.getOrderStatus().equals("IN_PROGRESS")) {
            Long daysBetween = Duration.between(rentalOrder.getReturnTime(), LocalDateTime.now()).toDays();
            return daysBetween * rentalOrder.getMovie().getCostPerDay() * 1.5;
        }
        return 0;
    }

    public void returnMovie(Long rentalOrderID) {
        Optional<RentalOrder> optionalRentalOrder = rentalOrderRepository.findById(rentalOrderID);
        if (optionalRentalOrder.isPresent()) {
            RentalOrder rentalOrder = optionalRentalOrder.get();
            Movie movie = rentalOrder.getMovie();
            movie.setStockQuantity(movie.getStockQuantity() + 1);
            rentalOrder.setOrderStatus("RETURNED");
            rentalOrderRepository.saveAndFlush(rentalOrder);
            movieRepository.saveAndFlush(movie);
        }
    }

    public List<RentalOrder> findAllOrdersForTheAccount(Long accountId) {
        return rentalOrderRepository.getRentalOrderListByAccountId(accountId);
    }

    public List<RentalOrder> findAllOrdersInProgressForTheAccount(Long accountId) {
        return rentalOrderRepository.getRentalOrderListByAccountInProgress(accountId);
    }

    /**
     * Allows user to check out taking into consideration whether there is or isn't a penalty fee.
     * @param rentalOrder
     */
    public double paymentCheckout(RentalOrder rentalOrder) {
        if (getPenaltyFee(rentalOrder) > 0 ){
            return getRentalCost(rentalOrder) + getPenaltyFee(rentalOrder);
        }
        return getRentalCost(rentalOrder);
    }

    public double getFuturePenaltyFee (RentalOrder rentalOrder) {
        if (rentalOrder.getReturnTime().isBefore(LocalDateTime.now().plusDays(10)) && rentalOrder.getOrderStatus().equals("IN_PROGRESS")) {
            Long daysBetween = Duration.between(rentalOrder.getReturnTime(), LocalDateTime.now().plusDays(10)).toDays();
            return daysBetween * rentalOrder.getMovie().getCostPerDay() * 1.5;
        }
        return 0;
    }

    public double futurePaymentCheckout(RentalOrder rentalOrder) {
        if (getFuturePenaltyFee(rentalOrder) > 0 ){
            return getRentalCost(rentalOrder) + getFuturePenaltyFee(rentalOrder);
        }
        return getRentalCost(rentalOrder);
    }
}
