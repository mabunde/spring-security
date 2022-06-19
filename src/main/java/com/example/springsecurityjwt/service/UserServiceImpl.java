package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.models.AppUser;
import com.example.springsecurityjwt.models.Roles;
import com.example.springsecurityjwt.repository.RoleRepository;
import com.example.springsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private  final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser= userRepository.findByUsername(username);
        if(appUser==null){
            log.error("username not found");
            throw new UsernameNotFoundException("username not found inthe database");
        }
        else {
            log.info("username found {} in the database",username);
        }
        Collection<SimpleGrantedAuthority> authorities= new ArrayList<>();
        appUser.getRoles().forEach(roles -> {
            authorities.add(new SimpleGrantedAuthority(roles.getName()));
        });
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("saving a user{}",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Roles saveRole(Roles role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleTOUser(String username, String roleName) {
        AppUser user =userRepository.findByUsername(username);
        Roles role =roleRepository.findByName(roleName);
        user.getRoles().add(role);

    }

    @Override
    public AppUser getUser(String user) {
        return userRepository.findByUsername(user);
    }

    @Override
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

}
