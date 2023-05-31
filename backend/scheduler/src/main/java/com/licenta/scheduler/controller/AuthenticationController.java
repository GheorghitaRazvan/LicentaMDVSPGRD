package com.licenta.scheduler.controller;

import com.licenta.scheduler.model.LoginForm;
import com.licenta.scheduler.model.RegisterForm;
import com.licenta.scheduler.model.UserData;
import com.licenta.scheduler.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authService;

    @PostMapping("/login")
    @ResponseBody
    public UserData login(@RequestBody LoginForm loginFormData) {
        return authService.login(loginFormData.getEmail(), loginFormData.getPassword());
    }

    @PostMapping("/register/user")
    @ResponseBody
    public String register(@RequestBody RegisterForm registerFormData) {
        return authService.registerUser(registerFormData);
    }
}
