package edu.northeastern.ccwebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.service.BookService;

public class BookController {

	@Autowired
	private BookService bookService;
	

    @PostMapping(value = "/book", produces = "applicatio/json", consumes = "application/json" )
    public ResponseEntity createBook(@RequestBody Book book) {
     return new ResponseEntity(null);	
    }
    
    @GetMapping(value="/book", produces = "application/json" , consumes ="application/json")
    public Iterable<Book> returnBookDetails(){
    	return bookService.getBooks();
    }
}
