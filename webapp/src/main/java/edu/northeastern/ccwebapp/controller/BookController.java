package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.service.BookService;

public class BookController {

	@Autowired
	private BookService bookService;
	private UserController userController;
	

    @PostMapping(value = "/book", produces = "application/json", consumes = "application/json" )
    public ResponseEntity createBook(@RequestBody Book book, HttpServletRequest request, HttpServletResponse response) {
    	
    	ResponseEntity responseEntity = userController.basicAuth(request, response);
    	return bookService.addBookDetails(book, responseEntity);	
    }
}
