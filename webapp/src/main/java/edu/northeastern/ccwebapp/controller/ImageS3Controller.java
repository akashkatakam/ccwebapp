package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import edu.northeastern.ccwebapp.service.ImageS3Service;
import edu.northeastern.ccwebapp.service.UserService;

@Profile("aws")
@Controller
public class ImageS3Controller {

	private UserService userService;
	private ImageS3Service imageService;
	
	public ImageS3Controller(UserService userService, ImageS3Service imageService) {
		this.userService = userService;
		this.imageService = imageService;
	}
	
	@PostMapping(value = "/book/{idBook}/image", produces = "application/json")
	public ResponseEntity<?> addImageToBook(@PathVariable String idBook, @RequestParam("file") MultipartFile file,
			HttpServletRequest request){
		ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        	return imageService.createCoverPage(idBook, file);
        }
        return responseEntity;
	}
	
	
	/*
	 * @GetMapping(value="/book/{idBook}/image/{idImage}",
	 * produces="application/json") public ResponseEntity<?>
	 * fetchBookImageDetails(@PathVariable String idBook, @PathVariable String
	 * idImage, HttpServletRequest request) { ResponseEntity<?> responseEntity =
	 * userService.resultOfUserStatus(request); if
	 * (responseEntity.getStatusCode().equals(HttpStatus.OK)) { return
	 * imageService.getCoverPage(idBook, idImage); } return responseEntity; }
	 */
	  
	/*
	 * @PutMapping(value = "/book/{idBook}/image/{idImage}", produces =
	 * "application/json") public ResponseEntity<?>
	 * modifyBookImageDetails(@PathVariable String idBook, @PathVariable String
	 * idImage , @RequestParam("file") MultipartFile file, HttpServletRequest
	 * request) { ResponseEntity<?> responseEntity =
	 * userService.resultOfUserStatus(request); if
	 * (responseEntity.getStatusCode().equals(HttpStatus.OK)) return
	 * imageService.updateCoverPage(idBook, idImage, file); return responseEntity; }
	 * 
	 * @DeleteMapping(value = "/book/{idBook}/image/{idImage}") public
	 * ResponseEntity<?> removeBookImageDetails(@PathVariable String
	 * idBook, @PathVariable String idImage, HttpServletRequest request) {
	 * ResponseEntity<?> responseEntity = userService.resultOfUserStatus(request);
	 * if (responseEntity.getStatusCode().equals(HttpStatus.OK)) return
	 * imageService.deleteCoverPage(idBook, idImage); return responseEntity; }
	 */
	 
}
