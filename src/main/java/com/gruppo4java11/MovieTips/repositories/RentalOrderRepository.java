package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
}
