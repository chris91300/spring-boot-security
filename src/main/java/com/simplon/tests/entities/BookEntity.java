package com.simplon.tests.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Nonnull
    @Column(unique = true, nullable = false)
    private String title;

    @Nonnull
    @Column(nullable = false)
    private String author;

    @Nonnull
    @Column(nullable = false)
    private String category;

    @Nonnull
    @Column(nullable = false)
    private String yearOfPublication;

    @Nonnull
    @Column(nullable = false)
    private String copiesAvailable;

    public BookEntity() {
    }

    public BookEntity(String title, String author, String category, String yearOfPublication, String copiesAvailable) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.yearOfPublication = yearOfPublication;
        this.copiesAvailable = copiesAvailable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public void setCopiesAvailable(String copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCategory() {
        return this.category;
    }

    public String getYearOfPublication() {
        return this.yearOfPublication;
    }

    public String getCopiesAvailable() {
        return this.copiesAvailable;
    }
}
