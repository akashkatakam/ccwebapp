package edu.northeastern.ccwebapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.northeastern.ccwebapp.pojo.Image;

public interface ImageRepository extends CrudRepository<Image, String>{
	List<Image> findAll();
}
