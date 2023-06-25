package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.RentalOrder;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.RentalOrderRepository;
import com.gruppo4java11.MovieTips.services.RentalOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Controllers of the rental order entities
 */
@RestController
@RequestMapping("/api/order")
@Tag(name = "Orders API")
public class RentalOrderController {

    @Autowired
    private RentalOrderRepository rentalOrderRepository;
    private RentalOrderService rentalOrderService;

    /**
     * @param rentalOrderRepository the repository used for accessing and managing rental orders
     * @param rentalOrderService  the service used for performing operations on rental orders
     */
    public RentalOrderController(RentalOrderRepository rentalOrderRepository, RentalOrderService rentalOrderService) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderService = rentalOrderService;
    }
    /**
     * Creates a new rental order in the database.
     * @param rentalOrder  rental order to be created
     * @return ResponseEntity indicating the success of the rental order creation
     */
    @PostMapping("/create")
    public ResponseEntity<String> createRentalOrder(@RequestBody RentalOrder rentalOrder, @RequestParam String username) {
        rentalOrder.setCreatedBy(username);
        rentalOrder.setCreatedOn(LocalDateTime.now());
        rentalOrder.setModifiedBy(username);
        rentalOrder.setModifiedOn(LocalDateTime.now());
        rentalOrderRepository.saveAndFlush(rentalOrder);
        Long highestId = rentalOrderRepository.getHighestID();
        return ResponseEntity.ok("Order created with id " + highestId);
    }
    /**
     * This mapping retrieves the rental order with the specified ID
     * @param id ID of the movie to retrieve
     * @return the rental order with the specified ID, or null if not found
     */
    @GetMapping("/{id}")
    public RentalOrder getRentalOrder(@PathVariable long id) {
        return rentalOrderRepository.findById(id).orElse(null);
    }
    /**
     * This mapping retrieves all the rental order from the database
     * @return a list of the allo rental order
     */
    @GetMapping("/all")
    public List<RentalOrder> getAllRentalOrders() {
        return rentalOrderRepository.findAll();
    }
    /**
     * Updates the rental order with the specified ID
     * @param rentalOrder the updated rental order information
     * @param id ID of the rental order to update
     * @return ResponseEntity indicating the success of the rental order update
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRentalOrders(@RequestBody RentalOrder rentalOrder, @PathVariable long id, @RequestParam String username){
        RentalOrder rentalsFromDB = rentalOrderRepository.findById(id).orElse(null);
        if(rentalsFromDB == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found!");
        }
        rentalsFromDB.setAccount(rentalOrder.getAccount());
        rentalsFromDB.setOrderStatus(rentalOrder.getOrderStatus());
        rentalsFromDB.setOrderTime(rentalOrder.getOrderTime());
        rentalsFromDB.setReturnTime(rentalOrder.getReturnTime());
        rentalsFromDB.setMovie(rentalOrder.getMovie());
        rentalsFromDB.setModifiedOn(LocalDateTime.now());
        rentalsFromDB.setModifiedBy(username);
        rentalOrderRepository.saveAndFlush(rentalsFromDB);
        return ResponseEntity.ok("Order updated!");
    }
    /**
     * Deletes the rental order with the specified ID from the database
     * @param id ID of the rental order to delete
     * @return ResponseEntity indicating the success of the rental order deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRentalOrder(@PathVariable long id){
        rentalOrderRepository.deleteById(id);
        return ResponseEntity.ok("Order deleted!");
    }
    /**
     * Sets the status of a rental order identified by the given ID.
     *
     * @param id The ID of the rental order to update.
     * @return A ResponseEntity containing a message indicating the updated status of the rental order.
     */
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setAccountStatus(@PathVariable long id, @RequestParam String username){
        RentalOrder rentalOrderToChange = rentalOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found!"));
        if(rentalOrderToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) rentalOrderToChange.setRecordStatus(RecordStatus.DELETED);
        else rentalOrderToChange.setRecordStatus(RecordStatus.ACTIVE);
        rentalOrderToChange.setModifiedBy(username);
        rentalOrderToChange.setModifiedOn(LocalDateTime.now());
        rentalOrderRepository.updateStatusById(rentalOrderToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Order with id " + id + " Status Updated to " + rentalOrderToChange.getRecordStatus());
    }
}
