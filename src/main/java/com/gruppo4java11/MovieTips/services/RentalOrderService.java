package com.gruppo4java11.MovieTips.services;

import com.gruppo4java11.MovieTips.repositories.RentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class that include all the Services and function of the Order entity
 */
@Service
public class RentalOrderService {

    @Autowired
    private RentalOrderRepository rentalOrderRepository;

    public RentalOrderService(RentalOrderRepository rentalOrderRepository) {
        this.rentalOrderRepository = rentalOrderRepository;
    }
}
