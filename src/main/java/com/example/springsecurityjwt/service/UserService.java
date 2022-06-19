package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.models.AppUser;
import com.example.springsecurityjwt.models.Roles;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    Roles saveRole(Roles role);
    void  addRoleTOUser(String username, String roleName);
    AppUser getUser(String user);
    List<AppUser> getUsers();
}
