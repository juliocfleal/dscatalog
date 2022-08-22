package com.julioleal.Ds.catalog.services;


import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.julioleal.Ds.catalog.dto.ProductDTO;
import com.julioleal.Ds.catalog.repositories.ProductRepository;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;



@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductRepository repository;

	private Long validId;
	private Long invalidId;
	private Long productsAmount;

	@BeforeEach
	void setUp() throws Exception {
		validId = 1L;
		invalidId = 1000L;
		productsAmount = 25L;
	}

	@Test
	public void deleteShouldDeleteProductWhenIdIsValid() {
		service.delete(validId);
		Assertions.assertEquals(productsAmount - 1, repository.count());
	}

	@Test
	public void deleteShouldThrowsExceptionWhenIdIsInvalid() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(invalidId);
		});
	}

	@Test
	public void findAllPagedShouldReturnPageWhenPageExists() {
		Pageable pegeable = PageRequest.of(0,10);
		Page<ProductDTO> page = service.findAllPaged(pegeable);
		Assertions.assertTrue(!page.isEmpty());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(25, page.getTotalElements());
		Assertions.assertEquals(3, page.getTotalPages());
		Assertions.assertEquals(0, page.getNumber());
	}
	
	@Test
	public void findAllPagedSrotShouldReturnPagedSorted() {
		Pageable peageble = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> page = service.findAllPaged(peageble);
		Assertions.assertTrue(!page.isEmpty());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(25, page.getTotalElements());
		Assertions.assertEquals(3, page.getTotalPages());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getName());
	}
	
	@Test
	public void findAllPagedShouldreturnEmptyWhenPageDoesNotExist() {
		Pageable pegeable = PageRequest.of(10,10);
		Page<ProductDTO> page = service.findAllPaged(pegeable);
		Assertions.assertTrue(page.isEmpty());
	}

}
