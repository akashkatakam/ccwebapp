package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.ccwebapp.service.ImageService;
import edu.northeastern.ccwebapp.service.UserService;

@RestController
public class ImageController {

	private UserService userService;
	private ImageService imageService;
	
	public ImageController(UserService userService, ImageService imageService) {
		this.userService = userService;
		this.imageService = imageService;
	}
	
	@PostMapping(value = "/book/{idBook}/image", produces = "application/json")
	public ResponseEntity<?> addImageToBook(@PathVariable String idBook, @RequestParam("file") MultipartFile file,
			HttpServletRequest request){
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return imageService.createImageAttachment(idBook, file);
		}
		return responseEntity;
	}
	
	@GetMapping(value="/book/{idBook}/image/{idImage}", produces="application/json")
	public ResponseEntity<?> fetchBookImageDetails(@PathVariable String idBook, @PathVariable String idImage, HttpServletRequest request) {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return imageService.getBookDetails(idBook, idImage);
		}
		return responseEntity;
	}
	
	@PutMapping(value="/book/{id}/image/{idImage}", produces="application/json")
	public ResponseEntity<?> modifyBookImageDetails(@PathVariable String idBook, @PathVariable String idImage, HttpServletRequest request) {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		return responseEntity;
	}
	
	@DeleteMapping(value="/book/{idBook}/image/{idImage}", produces="application/json")
	public ResponseEntity<?> removeBookImageDetails(@PathVariable String idBook, @PathVariable String idImage, HttpServletRequest request) {
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
		if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return imageService.deleteBookDetails(idBook, idImage);
		}
		return responseEntity;
	}
}
