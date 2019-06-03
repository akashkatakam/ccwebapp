package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public class BookService {

	@Autowired
	private UserService userService;

	private BookRepository bookRepository;
	public void addBookDetails(Book book) {

	}

	public Iterable<Book> getBooks() {
		return bookRepository.findAll();
	}

	public ResponseEntity getBook(@PathVariable String bookId, HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		ResponseEntity re =  userService.checkUserStatus(header);
		HttpStatus status = re.getStatusCode();
		if(status.equals(HttpStatus.OK)){
			Book book = bookRepository.getBookById(bookId);
			if(book == null){
				return new ResponseEntity(new String("Book with id "+bookId+" not found"),HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(book, HttpStatus.OK);
		}

		return re;
	}
}