package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.service.BookService;
import edu.northeastern.ccwebapp.service.UserService;

@RestController
public class BookController {

	private final BookService bookService;
	private final UserService userService;

    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @PostMapping(value = "/book", produces = "application/json", consumes = "application/json" )
    public ResponseEntity<?> createBook(@RequestBody Book book, HttpServletRequest request) {
    	String headerResp = request.getHeader("Authorization");
    	ResponseEntity<?> responseEntity = userService.checkUserStatus(headerResp);
    	return bookService.addBookDetails(book, responseEntity);	
    }
    
    @GetMapping(value="/book", produces = "application/json" , consumes ="application/json")
    public ResponseEntity<?> returnBookDetails(HttpServletRequest request){
    	return bookService.getBooks(request);
    }

    @GetMapping(value = "/book/{id}",produces = "application/json")
    public ResponseEntity<?> getBookById(@PathVariable String id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        ResponseEntity responseEntity =  userService.checkUserStatus(header);
        HttpStatus status = responseEntity.getStatusCode();
        if(status.equals(HttpStatus.OK)){
            return  bookService.getBook(id);
        }else return  responseEntity;

    }

    @PutMapping(value = "/book/{id}",produces = "application/json",consumes = "application/json")
    public ResponseEntity UpdateBook(@PathVariable("id") String id, @RequestBody Book book, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        ResponseEntity responseEntity =  userService.checkUserStatus(header);
        HttpStatus status = responseEntity.getStatusCode();
        if(status.equals(HttpStatus.OK)){
            return bookService.updateBook(book,id);
        }else return responseEntity;
    }
    
    @DeleteMapping(value = "/book/{id}", consumes = "application/json" )
    public ResponseEntity<?> deleteBookById(@PathVariable("id") String id, HttpServletRequest request) {
    	String headerResp = request.getHeader("Authorization");
    	ResponseEntity<?> responseEntity = userService.checkUserStatus(headerResp);
    	return bookService.deleteBook(id, responseEntity);
    }
}
