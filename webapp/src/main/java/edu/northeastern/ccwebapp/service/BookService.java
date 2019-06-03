package edu.northeastern.ccwebapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.repository.BookRepository;
import javax.servlet.http.HttpServletRequest;

@Service
public class BookService {

	private final UserService userService;
	private BookRepository bookRepository;

    public BookService(UserService userService) {
        this.userService = userService;
    }

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
			bookDetails.setUuid(uuid.toString());
			bookDetails.setIsbn(book.getIsbn());
			this.save(bookDetails);
			return new ResponseEntity(bookDetails, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity getBooks(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		List<Book> bookDetails= new ArrayList<>();
		ResponseEntity re =  userService.checkUserStatus(header);
		HttpStatus status = re.getStatusCode();
		if(status.equals(HttpStatus.OK)) {
			bookDetails= bookRepository.listAllBooks();
			return new ResponseEntity(bookDetails,HttpStatus.OK);
		}
		else
			return new ResponseEntity("Not authorized to access book details",HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity getBook(String bookId) {
			Book book = this.getBookById(bookId);
			if(book == null){
				return new ResponseEntity(new String("Book with id "+bookId+" not found"),HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(book, HttpStatus.OK);
	}

	public ResponseEntity updateBook(Book book,String id){
        Book currentBook = this.getBookById(id);
        if (currentBook == null) {
            return new ResponseEntity(new String("Unable to update Book, with id " + id + " not found."), HttpStatus.NO_CONTENT);
        }
        currentBook.setTitle(book.getTitle());
        currentBook.setAuthor(book.getAuthor());
        currentBook.setIsbn(book.getIsbn());
        currentBook.setQuantity(book.getQuantity());
        this.save(currentBook);
        return new ResponseEntity(currentBook, HttpStatus.OK);
    }

    private Book getBookById(String id){
        return bookRepository.findById(id);
    }
    private Book save(Book book){
	     bookRepository.save(book);
	     return book;
    }
	}
