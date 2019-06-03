package edu.northeastern.ccwebapp.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import edu.northeastern.ccwebapp.pojo.Book;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends CrudRepository<Book , Long>{
    Book findByUuid(String id);
    List<Book> findAll();
    @Transactional
    void deleteByUuid(String id);
}