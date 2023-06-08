package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.RentalOrder;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.repositories.RentalOrderRepository;
import com.gruppo4java11.MovieTips.services.RentalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class RentalOrderController {

    @Autowired
    private RentalOrderRepository rentalOrderRepository;
    private RentalOrderService rentalOrderService;

    public RentalOrderController(RentalOrderRepository rentalOrderRepository, RentalOrderService rentalOrderService) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderService = rentalOrderService;
    }

    @PostMapping
    public ResponseEntity<String> createRentalOrder(@RequestBody RentalOrder rentalOrder) {
        rentalOrderRepository.saveAndFlush(rentalOrder);
        return ResponseEntity.ok("Order created!");
    }
    @GetMapping("/{id}")
    public RentalOrder getRentalOrder(@PathVariable long id) {
        return rentalOrderRepository.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<RentalOrder> getAllRentalOrders() {
        return rentalOrderRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRentalOrders(@RequestBody RentalOrder rentalOrder, @PathVariable long id){
        RentalOrder rentalsFromDB = rentalOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found!"));
        rentalsFromDB.setAccount(rentalOrder.getAccount());
        rentalsFromDB.setOrderStatus(rentalOrder.getOrderStatus());
        rentalsFromDB.setOrderTime(rentalOrder.getOrderTime());
        rentalsFromDB.setReturnTime(rentalOrder.getReturnTime());
        rentalsFromDB.setMovie(rentalOrder.getMovie());
        rentalOrderRepository.saveAndFlush(rentalsFromDB);
        return ResponseEntity.ok("Order updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRentalOrder(@PathVariable long id){
        rentalOrderRepository.deleteById(id);
        return ResponseEntity.ok("Order deleted!");
    }

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setAccountStatus(@PathVariable long id){
        RentalOrder rentalOrderToChange = rentalOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found!"));
        if(rentalOrderToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) rentalOrderToChange.setRecordStatus(RecordStatus.DELETED);
        else rentalOrderToChange.setRecordStatus(RecordStatus.ACTIVE);
        rentalOrderRepository.updateStatusById(rentalOrderToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Order with id " + id + " Status Updated to " + rentalOrderToChange.getRecordStatus());
    }
}
