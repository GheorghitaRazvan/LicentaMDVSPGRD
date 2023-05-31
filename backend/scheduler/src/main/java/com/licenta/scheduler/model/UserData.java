package com.licenta.scheduler.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserData {
    private Long id;
    private String email;
    private String password;
    private String userType;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
