package edu.northeastern.ccwebapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import edu.northeastern.ccwebapp.pojo.Image;

public interface ImageRepository extends CrudRepository<Image, String>{
	List<Image> findAll();
	Optional<Image> findById(String id);
    @Transactional
    void deleteById(String id);
}
