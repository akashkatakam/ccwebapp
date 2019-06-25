package edu.northeastern.ccwebapp.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
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
public class ImageS3Service {

	static String domainName = "jalkotea";
	static String BucketName = "csye6225-su19-"+domainName+"me.csye6225.com";
	
    private static final String imagePath = "https://s3.amazonaws.com/" + BucketName + "/";


    private BookService bookService;
    private ImageService imageService;
    private S3ServiceImpl seServiceImpl;
    private ImageRepository imageRepository;

	
    public ImageS3Service(ImageRepository imageRepository, BookService bookService, 
    		ImageService imageService, S3ServiceImpl seServiceImpl) {
        this.imageRepository = imageRepository;
        this.bookService = bookService;
        this.seServiceImpl = seServiceImpl;
        this.imageService = imageService;
    }

	public ResponseEntity<?> createCoverPage(String bookId, MultipartFile file) {
		ResponseMessage responseMessage = new ResponseMessage();
        Book book = bookService.getBookById(bookId);
		if (book != null) {
            if (book.getImage() == null && imageService.checkContentType(file)) {
                try {
                    String key = Instant.now().getEpochSecond() + "_" + file.getOriginalFilename();
                    String pathURL = imagePath + URLEncoder.encode(key, "UTF-8");
                    seServiceImpl.uploadFile(key, file);
                    
                    Image image = new Image();
                    UUID id = UUID.randomUUID();
                    image.setId(id.toString());
                    image.setUrl(pathURL);
					/*
					 * File directory = new File(pathURL); file.transferTo(directory);
					 */
                    imageService.updateBookByAddingGivenImage(image, book);
                    //TO-Do : save into RDS instance
                    imageRepository.save(image);
                    return new ResponseEntity<>(image, HttpStatus.OK);
                } catch (IOException e) {
                    responseMessage.setMessage("Error in path not found" + e);
                    e.printStackTrace();
                    return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
                }
            } else {
                responseMessage.setMessage("Coverpage already added for book or Image format not supported");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
        }
        responseMessage.setMessage("Book with id " + bookId + " not found");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

}