package edu.northeastern.ccwebapp.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.ccwebapp.Util.ResponseMessage;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.pojo.Image;
import edu.northeastern.ccwebapp.repository.ImageRepository;

@Service
public class ImageService {

	private ImageRepository imageRepository;
	private BookService bookService;
	
	public ImageService(ImageRepository imageRepository, BookService bookService) {
		this.imageRepository = imageRepository;
		this.bookService = bookService;
	}

	public ResponseEntity<?> createImageAttachment(String idBook, MultipartFile file) {
		Image image = new Image();
		UUID id = UUID.randomUUID();
		image.setId(id.toString());
		// To-do: instead of name need to add local path of file
		String path = file.getOriginalFilename();
		image.setUrl(path);
		imageRepository.save(image);
		Book book = bookService.getBookById(idBook);
		if (book != null) {
			updateBookByAddingGivenImage(image, book);
			return new ResponseEntity<>(image, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> getBookDetails(String bookId, String imageId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Book book = bookService.getBookById(bookId);
		if(book != null && book.getImage().getId().equals(imageId)) {
			return new ResponseEntity<>(imageRepository.findById(imageId), HttpStatus.OK);
		}
		responseMessage.setMessage("Either book not found or given image does not exists in book.");
		return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> deleteBookDetails(String bookId, String imageId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Book book = bookService.getBookById(bookId);
		if(book != null && book.getImage().getId().equals(imageId)) {
			imageRepository.deleteById(imageId);
			updateBookByAddingGivenImage(null, book);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		responseMessage.setMessage("Either book not found or given image does not exists in book.");
		return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
	}

	public void updateBookByAddingGivenImage(Image image, Book book) {
		book.setImage(image);
		bookService.save(book);
	}
}