package com.example.aquariux_test.repository;

import com.example.aquariux_test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}