package edu.northeastern.ccwebapp.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import edu.northeastern.ccwebapp.pojo.Book;

public interface BookRepository extends CrudRepository<Book , Long>{
    Book findById(String id);
    List<Book> listAllBooks();
}