package org.carexchangeclient.controller;

import org.carexchangeclient.model.Status;
import org.carexchangeclient.service.CarService;
import org.carexchangelibrary.Car;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/cars")
public class CarClientController {

    private final CarService service;

    public CarClientController(CarService service) {
        this.service = service;
    }

    @GetMapping
    public String get(@RequestParam(required = false) String brand,
                      @RequestParam(required = false) String modelName, Model model) {
        try {
            model.addAttribute("isFound", true);

            List<Car> cars;
            if (brand != null && !brand.isEmpty())
                cars = service.getByBrand(brand);
            else if (modelName != null && !modelName.isEmpty())
                cars = service.getByModel(modelName);
            else
                cars = service.getAll();

            if (cars.isEmpty())
                model.addAttribute("isFound", false);

            model.addAttribute("cars", cars);
            model.addAttribute("brand", brand);
            model.addAttribute("modelName", modelName);
        } catch (NoSuchElementException ex) {
            model.addAttribute("status",
                    new Status(false, "Car not found", ex.getMessage()));
        } catch (Exception ex) {
            model.addAttribute("status",
                    new Status(false, "An unexpected error occurred", ex.getMessage()));
        }

        return "car/list";
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("method", "post");

        return "car/form";
    }

    @PostMapping("/create")
    public String post(@ModelAttribute Car car, Model model) {
        try {
            service.create(car);
            model.addAttribute("status",
                    new Status(true, "Car " + car.getBrand() + " " + car.getModel() + " have been successfully added."));
        } catch (Exception ex) {
            model.addAttribute("status",
                    new Status(false, "An unexpected error occurred", ex.getMessage()));

            return "car/form";
        }

        return "redirect:/cars";
    }

    @GetMapping("/{id}/update")
    public String getUpdate(@PathVariable long id, Model model) {
        try {
            model.addAttribute("car", service.getById(id));
            model.addAttribute("method", "put");
        } catch (NoSuchElementException ex) {
            model.addAttribute("status",
                    new Status(false, "Car not found", ex.getMessage()));

            return "car/list";
        } catch (Exception ex) {
            model.addAttribute("status",
                    new Status(false, "An unexpected error occurred", ex.getMessage()));

            return "car/list";
        }

        return "car/form";
    }

    @PostMapping("/update")
    public String put(@ModelAttribute Car car, Model model) {
        try {
            service.update(car);

            model.addAttribute("status",
                    new Status(true, "Car " + car.getBrand() + " " + car.getModel() + " have been successfully updated."));
        } catch (NoSuchElementException ex) {
            model.addAttribute("status",
                    new Status(false, "Car not found for update", ex.getMessage()));

            return "car/form";
        } catch (Exception ex) {
            model.addAttribute("status",
                    new Status(false, "An unexpected error occurred", ex.getMessage()));

            return "car/form";
        }

        return "redirect:/cars";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {
        try {
            service.delete(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("status",
                    new Status(false, "Car not found for deletion", ex.getMessage()));

            return "car/list";
        } catch (Exception ex) {
            model.addAttribute("status",
                    new Status(false, "An unexpected error occurred", ex.getMessage()));

            return "car/list";
        }

        return "redirect:/cars";
    }
}