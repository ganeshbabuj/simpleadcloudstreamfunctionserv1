package com.example.cloudstream.resource;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String country;
    private UserStatus status;

}
