package edu.northeastern.ccwebapp.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.pojo.Image;
import edu.northeastern.ccwebapp.repository.BookRepository;
import edu.northeastern.ccwebapp.repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private BookService bookService;
	@Autowired
	private BookRepository bookRepository;
	
	public ResponseEntity<?> createImageAttachment(String idBook, MultipartFile file) {
		// TODO Auto-generated method stub
		Image image = new Image();
		UUID id = UUID.randomUUID();
		image.setId(id.toString());
		//To-do: instead of name need to add local path of file
		String path = file.getOriginalFilename();
		image.setUrl(path);
		imageRepository.save(image);
		Book book = bookService.getBookById(idBook);
		if(book != null) {
			book.setImage(image);
			bookRepository.save(book);
			return new ResponseEntity<>(image, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
