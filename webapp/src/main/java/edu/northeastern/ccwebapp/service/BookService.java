package edu.northeastern.ccwebapp.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.repository.BookRepository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public class BookService {

	@Autowired
	private UserService userService;
	private BookRepository bookRepository;
	
	public ResponseEntity<Book> addBookDetails(Book book, ResponseEntity responseEntity) {
		
		if(responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		} 
		else if (responseEntity.getStatusCode().equals(HttpStatus.OK)){
			Book bookDetails = new Book();
			bookDetails.setAuthor(book.getAuthor());
			bookDetails.setQuantity(book.getQuantity());
			bookDetails.setTitle(book.getTitle());
			UUID uuid = UUID.randomUUID();
			bookDetails.setUuid(uuid.toString());
			bookDetails.setIsbn(book.getIsbn());
			return new ResponseEntity(bookDetails, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	public Iterable<Book> getBooks() {
		return bookRepository.findAll();
	}

	public ResponseEntity getBook(@PathVariable String bookId, HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		ResponseEntity re =  userService.checkUserStatus(header);
		HttpStatus status = re.getStatusCode();
		if(status.equals(HttpStatus.OK)){
			Book book = bookRepository.getBookByUuid(bookId);
			if(book == null){
				return new ResponseEntity(new String("Book with id "+bookId+" not found"),HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(book, HttpStatus.OK);
		}

		return re;

	}
}