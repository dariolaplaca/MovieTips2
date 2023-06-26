package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.RentalOrder;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.RentalOrderRepository;
import com.gruppo4java11.MovieTips.services.RentalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) })
    })
    @PostMapping("/create")
    public ResponseEntity<String> createRentalOrder(@Parameter(description = "Body of the order to add") @RequestBody RentalOrder rentalOrder, @Parameter(description = "Name of the user that is adding the order")@RequestParam String username) {
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
    @Operation(summary = "Get an Order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) }),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @GetMapping("/{id}")
    public RentalOrder getRentalOrder(@Parameter(description = "Id of the order")@PathVariable long id) {
        return rentalOrderRepository.findById(id).orElse(null);
    }
    /**
     * This mapping retrieves all the rental order from the database
     * @return a list of the allo rental order
     */
    @Operation(summary = "Get all Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All orders successfully retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) }),
    })
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
    @Operation(summary = "Update an Order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) }),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRentalOrders(@Parameter(description = "Body containing the account parameters to update") @RequestBody RentalOrder rentalOrder, @Parameter(description = "Id of the order to update")@PathVariable long id, @Parameter(description = "Name of the user that is updating the order")@RequestParam String username){
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
    @Operation(summary = "Delete an Order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) }),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRentalOrder(@Parameter(description = "Id of the order")@PathVariable long id){
        rentalOrderRepository.deleteById(id);
        return ResponseEntity.ok("Order deleted!");
    }
    /**
     * Sets the status of a rental order identified by the given ID.
     *
     * @param id The ID of the rental order to update.
     * @return A ResponseEntity containing a message indicating the updated status of the rental order.
     */
    @Operation(summary = "Set the logical status of an Order's record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status successfully changed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) }),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setAccountStatus(@Parameter(description = "Id of the order")@PathVariable long id,@Parameter(description = "Name of the user that is changing the status")@RequestParam String username){
        RentalOrder rentalOrderToChange = rentalOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found!"));
        if(rentalOrderToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) rentalOrderToChange.setRecordStatus(RecordStatus.DELETED);
        else rentalOrderToChange.setRecordStatus(RecordStatus.ACTIVE);
        rentalOrderToChange.setModifiedBy(username);
        rentalOrderToChange.setModifiedOn(LocalDateTime.now());
        rentalOrderRepository.updateStatusById(rentalOrderToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Order with id " + id + " Status Updated to " + rentalOrderToChange.getRecordStatus());
    }
}
