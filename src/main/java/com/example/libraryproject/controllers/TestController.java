package com.example.libraryproject.controllers;

import com.example.libraryproject.entity.Reader;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test")
@Log4j2
public class TestController {
    @GetMapping("reader")
    public String testMethod() {
        Reader reader = new Reader();
        reader.setName("Some");
        reader.setSurname("Some");
        reader.setPhone("Some");
        reader.setPlaceToLive("Some");
        log.log(Level.INFO, reader.toString());
        return reader.toString();
    }
}
