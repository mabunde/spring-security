package com.example.springsecurityjwt.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springsecurityjwt.models.AppUser;
import com.example.springsecurityjwt.models.Roles;
import com.example.springsecurityjwt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity <List<AppUser>>getUser(){
        return ResponseEntity.ok().body(userService.getUsers());
    };

    @PostMapping("/user/save")
    public ResponseEntity <AppUser>saveUser(@RequestBody AppUser user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    };
    @PostMapping("/role/save")
    public ResponseEntity <Roles>saveRole(@RequestBody Roles role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    };
    @PostMapping("/role/addToUser")
    public ResponseEntity <?>addRoleToUser(@RequestBody RoleToUserForm form){
        userService.addRoleTOUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().
                build();
    };
    @PostMapping("/api/token/refresh")
    public void refresh_token(HttpServletRequest request, HttpServletResponse response){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader !=null &&authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token =authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm=Algorithm.HMAC256("secrete".getBytes());
                JWTVerifier verifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT=verifier.verify(refresh_token);
                String username=decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                //tell spring security the parameters needed and their authorization
                String access_token = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*100))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Roles::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> tokens= new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);


            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
            }
        }else {
            throw new RuntimeException("The refresh token is missing");
        }
            };

}
@Data
class RoleToUserForm{
    private  String username;
    private String roleName;

}
