package com.example.aquariux_test.controller;

import com.example.aquariux_test.entity.Trade;
import com.example.aquariux_test.request.UserRequest;
import com.example.aquariux_test.response.WalletBalanceResponse;
import com.example.aquariux_test.service.UserService;

import java.util.List;

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

    @GetMapping("/wallet-balance/{userId}")
    public WalletBalanceResponse getUserWalletBalance(@PathVariable Long userId) {
        return userService.getUserWalletBalance(userId);
    }

    @GetMapping("/trade-history/{userId}")
    public List<Trade> getTradeHistory(@PathVariable Long userId) {
        return userService.getUserTradeHistory(userId);
    }
}