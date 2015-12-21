package com.gophergroceries.productread.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gophergroceries.productread.entities.CategoryEntity;


public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
	List<CategoryEntity> findAll();
	CategoryEntity findOneByName(String string);
	List<CategoryEntity> findAllByOrderByName();
}
