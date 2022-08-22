package com.julioleal.Ds.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.julioleal.Ds.catalog.entities.Product;
import com.julioleal.Ds.catalog.tests.EntitiesFactory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;

	
	long invalidId;
	long validId;
	long totalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		invalidId = 329L;
		validId = 1L;
		totalProducts = 25;
	}
	
	@Test
	public void saveShouldPersistNewIdWhenIdIsNull() {
		Product product = EntitiesFactory.createdNewProduct();
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(26L, product.getId());
		
	}
	
	@Test
	public void findByIdShouldThrowsExceptionWhenIdIsNotValid() {

			Optional<Product> product = repository.findById(invalidId);
			Assertions.assertTrue(product.isEmpty());

		
	}
	
	@Test
	public void findByIdShouldFindProductWhenIdIsValid() {
		Optional<Product> product = repository.findById(validId);
		Assertions.assertNotNull(product);
		Assertions.assertNotNull(product.isPresent());
		
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(validId);
		Optional<Product> result = repository.findById(validId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowExcepitionWhenInvalidId() {
		Assertions.assertThrows(EmptyResultDataAccessException.class,()  -> {
			repository.deleteById(invalidId);
			
		});
		
		
		
		
		
		
	}
}
