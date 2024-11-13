package org.carexchangeapi.repository;

import org.carexchangelibrary.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
