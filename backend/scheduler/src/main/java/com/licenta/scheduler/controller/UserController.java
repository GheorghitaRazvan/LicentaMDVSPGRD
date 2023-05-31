package com.licenta.scheduler.controller;

import com.licenta.scheduler.exceptions.UserNotFoundException;
import com.licenta.scheduler.model.User;
import com.licenta.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> allUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getSpecifiedUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }
    @PostMapping("/users")
    public User createNewUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }


    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User newUserData, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                            user.setEmail(newUserData.getEmail());
                            user.setPassword(newUserData.getPassword());
                            user.setFirstName(newUserData.getFirstName());
                            user.setLastName(newUserData.getLastName());
                            user.setPhoneNumber(newUserData.getPhoneNumber());
                            return userRepository.save(user);
                        })
                .orElseGet(() -> {
                    newUserData.setId(id);
                    return userRepository.save(newUserData);
                });
    }
}
