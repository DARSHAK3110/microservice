package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
