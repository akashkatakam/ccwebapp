package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.repository.BookRepository;

public class BookService {
	
	private BookRepository bookRepository;
	public void addBookDetails(Book book) {

	}

	public Iterable<Book> getBooks() {
		return bookRepository.findAll();
	}
}