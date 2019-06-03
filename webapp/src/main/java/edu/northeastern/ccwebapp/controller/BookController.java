package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.service.BookService;
import edu.northeastern.ccwebapp.service.UserService;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	
    @PostMapping(value = "/book", produces = "application/json", consumes = "application/json" )
    public ResponseEntity<?> createBook(@RequestBody Book book, HttpServletRequest request) {
    	String headerResp = request.getHeader("Authorization");
    	ResponseEntity<?> responseEntity = userService.checkUserStatus(headerResp);
    	return bookService.addBookDetails(book, responseEntity);	
    }
    
    @GetMapping(value="/book", produces = "application/json" , consumes ="application/json")
    public Iterable<Book> returnBookDetails(){
    	return bookService.getBooks();
    }
}
