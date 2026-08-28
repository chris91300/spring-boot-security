package com.simplon.tests.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.tests.entities.BookEntity;
import com.simplon.tests.services.BookService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class BookController {

    private BookService bookservice;

    public BookController(BookService bookServiceInjected) {
        this.bookservice = bookServiceInjected;
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/books")
    public List<BookEntity> getBooks() {
        return this.bookservice.getAll();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/books/{bookId}")
    public BookEntity getBook(@PathVariable String bookId) throws Exception {
        return this.bookservice.getById(bookId);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookEntity createBook(@RequestBody BookEntity book) throws Exception {
        return this.bookservice.save(book);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("books/{id}")
    public BookEntity updateBook(@PathVariable String id, @RequestBody BookEntity book) throws Exception {
        return this.bookservice.update(id, book);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("books/{id}")
    public BookEntity deleteBook(@PathVariable String id) throws Exception {
        return this.bookservice.delete(id);
    }

}
