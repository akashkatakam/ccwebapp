package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.Util.ResponseMessage;
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

    private UserService userService;
    private BookRepository bookRepository;

    public BookService(UserService userService, BookRepository bookRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;

    }

    public ResponseEntity<?> addBookDetails(Book book) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book bookDetails = new Book();
        if (book.getTitle() == null && book.getAuthor() == null && book.getIsbn() == null) {
            bookDetails.setAuthor(book.getAuthor());
            bookDetails.setQuantity(book.getQuantity());
            bookDetails.setTitle(book.getTitle());
            UUID uuid = UUID.randomUUID();
            bookDetails.setId(uuid.toString());
            bookDetails.setIsbn(book.getIsbn());
            this.save(bookDetails);
            return new ResponseEntity<>(bookDetails, HttpStatus.CREATED);
        } else {
            responseMessage.setMessage("Invalid Title/ Author or Invalid JSON");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getBooks() {
        List<Book> bookDetails;
        bookDetails = bookRepository.findAll();
        return new ResponseEntity<>(bookDetails, HttpStatus.OK);
    }

    public ResponseEntity<?> getBook(String bookId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book book = this.getBookById(bookId);
        if (book == null) {
            responseMessage.setMessage("Book with id " + bookId + " not found");
            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    public ResponseEntity<?> updateBook(Book book) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book currentBook = this.getBookById(book.getId());
        if (currentBook != null) {
            if (currentBook.getAuthor() == null && currentBook.getTitle() == null) {
                responseMessage.setMessage("Invalid tittle/Author or an invalid Json format");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            currentBook.setTitle(book.getTitle());
            currentBook.setAuthor(book.getAuthor());
            currentBook.setIsbn(book.getIsbn());
            currentBook.setQuantity(book.getQuantity());
            this.save(currentBook);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            responseMessage.setMessage("Book with id " + book.getId() + " not found");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    private Book getBookById(String id) {
        return bookRepository.findById(id);
    }

    private void save(Book book) {
        bookRepository.save(book);
    }

    public ResponseEntity<?> deleteBook(String id) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book currentBook = this.getBookById(id);
        if (currentBook != null) {
            this.deleteBookById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            responseMessage.setMessage("Book with id " + id + " not found");
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }

    private void deleteBookById(String id) {
        bookRepository.deleteById(id);
    }

    public ResponseEntity resultOfUserStatus(HttpServletRequest request) {
        String headerResp = request.getHeader("Authorization");
        return userService.checkUserStatus(headerResp);
    }
}

