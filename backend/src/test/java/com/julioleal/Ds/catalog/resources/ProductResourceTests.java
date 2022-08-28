package com.julioleal.Ds.catalog.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julioleal.Ds.catalog.dto.ProductDTO;
import com.julioleal.Ds.catalog.entities.Product;
import com.julioleal.Ds.catalog.services.ProductService;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;
import com.julioleal.Ds.catalog.tests.EntitiesFactory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Long validId;
	private Long invalidId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> pageDTO;
	private Long integratedId;
	private Product product;

	@BeforeEach
	void setUp() throws Exception {
		productDTO = EntitiesFactory.createdProductDTO();
		validId = 1L;
		invalidId = 1000L;
		integratedId = 500L;
		product = EntitiesFactory.createdProduct();

		pageDTO = new PageImpl<>(List.of(productDTO));
		when(service.findById(validId)).thenReturn(productDTO);
		when(service.findById(invalidId)).thenThrow(ResourceNotFoundException.class);
		when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(pageDTO);

		when(service.update(ArgumentMatchers.eq(validId), ArgumentMatchers.any())).thenReturn(productDTO);
		when(service.update(ArgumentMatchers.eq(invalidId), ArgumentMatchers.any()))
				.thenThrow(ResourceNotFoundException.class);
		doNothing().when(service).delete(validId);
		when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		doThrow(ResourceNotFoundException.class).when(service).delete(invalidId);
		doThrow(DataIntegrityViolationException.class).when(service).delete(integratedId);
	}

	@Test
	public void insertShouldReturnCreatedAndProductDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(product);
		ResultActions result = mockMvc.perform(post("/products").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

//	@Test
//	public void deleteShouldReturnWhenIdIsIntegrated() throws Exception{
//		ResultActions result = mockMvc.perform(delete("/products/{id}", integratedId)
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//		
//		result.andExpect(status().isNotFound());
//	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdIsInvalid() throws Exception {

		ResultActions result = mockMvc.perform(delete("/products/{id}", invalidId)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());

	}

	@Test
	public void deleteShouldReturnNothiungWhenIdIsValid() throws Exception {
		ResultActions result = mockMvc.perform(delete("/products/{id}", validId).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdISValid() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", validId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void updateShouldThrowExceptionWhenIdIsInvalid() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", invalidId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isNotFound());
	}

	@Test
	public void findByIdShouldThrowExceptionWhenIdIsInvalid() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", invalidId).accept(MediaType.APPLICATION_JSON));
		result.andExpectAll(status().isNotFound());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdIsValid() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", validId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {

		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

}
