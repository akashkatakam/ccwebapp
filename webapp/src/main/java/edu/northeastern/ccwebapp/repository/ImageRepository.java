package edu.northeastern.ccwebapp.repository;

import org.springframework.data.repository.CrudRepository;

import edu.northeastern.ccwebapp.pojo.Image;

public interface ImageRepository extends CrudRepository<Image, Long>{

}
