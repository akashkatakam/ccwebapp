package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.Util.ResponseMessage;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.pojo.Image;
import edu.northeastern.ccwebapp.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private static final String imagePath = System.getProperty("user.home") + "/Pictures/";

    private ImageRepository imageRepository;
    private BookService bookService;
	
	public ImageService(ImageRepository imageRepository, BookService bookService) {
		this.imageRepository = imageRepository;
		this.bookService = bookService;
	}

    public ResponseEntity<?> createCoverPage(String bookId, MultipartFile file) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book book = bookService.getBookById(bookId);
		if (book != null) {
            try {
                Image image = new Image();
                String pathURL = imagePath + Instant.now() + "___" + file.getOriginalFilename();
                UUID id = UUID.randomUUID();
                image.setId(id.toString());
                image.setUrl(pathURL);
                File directory = new File(pathURL);
                file.transferTo(directory);
                updateBookByAddingGivenImage(image, book);
                return new ResponseEntity<>(image, HttpStatus.OK);
            } catch (IOException e) {
                responseMessage.setMessage("Error in path not found" + e);
                e.printStackTrace();
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
        }
        responseMessage.setMessage("Book with id " + bookId + " not found");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getCoverPage(String bookId, String imageId) {
		ResponseMessage responseMessage = new ResponseMessage();
        Book book = bookService.getBookById(bookId);
        if (book != null && book.getImage().getId().equals(imageId)) {
            return new ResponseEntity<>(imageRepository.findById(imageId), HttpStatus.OK);
        }
        responseMessage.setMessage("Either book not found or given image does not exists in book.");
        return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> deleteCoverPage(String bookId, String imageId) {
		ResponseMessage responseMessage = new ResponseMessage();
        Book book = bookService.getBookById(bookId);
        if (book != null && book.getImage().getId().equals(imageId)) {
            updateBookByAddingGivenImage(null, book);
            imageRepository.deleteById(imageId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        responseMessage.setMessage("Either book not found or given image is not the cover page of the book.");
        return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity updateCoverPage(String bookId, String imageId, MultipartFile file) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book currentBook = bookService.getBookById(bookId);
        Optional<Image> currentImage = imageRepository.findById(imageId);

        if (currentBook != null) {
            if (currentImage.isPresent()) {
                if (currentBook.getImage().getId().equals(imageId)) {
                    try {
                        String pathURL = imagePath + Instant.now() + "___" + file.getOriginalFilename();
                        File directory = new File(pathURL);
                        currentBook.getImage().setUrl(pathURL);
                        imageRepository.save(currentBook.getImage());
                        file.transferTo(directory);
                        return new ResponseEntity(HttpStatus.NO_CONTENT);
                    } catch (IOException e) {
                        responseMessage.setMessage("Error in path not found" + e);
                        e.printStackTrace();
                        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    responseMessage.setMessage("Image with id " + imageId + " not found");
                    return new ResponseEntity(responseMessage, HttpStatus.NOT_FOUND);
                }
            }
        }
        responseMessage.setMessage("Book with id " + bookId + " not found");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    private void updateBookByAddingGivenImage(Image image, Book book) {
        imageRepository.save(image);
        book.setImage(image);
        bookService.save(book);
	}
}