package com.licenta.scheduler.services;

import com.licenta.scheduler.model.*;
import com.licenta.scheduler.repository.AdminRepository;
import com.licenta.scheduler.repository.DriverRepository;
import com.licenta.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

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
}
