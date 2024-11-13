package org.carexchangeapi.controller;

import org.carexchangeapi.repository.CarRepository;
import org.carexchangelibrary.Car;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarAPIController {
    private final CarRepository repository;

    public CarAPIController(CarRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getById(@PathVariable long id) {
        Optional<Car> car = repository.findById(id);

        if (car.isPresent())
            return ResponseEntity.ok(car.get());  // 200 OK

        return ResponseEntity.notFound().build();  // 404 Not Found
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Car>> getByBrand(@PathVariable String brand) {
        List<Car> car = repository.findAll().stream().filter(
                p -> p.getBrand().toLowerCase().contains(brand.toLowerCase())
        ).toList();

        return ResponseEntity.ok(car);  // 200 OK
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Car>> getByModel(@PathVariable String model) {
        List<Car> car = repository.findAll().stream().filter(
                p -> p.getModel().toLowerCase().contains(model.toLowerCase())
        ).toList();

        return ResponseEntity.ok(car);  // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAll() {
        try {
            List<Car> cars = repository.findAll();
            return ResponseEntity.ok(cars);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<Car> post(@RequestBody Car car) {
        try {
            Car savedCar = repository.save(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);  // 201 Created
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();  // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> put(@PathVariable long id, @RequestBody Car car) {
        Optional<Car> existingCar = repository.findById(id);
        if (existingCar.isPresent()) {
            car.setId(existingCar.get().getId());

            Car updatedCar;
            try {
                updatedCar = repository.save(car);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();  // 409 Conflict
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();  // 400 Bad Request
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
            }

            return ResponseEntity.ok(updatedCar);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Optional<Car> existingCar = repository.findById(id);
        if (existingCar.isPresent()) {
            try {
                repository.delete(existingCar.get());
                return ResponseEntity.noContent().build();  // 204 No Content
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();  // 409 Conflict
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
            }
        }
        return ResponseEntity.notFound().build();  // 404 Not Found
    }
}