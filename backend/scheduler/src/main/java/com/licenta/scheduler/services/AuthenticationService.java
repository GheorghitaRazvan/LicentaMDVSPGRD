package com.licenta.scheduler.services;

import com.licenta.scheduler.model.*;
import com.licenta.scheduler.model.frontData.DriverRegisterForm;
import com.licenta.scheduler.model.frontData.RegisterForm;
import com.licenta.scheduler.model.frontData.UserData;
import com.licenta.scheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.Long.parseLong;

@Service
public class AuthenticationService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private Encrypter encrypter;

    public UserData login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        String encryptedPassword = encrypter.encrypt(password);
        if(admin != null && admin.getPassword().equals(encryptedPassword))
        {
            return UserData.builder()
                    .id(admin.getId())
                    .email(admin.getEmail())
                    .password(encrypter.encrypt(admin.getPassword()))
                    .userType("admin")
                    .firstName("Admin")
                    .lastName("Account")
                    .phoneNumber("None")
                    .build();
        }
        Driver driver = driverRepository.findByEmail(email);
        if(driver != null && driver.getPassword().equals(encryptedPassword))
        {
            return UserData.builder()
                    .id(driver.getId())
                    .email(driver.getEmail())
                    .password(encrypter.encrypt(driver.getPassword()))
                    .userType("driver")
                    .firstName(driver.getFirstName())
                    .lastName(driver.getLastName())
                    .phoneNumber("None")
                    .build();
        }
        User user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(encryptedPassword))
        {
            return UserData.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password((user.getPassword()))
                    .userType("user")
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        }
        return null;
    }

    public String registerUser(RegisterForm registerFormData) {
        if(userRepository.findByEmail(registerFormData.getEmail()) != null)
        {
            return "Email already in use";
        }
        User user = new User();
        user.setEmail(registerFormData.getEmail());
        user.setPassword(encrypter.encrypt(registerFormData.getPassword()));
        user.setFirstName(registerFormData.getFirstName());
        user.setLastName(registerFormData.getLastName());
        user.setPhoneNumber(registerFormData.getPhoneNumber());
        userRepository.save(user);
        return "Account successfully created";
    }

    public String registerDriver(DriverRegisterForm registerFormData) {
        if(driverRepository.findByEmail(registerFormData.getEmail()) != null)
        {
            return "Email already in use";
        }
        Driver driver = new Driver();
        driver.setEmail(registerFormData.getEmail());
        driver.setPassword(encrypter.encrypt(registerFormData.getPassword()));
        driver.setFirstName(registerFormData.getFirstName());
        driver.setLastName(registerFormData.getLastName());

        Long depotId = parseLong(registerFormData.getDepotId());
        Optional<Location> depot = locationRepository.findById(depotId);
        if(depot.isPresent())
        {
            Location foundDepot = depot.get();

            Vehicle vehicle = new Vehicle();
            vehicle.setCapacity(3L);
            vehicle.setDriver(driver);
            vehicle.setDepot(foundDepot);

            driver.setVehicle(vehicle);
            driverRepository.save(driver);

            vehicleRepository.save(vehicle);
            return "Account successfully created";
        }
        else
        {
            return "Depot not found";
        }
    }
}
