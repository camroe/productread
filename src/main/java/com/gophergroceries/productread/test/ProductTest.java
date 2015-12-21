package com.gophergroceries.productread.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gophergroceries.productread.entities.ProductEntity;
import com.gophergroceries.productread.repositories.ProductsRepository;

@Component
public class ProductTest {
	private static Logger logger = LoggerFactory.getLogger(ProductTest.class);

	@Autowired(required = true)
	private ProductsRepository productsRepository;

	
	public ProductTest() {
	}

	public void printProducts() {

		logger.debug("Product Repository is {}", productsRepository == null ? "NULL" : "NOT NULL");
		List<ProductEntity> peList = productsRepository.findAllByOrderByNameAsc();
		for (ProductEntity pe : peList) {
			System.out.println(pe.toString());
		}
	}
}
