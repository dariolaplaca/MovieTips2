package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.RentalOrder;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.RentalOrderRepository;
import com.gruppo4java11.MovieTips.services.AccountService;
import com.gruppo4java11.MovieTips.services.RentalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private AccountService accountService;
    @Autowired
    private RentalOrderService rentalOrderService;

    /**
     * @param rentalOrderRepository the repository used for accessing and managing rental orders
     * @param rentalOrderService  the service used for performing operations on rental orders
     */
    public RentalOrderController(RentalOrderRepository rentalOrderRepository, RentalOrderService rentalOrderService, AccountService accountService) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderService = rentalOrderService;
        this.accountService = accountService;
    }

    /**
     * Creates a new rental order in the database.
     * @param movieID ID of the selected movie
     * @param rentalDays amount of days the user wishes to rent the movie
     * @param accountId ID of the user's account
     * @param username Username of the user's account
     * @return Response entity indicating the success of the created order
     */
    @Operation(summary = "Create a new Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) })
    })
    @PostMapping("/create")
    public ResponseEntity<String> createRentalOrder(@Parameter(description = "Body of the order to add") @RequestParam Long movieID, @Parameter(description = "Name of the user that is adding the order")@RequestParam Integer rentalDays, @Parameter(description = "ID of the user's account")@RequestParam Long accountId, @Parameter(description = "Name of the user that is performing the creation") @RequestParam String username) {
        RentalOrder rentalOrder = accountService.confirmRentalOrder(movieID, rentalDays, accountId);
        rentalOrder.setCreatedBy(username);
        rentalOrder.setCreatedOn(LocalDateTime.now());
        rentalOrder.setModifiedBy(username);
        rentalOrder.setModifiedOn(LocalDateTime.now());
        rentalOrderRepository.saveAndFlush(rentalOrder);
        Long highestId = rentalOrderRepository.getHighestID();
        return ResponseEntity.ok("Order created with id " + highestId);
    }

    /**
     * Allows the user to proceed to the checkout screen where the total cost of the rental is calculated whether it was returned on time or if it was returned late, thust applying a penalty fee.
     * @param rentalOrderId The ID of the specific rental order that was made by the user
     * @return a Response entity that confirms whether the payment was successful or not.
     */
    @Operation(summary = "Proceed to checkout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment went through successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) })
    })
    @GetMapping("/checkout")
    public ResponseEntity<String> paymentCheckout(@Parameter(description = "The rental order with its respective cost") @RequestParam Long rentalOrderId) {
        Double cost = accountService.paymentCheckout(rentalOrderRepository.findById(rentalOrderId).get());
        Double penaltyFee = accountService.getPenaltyFee(rentalOrderRepository.findById(rentalOrderId).get());
        return ResponseEntity.ok("Total cost of movie is:  " + cost + " of which " + penaltyFee + " have been added as a penalty fee.");
    }
    /**
     * Allows the user to proceed to the checkout screen where the total cost of the rental is calculated whether it was returned on time or if it was returned late, thust applying a penalty fee.
     * @param rentalOrderId The ID of the specific rental order that was made by the user
     * @return a Response entity that confirms whether the payment was successful or not.
     */
    @Operation(summary = "TEST to see Penalty in action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment went through successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalOrder.class)) })
    })
    @GetMapping("/future-checkout")
    public ResponseEntity<String> futurePaymentCheckout(@Parameter(description = "The rental order with its respective cost") @RequestParam Long rentalOrderId) {
        Double cost = accountService.futurePaymentCheckout(rentalOrderRepository.findById(rentalOrderId).get());
        Double penaltyFee = accountService.getFuturePenaltyFee(rentalOrderRepository.findById(rentalOrderId).get());
        return ResponseEntity.ok("Total cost of movie is:  " + cost  + " of which " + penaltyFee + " have been added as a penalty fee.");
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
     * This mapping retrieves all the rental orders from the database
     * @return a list of all the rental orders
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
