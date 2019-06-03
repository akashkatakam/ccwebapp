package edu.northeastern.ccwebapp.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.northeastern.ccwebapp.pojo.Book;

public class BookService {

	@Autowired
	UserService userService;
	
	public ResponseEntity addBookDetails(Book book, ResponseEntity responseEntity) {
		
		if(responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		} 
		else if (responseEntity.getStatusCode().equals(HttpStatus.OK)){
			Book bookDetails = new Book();
			bookDetails.setAuthor(book.getAuthor());
			bookDetails.setQuantity(book.getQuantity());
			bookDetails.setTitle(book.getTitle());
			UUID uuid = UUID.randomUUID();
			bookDetails.setUuid(uuid);
			bookDetails.setIsbn(book.getIsbn());
			return new ResponseEntity(bookDetails, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);	
		}
	}
}
