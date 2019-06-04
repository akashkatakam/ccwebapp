package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

	private final UserService userService;
	private BookRepository bookRepository;

    public BookService(UserService userService,BookRepository bookRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;

    }

    public ResponseEntity<Book> addBookDetails(Book book, ResponseEntity responseEntity) {

		if(responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		else if (responseEntity.getStatusCode().equals(HttpStatus.OK)){
			Book bookDetails = new Book();
			bookDetails.setAuthor(book.getAuthor());
			bookDetails.setQuantity(book.getQuantity());
			bookDetails.setTitle(book.getTitle());
			UUID uuid = UUID.randomUUID();
			bookDetails.setUuid(uuid.toString());
			bookDetails.setIsbn(book.getIsbn());
			bookRepository.save(bookDetails);
			return new ResponseEntity<>(bookDetails, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<Object> getBooks(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		List<Book> bookDetails;
		ResponseEntity<String> re =  userService.checkUserStatus(header);
		HttpStatus status = re.getStatusCode();
		if(status.equals(HttpStatus.OK)) {
			bookDetails= bookRepository.findAll();
			return new ResponseEntity<>(bookDetails, HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("Not authorized to access book details", HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity getBook(String bookId) {
			Book book = this.getBookById(bookId);
			if(book == null){
				return new ResponseEntity<>("Book with id " + bookId + " not found",HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(book, HttpStatus.OK);
	}

	public ResponseEntity updateBook(Book book, String id){
        Book currentBook = this.getBookById(id);
        if (currentBook == null) {
            return new ResponseEntity<>("Unable to update Book, with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }
        currentBook.setTitle(book.getTitle());
        currentBook.setAuthor(book.getAuthor());
        currentBook.setIsbn(book.getIsbn());
        currentBook.setQuantity(book.getQuantity());
        this.save(currentBook);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Book getBookById(String id){
        return bookRepository.findByUuid(id);
    }

    private Book save(Book book){
	     bookRepository.save(book);
	     return book;
    }
    
    public ResponseEntity<?> deleteBook(String id) {
		Book currentBook = this.getBookById(id);
		if (currentBook == null) {
			return new ResponseEntity<>("Book with id " + id + " not found.", HttpStatus.NOT_FOUND);
		}else {
			this.deleteBookById(id);
			return new ResponseEntity<>("Deleted successfully!",HttpStatus.NO_CONTENT);
		}
	}

    private void deleteBookById(String id) {
    	bookRepository.deleteByUuid(id);
    }
}

