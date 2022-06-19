package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
}
