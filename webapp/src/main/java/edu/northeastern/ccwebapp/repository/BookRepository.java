package edu.northeastern.ccwebapp.repository;

import org.springframework.data.repository.CrudRepository;

import edu.northeastern.ccwebapp.pojo.Book;

public interface BookRepository extends CrudRepository<Book , Long>{
    Book getBookById(String id);
    Iterable<Book> listAllBooks();
}