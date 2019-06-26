package edu.northeastern.ccwebapp.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.ccwebapp.Util.ResponseMessage;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.pojo.Image;
import edu.northeastern.ccwebapp.repository.ImageRepository;
import edu.northeastern.ccwebapp.s3PreSigned.S3GeneratePreSignedURL;

@Service
public class ImageS3Service {

	static String domainName = "kallurit";
	static String BucketName = "csye6225-su19-"+domainName+".me.csye6225.com";

    private static final String imagePath = "s3://"+BucketName+"/";

    private BookService bookService;
    private ImageService imageService;
    private S3ServiceImpl s3ServiceImpl;
    private ImageRepository imageRepository;

	
    public ImageS3Service(ImageRepository imageRepository, BookService bookService, 
    		ImageService imageService, S3ServiceImpl s3ServiceImpl) {
        this.imageRepository = imageRepository;
        this.bookService = bookService;
        this.s3ServiceImpl = s3ServiceImpl;
        this.imageService = imageService;
    }

	public ResponseEntity<?> createCoverPage(String bookId, MultipartFile file) {
		ResponseMessage responseMessage = new ResponseMessage();
        Book book = bookService.getBookById(bookId);
		if (book != null) {
            if (book.getImage() == null && imageService.checkContentType(file)) {
                    Image uploadedImage = saveFileInS3Bucket(file, book);
                    return new ResponseEntity<>(uploadedImage, HttpStatus.OK);
            } else {
                responseMessage.setMessage("Coverpage already added for book or Image format not supported");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
        }
        responseMessage.setMessage("Book with id " + bookId + " not found");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}
	
	private Image saveFileInS3Bucket(MultipartFile file, Book book) {
		try {
			String key = Instant.now().getEpochSecond() + "_" + file.getOriginalFilename();
	        String pathURL = imagePath + URLEncoder.encode(key, "UTF-8");
	        s3ServiceImpl.uploadFile(key, file);
	        Image image = new Image();
	        UUID id = UUID.randomUUID();
	        image.setId(id.toString());
	        image.setUrl(pathURL);
	        imageService.updateBookByAddingGivenImage(image, book);
	        //TO-Do : save into RDS instance
	        return imageRepository.save(image);	
		} catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}

	public ResponseEntity<?> updateCoverPage(String bookId, String imageId, MultipartFile file) {
        ResponseMessage responseMessage = new ResponseMessage();
        Book currentBook = bookService.getBookById(bookId);
        Optional<Image> currentImage = imageRepository.findById(imageId);
        if (currentBook != null) {
            if (currentImage.isPresent()) {
                if (currentBook.getImage().getId().equals(imageId)) {
                    if (imageService.checkContentType(file)) {
						String path = currentImage.get().getUrl();
						s3ServiceImpl.deleteFile(path.split("/")[3]);
						saveFileInS3Bucket(file, currentBook);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else {
                        responseMessage.setMessage("Only .jpg,.png,.jpeg formats are supported");
                        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    responseMessage.setMessage("Image with id " + imageId + " not found in mentioned book.");
                }
            } else {
            	responseMessage.setMessage("Image with id " + imageId + " not found");
            }
        } else {
        	responseMessage.setMessage("Book with id " + bookId + " not found");
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
    }
	
	
	public ResponseEntity<?> getCoverPage(String bookId, String imageId) throws Exception {
		ResponseMessage responseMessage = new ResponseMessage();
        Book book = bookService.getBookById(bookId);
        if(book != null) {
        	if(book.getImage() != null) {
        		if(book.getImage().getId().equals(imageId)) {
        			S3GeneratePreSignedURL s3Url = new S3GeneratePreSignedURL();
        			Optional<Image> mp=imageRepository.findById(imageId);
        			String image_loc=mp.get().getUrl().substring(mp.get().getUrl().lastIndexOf("/")+1);
        			String img_url=s3Url.getPreSignedURL(image_loc, BucketName);   
        			Map<String, String> urlMap =  new HashMap<>();
        			urlMap.put("url", img_url);
        			urlMap.put("id", mp.get().getId());
        		return new ResponseEntity<>(urlMap, HttpStatus.OK);	
        					
        		} else {
        			responseMessage.setMessage("Image with mentioned id does not match with book's image id..");
        		}
        	} else {
        		responseMessage.setMessage("Image with mentioned id does not exists.");
        	}
        } else {
        	responseMessage.setMessage("Book with mentioned id does not exists.");
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
    }

}