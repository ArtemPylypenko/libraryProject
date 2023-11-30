package com.example.libraryproject.services;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.repo.BookHistoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookHistoryService {
    private final BookHistoryRepo bookHistoryRepo;
    private final BookService bookService;
    private final ReaderService readerService;

    public boolean existsByBookAndUser(Long book, Long user) {
        return bookHistoryRepo.existsBookUser(book, user).isPresent();
    }


    @Transactional
    public void addReaderBook(Reader reader, Book book) {

        reader.getBooks().add(book);
        book.getReaders().add(reader);
        updateRiveTime(book.getId(), reader.getId());

//        bookHistoryRepo.save(bookHistory);
    }

    public void updateRiveTime(Long book, Long reader) {

        bookHistoryRepo.updateGiveTime(bookService.getById(book).get(), readerService.getById(reader).get());
    }

    public void updateReturnTime(Long book, Long reader) {

        bookHistoryRepo.updateReturnTime(bookService.getById(book).get(), readerService.getById(reader).get());
    }
    public Double getAVGBookRating(Book book){
        return bookHistoryRepo.getAvgRating(book);
    }

    @Transactional
    public void deleteUserBooksByBook(Book book) {
        bookHistoryRepo.deleteByBook(book);
    }

    @Transactional
    public void deleteUserBooksByUser(Reader reader) {
        bookHistoryRepo.deleteByReader(reader);
    }

}
