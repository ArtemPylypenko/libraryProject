package com.example.libraryproject.controllers;

import com.example.libraryproject.entity.Librarian;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.entity.Role;
import com.example.libraryproject.services.ReaderService;
import com.example.libraryproject.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
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


//    @PostMapping("/reader/save")
//    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
//        userDto.password = passwordEncoder.encode(userDto.password);
//        Reader reader = new Reader();
//        reader.setEmail(userDto.email);
//        reader.setPassword(userDto.password);
//        return userRepo.existsById(reader.getId()) ? ResponseEntity.ok("User was saved") : ResponseEntity.status(404).body("Can t save user");
//    }


    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPage(Model model) {
        //TODO:дістати з бд
        ArrayList<Librarian> lib = new ArrayList<>();
        Librarian librarian = new Librarian();
        librarian.setEmail("qwe");
        librarian.setId(1L);
        lib.add(librarian);
        lib.add(librarian);
        lib.add(librarian);
        lib.add(librarian);
        lib.add(librarian);
        lib.add(librarian);

        model.addAttribute("librarians", lib);
        return "admin/main_page_admin";
    }

    @GetMapping("/librarian/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAddLibrarianPAge(Model model) {
        return "admin/new_librarian";
    }

    @PostMapping("/librarian/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RedirectView addLibrarian(@RequestParam("email") String email, @RequestParam String password, RedirectAttributes attributes) {
        //TODO:перевірка в бд
        Librarian librarian = new Librarian();
        librarian.setRole(Role.LIBRARIAN);
        librarian.setPassword(password);
        librarian.setEmail(email);
        attributes.addFlashAttribute("success", "Can t add librarian");
        return new RedirectView("/admin");
    }

    @GetMapping("/librarian/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditLibrarianPage(@PathVariable(value = "id") Long id, Model model) {
        Librarian attributeValue = new Librarian();
        attributeValue.setEmail("someEmail");
        attributeValue.setPassword("pass");
        attributeValue.setId(8L);
        model.addAttribute("librarian", attributeValue);
        return "admin/edit_librarian";
    }

    @PostMapping("/librarian/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RedirectView editLibrarian(@RequestParam("email") String email, @RequestParam("password") String password, @PathVariable(value = "id") Long id, Model model) {
        System.out.println(id);
        return new RedirectView("/admin");
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
