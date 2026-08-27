package com.simplon.tests.configurations;

import org.springframework.stereotype.Component;

import com.simplon.tests.entities.BookEntity;
import com.simplon.tests.entities.RoleEntity;
import com.simplon.tests.repositories.RoleRepository;
import com.simplon.tests.services.BookService;

import java.util.List;

import org.springframework.boot.CommandLineRunner;

@Component
public class DataInitializer implements CommandLineRunner {

    private BookService bookService;
    private final RoleRepository roleRepository;

    public DataInitializer(BookService bookserviceInjected, RoleRepository roleRepositoryInjected) {
        this.roleRepository = roleRepositoryInjected;
        this.bookService = bookserviceInjected;
    }

    @Override
    public void run(String... args) throws Exception {
        List<BookEntity> books = this.bookService.getAll();
        if (books.isEmpty()) {
            BookEntity book1 = new BookEntity(
                    "Tintin sur la lune",
                    "RG",
                    "BD",
                    "1970",
                    "100");

            BookEntity book2 = new BookEntity(
                    "Ouioui à la plage",
                    "NonNon",
                    "BD",
                    "2000",
                    "10");

            this.bookService.save(book1);
            this.bookService.save(book2);
        }

        RoleEntity roleUser = new RoleEntity();
        roleUser.setAuthority("ROLE_USER");
        roleRepository.save(roleUser);

        RoleEntity roleAdmin = new RoleEntity();
        roleAdmin.setAuthority("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
    }
}
