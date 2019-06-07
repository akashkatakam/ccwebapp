package edu.northeastern.ccwebapp;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.repository.BookRepository;
import edu.northeastern.ccwebapp.service.BookService;
import edu.northeastern.ccwebapp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CcwebappApplicationTests {

	@Mock
	UserService userService;
	
	@Mock
	BookService bookService;
	
	@Autowired
	BookRepository bookRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		User createUser = new User();
		createUser.setUsername("monica@gmail.com");
		createUser.setPassword("rachel321");
		when(userService.findByUserName("monica@gmail.com")).thenReturn(createUser);
	}

	@Test
	public void register() {
		User receivedUser = userService.findByUserName("monica@gmail.com");
		assertEquals(receivedUser.getUsername(), "monica@gmail.com");
	}
	
	public UUID id;
	
	@Before
	public void bookSetUp() {
		MockitoAnnotations.initMocks(this);
		Book book = new Book();
		id = UUID.randomUUID();
		book.setId(id.toString());
		book.setAuthor("Tanenbaum");
		book.setIsbn("978-0132126");
		book.setTitle("Computer Networks");
		book.setQuantity(3);
		bookRepository.save(book);
		when(bookService.getBookById(id.toString())).thenReturn(book);
	}
	
	@Test
	public void checkBookDetails() {
		Book receivedBook = bookRepository.findById(id.toString());
		assertEquals(receivedBook.getAuthor(), "Tanenbaum");
		assertEquals(receivedBook.getTitle(), "Computer Networks");
		assertEquals(receivedBook.getIsbn(), "978-0132126");
		assertEquals(receivedBook.getQuantity(), 3);
	}

}
