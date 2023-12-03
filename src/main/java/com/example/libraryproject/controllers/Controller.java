package com.example.libraryproject.controllers;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.Librarian;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.entity.Role;
import com.example.libraryproject.services.*;
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

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@EnableWebMvc
@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {
    private final UserService userService;
    private final ReaderService readerService;
    private final LibrarianService librarianService;
    private final BookService bookService;
    private final BookReaderService bookReaderService;
    private final HistoryService historyService;
    private final PasswordEncoder passwordEncoder;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @GetMapping("/afterLogin")
    public RedirectView afterLogin() {
        historyService.updateRating();
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
        if (Objects.equals(email, "") || Objects.equals(password, "")) {
            attributes.addFlashAttribute(ERROR, "U should avoid empty fields!");
            return new RedirectView("/admin");
        }
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
            if (email.equals(librarianService.getById(id).get().getEmail())) {
                librarianService.updateById(email, password, id);
                attributes.addFlashAttribute(SUCCESS, "Edited successfully");
            } else {
                attributes.addFlashAttribute(ERROR, "Such email already exist!");
            }
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
        model.addAttribute("booksTop", bookService.getAll().stream()
                .sorted(Comparator.comparingDouble(Book::getRating).reversed())
                .limit(5)
                .collect(Collectors.toList()));

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
                                @RequestParam("publication") String publication,
                                RedirectAttributes attributes) {
        if (Objects.equals(name, "") || Objects.equals(authors, "") || Objects.equals(given_by, "") || Objects.equals(isbn, "")) {
            attributes.addFlashAttribute(ERROR, "U should avoid empty fields!");
            return new RedirectView("/librarian");
        }
        Book book = new Book();

        book.setName(name);
        book.setAuthors(authors);
        book.setGivenBy(given_by);
        book.setIsbn(isbn);
        book.setPublication(Integer.valueOf(publication));
        book.setRating(3D);
        book.setAvailable(true);

        bookService.save(book);
        return new RedirectView("/librarian");
    }


    @GetMapping("/book/edit/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String editBook(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.getById(id).get());
        return "librarian/edit_book";
    }

    @PostMapping("/book/edit/{id}")
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

    @PostMapping("/book/delete/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView deleteBook(@PathVariable("id") Long id, RedirectAttributes attributes) {
        attributes.addFlashAttribute(SUCCESS, "book deleted!");
        bookService.deleteById(id);
        return new RedirectView("/librarian");
    }


    @GetMapping("/reader/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String getAddReaderPage() {
        return "librarian/new_reader";
    }

    @PostMapping("/reader/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView addReader(@RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam("name") String name,
                                  @RequestParam("surname") String surname,
                                  @RequestParam("phone") String phone,
                                  @RequestParam("place_to_live") String placeToLive,
                                  RedirectAttributes attributes) {
        if (email.equals("") || password.equals("") || name.equals("") || surname.equals("") || phone.equals("") || placeToLive.equals("")) {
            attributes.addFlashAttribute(ERROR, "Avoid empty fields");
            return new RedirectView("/librarian");
        }
        if (readerService.existByEmail(email)) {
            attributes.addFlashAttribute(ERROR, "Reader with such email exist!");
            return new RedirectView("/librarian");
        }

        Reader reader = new Reader();
        reader.setRole(Role.READER);
        reader.setEmail(email);
        reader.setPassword(password);
        reader.setName(name);
        reader.setSurname(surname);
        reader.setPhone(phone);
        reader.setPlaceToLive(placeToLive);
        readerService.save(reader);
        attributes.addFlashAttribute(SUCCESS, "Edited successfully");
        return new RedirectView("/librarian");
    }


    @GetMapping("/reader/edit/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String getEditReader(@PathVariable("id") Long id, Model model) {
        model.addAttribute("reader", readerService.getById(id).get());
        return "librarian/edit_reader";
    }

    @PostMapping("/reader/edit/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView editReader(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   @RequestParam("name") String name,
                                   @RequestParam("surname") String surname,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("place_to_live") String placeToLive,
                                   @PathVariable("id") Long id,
                                   RedirectAttributes attributes) {

        if (email.equals("") || password.equals("") || name.equals("") || surname.equals("") || phone.equals("") || placeToLive.equals("")) {
            attributes.addFlashAttribute(ERROR, "Avoid empty fields");
            return new RedirectView("/librarian");
        }
        if (readerService.existByEmail(email) && !readerService.getByEmail(email).get().getPassword().equals(password)) {
            attributes.addFlashAttribute(ERROR, "Reader with such email exist!");
            return new RedirectView("/librarian");
        }
        readerService.update(email, password, name, surname, phone, placeToLive, id);
        attributes.addFlashAttribute(SUCCESS, "Edited successfully");

        return new RedirectView("/librarian");
    }


    @PostMapping("/reader/delete/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public RedirectView deleteReader(@PathVariable("id") Long id, RedirectAttributes attributes) {
        readerService.deleteById(id);
        attributes.addFlashAttribute(SUCCESS, "Delete successfully");
        return new RedirectView("/librarian");
    }


    // ==== READER ====
    //
    //
    //
    // ==== READER ====

    @GetMapping("/reader")
    @PreAuthorize("hasAuthority('READER')")
    public String readerPage(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "reader/book_list_reader";
    }

    @GetMapping("/bookInfo/{id}")
    @PreAuthorize("hasAuthority('READER')")
    public String bookInfo(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getById(id).get();
        model.addAttribute("book", book);
        return "reader/book_info_reader";
    }

    @PostMapping("/bookTake/{id}")
    @PreAuthorize("hasAuthority('READER')")
    public RedirectView takeBook(@PathVariable("id") Long id, RedirectAttributes attributes) {
        Long loggedReaderId = getLoggedReaderId();
        if (bookService.getById(id).get().isAvailable()) {
            if (!bookReaderService.existsByBookAndUser(id, loggedReaderId)) {
                attributes.addFlashAttribute(SUCCESS, "Book added to your books");
                bookReaderService.addReaderBook(readerService.getById(loggedReaderId).get(), bookService.getById(id).get());
                bookService.updateAvailable(false, id);
            } else {
                attributes.addFlashAttribute(ERROR, "Reader has such book!");
            }
        } else {
            attributes.addFlashAttribute(ERROR, "Book is not available!");
        }
        return new RedirectView("/reader");
    }

    @GetMapping("/readerBooks")
    @PreAuthorize("hasAuthority('READER')")
    public String myBooks(Model model) {
        model.addAttribute("books", readerService.getById(getLoggedReaderId()).get().getBooks().stream().filter(book ->
                !book.isAvailable()));
        return "reader/my_book_reader";
    }

    @PostMapping("/bookReturn/{id}")
    @PreAuthorize("hasAuthority('READER')")
    public RedirectView myBooks(@PathVariable("id") Long id, @RequestParam("rating") Double rating) {
        Reader reader = readerService.getById(getLoggedReaderId()).get();
        Book book = bookService.getById(id).get();
        bookService.updateAvailable(true, id);
        //bookService.updateRating(rating, id);

        reader.removeBook(book);
        book.removeReader(reader);
        bookReaderService.deteleReaderBook(reader, book);
        historyService.updateRating(rating, reader.getId(), book.getId());

        return new RedirectView("/reader");
    }


    // ==== HELP ====
    //
    //
    //
    // ==== HELP ====

    private UserDetails getLoggedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal());
        }
        return null;
    }

    private Long getLoggedReaderId() {
        return readerService.getByEmail(getLoggedUserDetails().getUsername()).get().getId();
    }


    private String getLoggedUserRole() {
        return getLoggedUserDetails().getAuthorities().toArray()[0].toString();
    }
}
