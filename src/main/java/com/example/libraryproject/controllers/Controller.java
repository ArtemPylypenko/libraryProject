package com.example.libraryproject.controllers;

import com.example.libraryproject.dto.UserDto;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class Controller {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String goHome() {

        return "Public accsis";
    }

    @PostMapping("/reader/save")
    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
        userDto.password = passwordEncoder.encode(userDto.password);
        Reader reader = new Reader();
        reader.setEmail(userDto.email);
        reader.setPassword(userDto.password);
        return userRepo.existsById(reader.getId()) ? ResponseEntity.ok("User was saved") : ResponseEntity.status(404).body("Can t save user");

    }
}
