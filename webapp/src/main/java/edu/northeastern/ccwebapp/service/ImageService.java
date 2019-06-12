package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.Util.ResponseMessage;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.pojo.Image;
import edu.northeastern.ccwebapp.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final BookService bookService;

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
		this.saveImage(image);
		Book book = bookService.getBookById(idBook);
		if (book != null) {
			book.setImage(image);
			bookService.save(book);
			return new ResponseEntity<>(image, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> getBookDetails(String bookId, String imageId) {
		return new ResponseEntity<>(imageRepository.findById(imageId), HttpStatus.OK);
	}

	public ResponseEntity<ResponseMessage> updateBook(String bookId, String imageId, MultipartFile file) {
		Book currentBook;
		Image currentImage;
		ResponseMessage responseMessage = new ResponseMessage();
		currentBook = bookService.getBookById(bookId);
		if (currentBook == null) {
			responseMessage.setMessage("Book with " + bookId + "Not found");
			return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
		} else {
			currentImage = this.getImageById(imageId);
			if (currentImage == null) {
				responseMessage.setMessage("Image with id" + imageId + "Not Found");
				return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
			} else {
				this.saveImage();
			}
		}
	}

	public ResponseEntity<?> deleteBookDetails(String idBook, String idImage) {
		ResponseMessage responseMessage = new ResponseMessage();
		imageRepository.deleteById(idImage);
		responseMessage.setMessage("Image deleted successfully");
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
	}

	private Image getImageById(String imageId) {
		return imageRepository.findById(imageId);
	}

	private void saveImage(Image image) {
		Image newImage = new Image();
		newImage.setId(image.getId());
		newImage.setUrl(image.getUrl());
		imageRepository.save(image);
	}
}