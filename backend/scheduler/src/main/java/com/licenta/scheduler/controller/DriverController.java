package com.licenta.scheduler.controller;

import com.licenta.scheduler.exceptions.UserNotFoundException;
import com.licenta.scheduler.model.Driver;
import com.licenta.scheduler.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping("/drivers")
    public List<Driver> allDrivers(){
        return driverRepository.findAll();
    }

    @GetMapping("/drivers/{id}")
    public Driver getSpecifiedUser(@PathVariable Long id) {
        return driverRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }
    @PostMapping("/drivers")
    public Driver createNewDriver(@RequestBody Driver newDriver) {
        return driverRepository.save(newDriver);
    }


    @PutMapping("/drivers/{id}")
    Driver updateDriver(@RequestBody Driver newDriverData, @PathVariable Long id) {
        return driverRepository.findById(id)
                .map(driver -> {
                    driver.setEmail(newDriverData.getEmail());
                    driver.setPassword(newDriverData.getPassword());
                    return driverRepository.save(driver);
                })
                .orElseGet(() -> {
                    newDriverData.setId(id);
                    return driverRepository.save(newDriverData);
                });
    }
}
