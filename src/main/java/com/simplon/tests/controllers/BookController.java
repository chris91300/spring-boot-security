package com.simplon.tests.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.tests.entities.BookEntity;
import com.simplon.tests.services.BookService;

import java.util.List;

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

    @GetMapping("/books")
    public List<BookEntity> getBooks() {
        return this.bookservice.getAll();
    }

    @GetMapping("/books/{bookId}")
    public BookEntity getBook(@PathVariable String bookId) throws Exception {
        return this.bookservice.getById(bookId);
    }

    @PostMapping("/books")
    public BookEntity createBook(@RequestBody BookEntity book) throws Exception {
        return this.bookservice.save(book);
    }

    @PutMapping("books/{id}")
    public BookEntity updateBook(@PathVariable String id, @RequestBody BookEntity book) throws Exception {
        return this.bookservice.update(id, book);
    }

    @DeleteMapping("books/{id}")
    public BookEntity deleteBook(@PathVariable String id) throws Exception {
        return this.bookservice.delete(id);
    }

}
