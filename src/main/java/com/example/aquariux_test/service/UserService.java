package com.example.aquariux_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquariux_test.entity.User;
import com.example.aquariux_test.repository.UserRepository;
import com.example.aquariux_test.request.UserRequest;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean createUser(UserRequest userRequest) {
        User user = User.builder().name(userRequest.name()).email(userRequest.email()).build();
        
        userRepository.save(user);
        return true;
    }
}
