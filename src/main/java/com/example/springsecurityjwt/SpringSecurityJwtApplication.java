package com.example.springsecurityjwt;

import com.example.springsecurityjwt.models.AppUser;
import com.example.springsecurityjwt.models.Roles;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService){
		return args -> {
			userService.saveRole(new Roles(null,"USER_ROLE"));
			userService.saveRole(new Roles(null,"USER_ADMIN"));
			userService.saveRole(new Roles(null,"USER_MANAGER"));
			userService.saveRole(new Roles(null,"USER_SUPER_ADMIN"));
			userService.saveRole(new Roles(null,"USER_SENIOR"));


			userService.saveUser(new AppUser(null,"Daniel Senior",
					"SDaniel","12345",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Amor Kenya",
					"AKenya","12346",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Alice Obama",
					"AObama","12347",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Sam Kay",
					"SKay","12348",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Geoffrey Wekesa",
					"GWekesa","12349",new ArrayList<>()));

			userService.addRoleTOUser("SDaniel","USER_ROLE");
			userService.addRoleTOUser("SDaniel","USER_MANAGER");
			userService.addRoleTOUser("AKenya","USER_ADMIN");
			userService.addRoleTOUser("AObama","USER_MANAGER");
			userService.addRoleTOUser("SKay","USER_SUPER_ADMIN");
			userService.addRoleTOUser("GWekesa","USER_SENIOR");

		};
	}
}
