package com.julioleal.Ds.catalog.services;

import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.julioleal.Ds.catalog.dto.ProductDTO;
import com.julioleal.Ds.catalog.entities.Category;
import com.julioleal.Ds.catalog.entities.Product;
import com.julioleal.Ds.catalog.repositories.CategoryRepository;
import com.julioleal.Ds.catalog.repositories.ProductRepository;
import com.julioleal.Ds.catalog.services.exceptions.DataBaseException;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;
import com.julioleal.Ds.catalog.tests.EntitiesFactory;

@WebMvcTest(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository catrepo;

	private Long validId;
	private Long invalidId;
	private Long dependId;
	private Product product;
	private ProductDTO productDTO;
	private PageImpl<Product> page;
	private Category cateogry;

	@BeforeEach
	void setUp() throws Exception {
		validId = 1L;
		invalidId = 300L;
		dependId = 5L;
		product = EntitiesFactory.createdProduct();
		page = new PageImpl<>(List.of(product));
		productDTO = EntitiesFactory.createdProductDTO();
		cateogry = EntitiesFactory.createdCategory();
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(repository.findById(validId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(invalidId)).thenReturn(Optional.empty());
		Mockito.when(repository.getOne(validId)).thenReturn(product);
		Mockito.when(catrepo.getOne(validId)).thenReturn(cateogry);
		Mockito.when(repository.getOne(invalidId)).thenThrow(EntityNotFoundException.class);
		Mockito.doNothing().when(repository).deleteById(validId);
//		Mockito.doThrow(EntityNotFoundException.class).when(repository).getReferenceById(invalidId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(invalidId);
		Mockito.doThrow(DataBaseException.class).when(repository).deleteById(dependId);
	}
	
	@Test
	public void updateShouldThrowExceptionwhenInvalidId() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.update(invalidId, productDTO);
		});
	}
	
	
	@Test
	public void updateShouldReturnProductDTOWhenIdIsValid() {
		ProductDTO result = service.update(validId, productDTO);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, times(1)).getOne(validId);
	}

	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository).findAll(pageable);		
	}
	
	@Test
	public void findByIdShouldThrownExceptionWhenIdIsInvalid() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(invalidId);
		});
		
		
		Mockito.verify(repository, times(1)).findById(invalidId);
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdIsValid() {
		ProductDTO result = service.findById(validId); 
		Assertions.assertNotNull(result);
		Mockito.verify(repository, times(1)).findById(validId);
		
	}
	
	
	@Test
	public void deleteShouldDoNothingWhenIdIsValid() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(validId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(validId);
	}

	@Test
	public void deleteShouldThrowsExceptionsWhenIdIsInvalid() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(invalidId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(invalidId);
	}

	@Test
	public void deleteShouldThrowsExceptionsWhenIdIsDependent() {
		
		Assertions.assertThrows(DataBaseException.class, ()->{
			service.delete(dependId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependId);
	}
}
