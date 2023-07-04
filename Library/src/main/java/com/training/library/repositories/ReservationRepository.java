package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Reservations;

public interface ReservationRepository extends JpaRepository<Reservations, Long>{

}
