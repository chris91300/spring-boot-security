package com.simplon.tests.services;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import com.simplon.tests.entities.BookEntity;
import com.simplon.tests.repositories.BookRepository;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepositoryInjected) {
        this.bookRepository = bookRepositoryInjected;
    }

    public List<BookEntity> getAll() {
        return this.bookRepository.findAll();
    }

    public BookEntity getById(String id) throws Exception {
        Optional<BookEntity> bookFound = this.bookRepository.findById(id);

        if (bookFound.isEmpty()) {
            throw new BadRequestException("id invalid.");
        }

        return bookFound.get();
    }

    public BookEntity save(BookEntity book) throws Exception {
        if (book.getTitle() == null) {
            throw new BadRequestException("title est obligatoire.");
        }

        if (book.getAuthor() == null) {
            throw new BadRequestException("author est obligatoire.");
        }

        if (book.getCategory() == null) {
            throw new BadRequestException("category est obligatoire.");
        }

        if (book.getYearOfPublication() == null) {
            throw new BadRequestException("yearOfPublication est obligatoire.");
        }

        if (book.getCopiesAvailable() == null) {
            throw new BadRequestException("copiesAvailable est obligatoire.");
        }

        return this.bookRepository.save(book);
    }

    public BookEntity update(String id, BookEntity book) throws Exception {
        Optional<BookEntity> bookFound = this.bookRepository.findById(id);

        if (bookFound.isEmpty()) {
            throw new BadRequestException("id invalid.");
        }

        BookEntity bookToUpdate = bookFound.get();

        if (book.getTitle() != null) {
            bookToUpdate.setTitle(book.getTitle());
        }

        if (book.getAuthor() != null) {
            bookToUpdate.setAuthor(book.getAuthor());
        }

        if (book.getCategory() != null) {
            bookToUpdate.setCategory(book.getCategory());
        }

        if (book.getYearOfPublication() != null) {
            bookToUpdate.setYearOfPublication(book.getYearOfPublication());
        }

        if (book.getCopiesAvailable() != null) {
            bookToUpdate.setCopiesAvailable(book.getCopiesAvailable());
        }

        return this.bookRepository.save(bookToUpdate);
    }

    public BookEntity delete(String id) throws Exception {
        Optional<BookEntity> bookFound = this.bookRepository.findById(id);

        if (bookFound.isEmpty()) {
            throw new BadRequestException("id invalid.");
        }

        BookEntity bookTodelete = bookFound.get();
        this.bookRepository.delete(bookTodelete);
        return bookTodelete;
    }
}
