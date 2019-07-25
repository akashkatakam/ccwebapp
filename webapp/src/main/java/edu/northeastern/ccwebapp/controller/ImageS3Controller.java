package edu.northeastern.ccwebapp.controller;

import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.service.BookService;
import edu.northeastern.ccwebapp.service.ImageS3Service;
import edu.northeastern.ccwebapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.timgroup.statsd.StatsDClient;

import javax.servlet.http.HttpServletRequest;

@Profile("aws")
@Controller
public class ImageS3Controller {

	private UserService userService;
	private ImageS3Service imageS3Service;
	private BookService bookService;
	@Autowired
	private StatsDClient stats;

	public ImageS3Controller(UserService userService, ImageS3Service imageS3Service, BookService bookService) {
		this.userService = userService;
		this.imageS3Service = imageS3Service;
		this.bookService = bookService;
	}
	
	@PostMapping(value = "/book/{idBook}/image", produces = "application/json")
	public ResponseEntity<?> addImageToBook(@PathVariable String idBook, @RequestParam("file") MultipartFile file,
			HttpServletRequest request){
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		stats.incrementCounter("endpoint.uploadimage.http.post");
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        	return imageS3Service.createCoverPage(idBook, file);
        }
        return responseEntity;
	}
	
	  @GetMapping(value="/book/{idBook}/image/{idImage}", produces="application/json") 
	  public ResponseEntity<?> fetchBookImageDetails(@PathVariable String idBook, @PathVariable String idImage,
			  HttpServletRequest request) throws Exception {
		  ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request); 
		  stats.incrementCounter("endpoint.getbookimage.http.get");
		  if (responseEntity.getStatusCode().equals(HttpStatus.OK)) { 
			  return imageS3Service.getCoverPage(idBook, idImage); 
	      } 
		  return responseEntity; 
	  }

	@GetMapping(value = "/book", produces = "application/json")
	public ResponseEntity<?> getBooks(HttpServletRequest request) throws Exception {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		stats.incrementCounter("endpoint.getbookswithimages.http.get");
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return imageS3Service.getAllBooks();
		}
		return responseEntity;
	}

	@GetMapping(value = "/book/{idBook}", produces = "application/json")
	public ResponseEntity<?> getBookById(@PathVariable String idBook, HttpServletRequest request) throws Exception {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		stats.incrementCounter("endpoint.getBookById.http.get");
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return imageS3Service.getBookById(idBook);
		}
		return responseEntity;
	}
	 

	  @PutMapping(value = "/book/{idBook}/image/{idImage}", produces = "application/json") 
	  public ResponseEntity<?> modifyBookImageDetails(@PathVariable String idBook, @PathVariable String idImage, 
			  @RequestParam("file") MultipartFile file, HttpServletRequest request) { 
		  ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request); 
		  stats.incrementCounter("endpoint.updateimage.http.put");
		  if (responseEntity.getStatusCode().equals(HttpStatus.OK)) { 
			  return imageS3Service.updateCoverPage(idBook, idImage, file);
		  }
	      return responseEntity; 
	  }
	  
	
	  @DeleteMapping(value = "/book/{idBook}/image/{idImage}") 
	  public ResponseEntity<?> removeBookImageDetails(@PathVariable String idBook, @PathVariable String idImage,
			  HttpServletRequest request) {
		  ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		  stats.incrementCounter("endpoint.deleteimage.http.delete");
		  if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			  return imageS3Service.deleteCoverPage(idBook, idImage);
		  } 
		  return responseEntity; 
	  }

	@PutMapping(value = "/book", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> UpdateBook(@RequestBody Book book, HttpServletRequest request) {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		stats.incrementCounter("endpoint.updateBook.http.put");
		HttpStatus status = responseEntity.getStatusCode();
		if (status.equals(HttpStatus.OK)) return bookService.updateBook(book);
		else return responseEntity;
	}

	@DeleteMapping(value = "/book/{id}")
	public ResponseEntity<?> deleteBookById(@PathVariable("id") String id, HttpServletRequest request) {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		stats.incrementCounter("endpoint.deleteBookWithImage.http.delete");
		HttpStatus status = responseEntity.getStatusCode();
		if (status.equals(HttpStatus.OK)) return bookService.deleteBook(id);
		else return responseEntity;
	}

    @PostMapping(value = "/book", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> createBook(@RequestBody Book book, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
        stats.incrementCounter("endpoint.uploadbook.http.post");
        HttpStatus status = responseEntity.getStatusCode();
        if (status.equals(HttpStatus.OK)) return bookService.addBookDetails(book);
        else return responseEntity;
    }
	 
	 
}
