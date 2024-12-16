package com.example.aquariux_test.controller;

import com.example.aquariux_test.request.UserRequest;
import com.example.aquariux_test.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public boolean createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    // @GetMapping
    // public List<User> getAllUsers() {
    //     return userRepository.findAll();
    // }
}