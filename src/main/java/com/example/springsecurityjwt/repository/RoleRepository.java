package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles,Long> {
    Roles findByName(String name);
}
