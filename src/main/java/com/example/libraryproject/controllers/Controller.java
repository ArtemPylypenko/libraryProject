package com.example.libraryproject.controllers;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.Librarian;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.entity.Role;
import com.example.libraryproject.services.BookService;
import com.example.libraryproject.services.LibrarianService;
import com.example.libraryproject.services.ReaderService;
import com.example.libraryproject.services.UserService;
import lombok.RequiredArgsConstructor;
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

import java.util.Objects;
import java.util.Optional;

@EnableWebMvc
@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {
    private final UserService userService;
    private final ReaderService readerService;
    private final LibrarianService librarianService;
    private final BookService bookService;
    private final PasswordEncoder passwordEncoder;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @GetMapping("/afterLogin")
    public RedirectView afterLogin() {
        Role role = Role.valueOf(getLoggedUserRole());
        return switch (role) {
            case ADMIN -> new RedirectView("/admin");
            case READER -> new RedirectView("/reader");
            case LIBRARIAN -> new RedirectView("/librarian");
        };
    }

    // ==== ADMIN ====
    //
    //
    //
    // ==== ADMIN ====

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPage(Model model) {
        model.addAttribute("librarians", librarianService.getAll());
        return "admin/main_page_admin";
    }

    @GetMapping("/librarian/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAddLibrarianPAge() {
        return "admin/new_librarian";
    }

    @PostMapping("/librarian/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RedirectView addLibrarian(@RequestParam("email") String email, @RequestParam String password, RedirectAttributes attributes) {
        if (!librarianService.existByEmail(email)) {
            Librarian librarian = new Librarian();
            librarian.setRole(Role.LIBRARIAN);
            librarian.setPassword(password);
            librarian.setEmail(email);
            librarianService.save(librarian);
            attributes.addFlashAttribute(SUCCESS, "Librarian added!");
        } else {
            attributes.addFlashAttribute(ERROR, "Librarian with such email already exist!");
        }
        return new RedirectView("/admin");
    }

    @GetMapping("/librarian/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditLibrarianPage(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("librarian", librarianService.getById(id).get());
        return "admin/edit_librarian";
    }

    @PostMapping("/librarian/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RedirectView editLibrarian(@RequestParam("email") String email, @RequestParam("password") String password, @PathVariable(value = "id") Long id, RedirectAttributes attributes) {
        if (Objects.equals(email, "") || Objects.equals(password, "")) {
            attributes.addFlashAttribute(ERROR, "U should avoid empty fields!");
            return new RedirectView("/admin");
        }
        if (librarianService.existByEmail(email)) {
            attributes.addFlashAttribute(ERROR, "Such email already exist!");
            return new RedirectView("/admin");
        }
        librarianService.updateById(email, password, id);
        attributes.addFlashAttribute(SUCCESS, "Edited successfully");

        return new RedirectView("/admin");
    }

    @PostMapping("/librarian/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RedirectView editLibrarian(@PathVariable(value = "id") Long id, RedirectAttributes attributes) {

        librarianService.deleteById(id);
        attributes.addFlashAttribute(SUCCESS, "Deleted successfully");

        return new RedirectView("/admin");
    }

    // ==== LIBRARIAN ====
    //
    //
    //
    // ==== LIBRARIAN ====

    @GetMapping("/librarian")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String librarianPage(Model model) {

        model.addAttribute("readers", readerService.getAll());
        model.addAttribute("books", bookService.getAll());

        return "librarian/main_page_librarian";
    }

    @GetMapping("/book/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String getAddBookPage() {
        return "librarian/new_book";
    }

    @PostMapping("/book/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView addBook(@RequestParam("name") String name,
                                @RequestParam("authors") String authors,
                                @RequestParam("given_by") String given_by,
                                @RequestParam("isbn") String isbn,
                                @RequestParam("publication") int publication) {

        Book book = new Book();
        book.setName(name);
        book.setAuthors(authors);
        book.setGivenBy(given_by);
        book.setIsbn(isbn);
        book.setPublication(publication);
        book.setRating(3D);
        book.setAvailable(true);

        bookService.save(book);
        return new RedirectView("/librarian");
    }


    @GetMapping("book/edit/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String editBook(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.getById(id).get());
        return "librarian/edit_book";
    }

    @PostMapping("book/edit/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView editBook(@RequestParam("title") String name,
                                 @RequestParam("author") String authors,
                                 @RequestParam("given_by") String givenBy,
                                 @RequestParam("publication") int publication,
                                 @RequestParam("isbn") String isbn,
                                 @PathVariable(value = "id") Long id,
                                 RedirectAttributes attributes) {
        if (Objects.equals(name, "") || Objects.equals(authors, "") || Objects.equals(givenBy, "")
                || Objects.equals(isbn, "")) {
            attributes.addFlashAttribute(ERROR, "U should avoid empty fields!");
            return new RedirectView("/librarian");
        }

        bookService.updateById(name, authors, publication, isbn, givenBy, id);
        attributes.addFlashAttribute(SUCCESS, "Edited successfully");

        return new RedirectView("/librarian");
    }

    @PostMapping("book/delete/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView deleteBook(@PathVariable("id") Long id, RedirectAttributes attributes) {
        attributes.addFlashAttribute(SUCCESS, "book deleted!");
        bookService.deleteById(id);
        return new RedirectView("/librarian");
    }


    @GetMapping("reader/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String getAddReaderPage() {
        return "librarian/new_reader";
    }

    @PostMapping("/reader/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView addReader(@RequestParam("author") String author,
                                  @RequestParam("publishing_house") String publishing_house,
                                  @RequestParam("name") String name,
                                  @RequestParam("isbn") String isbn) {
        Book book = new Book();
        book.setAuthors(author);
        book.setName(name);
        book.setIsbn(isbn);
        book.setAvailable(true);

        bookService.save(book);
        return new RedirectView("/librarian");
    }


    @GetMapping("/reader")
    @PreAuthorize("hasAuthority('READER')")
    public String readerPage() {

        Optional<Reader> reader = readerService.findByEmail("reader1@gmail.com");
        reader.ifPresent(readerService::delete);

        return "Hello reader";
    }

    // ==== HELP ====
    //
    //
    //
    // ==== HELP ====

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
