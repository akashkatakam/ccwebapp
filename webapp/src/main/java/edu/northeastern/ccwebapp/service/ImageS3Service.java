package edu.northeastern.ccwebapp.service;

import edu.northeastern.ccwebapp.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageS3Service {

    private static final String imagePath = System.getProperty("user.home") + "/Pictures/";

    private ImageRepository imageRepository;
    private BookService bookService;

    public ImageS3Service(ImageRepository imageRepository, BookService bookService) {
        this.imageRepository = imageRepository;
        this.bookService = bookService;
    }

}