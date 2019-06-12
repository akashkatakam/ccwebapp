package edu.northeastern.ccwebapp.repository;

import edu.northeastern.ccwebapp.pojo.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Long> {
	List<Image> findAll();

	Image findById(String imageId);

	Image deleteById(String imageId);
}
