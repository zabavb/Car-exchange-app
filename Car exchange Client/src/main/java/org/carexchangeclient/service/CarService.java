package org.carexchangeclient.service;

import org.carexchangelibrary.Car;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CarService {
    private final WebClient webClient;

    public CarService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/cars").build();
    }

    // http://localhost:8081/api/cars/{id} GET
    public Car getById(long id) {
        return webClient.get().uri("/{id}", id).retrieve().bodyToMono(Car.class).block();
    }

    // http://localhost:8081/api/cars/brand/{brand} GET
    public List<Car> getByBrand(String brand) {
        return webClient.get().uri("/brand/{brand}", brand).retrieve().bodyToFlux(Car.class).collectList().block();
    }

    // http://localhost:8081/api/cars/model/{model} GET
    public List<Car> getByModel(String model) {
        return webClient.get().uri("/model/{model}", model).retrieve().bodyToFlux(Car.class).collectList().block();
    }

    // http://localhost:8081/api/cars GET
    public List<Car> getAll() {
        return webClient.get().uri("").retrieve().bodyToFlux(Car.class).collectList().block();
    }

    // http://localhost:8081/api/cars POST
    public Car create(Car car) {
        return webClient.post().uri("").bodyValue(car).retrieve().bodyToMono(Car.class).block();
    }

    // http://localhost:8081/api/cars/{id} PUT
    public Car update(Car car) {
        return webClient.put().uri("/{id}", car.getId()).bodyValue(car).retrieve().bodyToMono(Car.class).block();
    }

    // http://localhost:8081/api/cars/{id} DELETE
    public void delete(long id) {
        webClient.delete().uri("/{id}", id).retrieve().toBodilessEntity().block();
    }
}
