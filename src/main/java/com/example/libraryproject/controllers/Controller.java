package com.example.libraryproject.controllers;

import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.services.ReaderService;
import com.example.libraryproject.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@EnableWebMvc
@org.springframework.stereotype.Controller
public class Controller {
    private final UserService userService;
    private final ReaderService readerService;

    private final PasswordEncoder passwordEncoder;

    public Controller(UserService userService, ReaderService readerService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.readerService = readerService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/afterLogin")
    public RedirectView afterLogin(RedirectAttributes attributes) {
        attributes.addFlashAttribute("message", "Ласкаво просимо! Ви успішно авторизувалися.");
        String role = getLoggedUserRole();
        return switch (role) {
            case "ADMIN" -> new RedirectView("/admin");
            case "READER" -> new RedirectView("/reader");
            case "LIBRARIAN" -> new RedirectView("/librarian");
            default -> new RedirectView("/home");
        };
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('READER')")
    public String goHome() {
        return "test";
    }

//    @PostMapping("/reader/save")
//    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
//        userDto.password = passwordEncoder.encode(userDto.password);
//        Reader reader = new Reader();
//        reader.setEmail(userDto.email);
//        reader.setPassword(userDto.password);
//        return userRepo.existsById(reader.getId()) ? ResponseEntity.ok("User was saved") : ResponseEntity.status(404).body("Can t save user");
//    }

    @GetMapping("/librarian")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String librarianPage() {
        return "Hello librarian";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPage(Model model) {
        return "test";
    }

    @GetMapping("/reader")
    @PreAuthorize("hasAuthority('READER')")
    public String readerPage() {

        Optional<Reader> reader = readerService.findByEmail("reader1@gmail.com");
        if (reader.isPresent()) {
            readerService.delete(reader.get());
        }

        return "Hello reader";
    }

    public UserDetails getLoggedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal());
        }
        return null;
    }

    public String getLoggedUserRole() {
        return getLoggedUserDetails().getAuthorities().toArray()[0].toString();
    }
}
