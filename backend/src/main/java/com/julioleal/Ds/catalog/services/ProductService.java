package com.julioleal.Ds.catalog.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julioleal.Ds.catalog.dto.CategoryDTO;
import com.julioleal.Ds.catalog.dto.ProductDTO;
import com.julioleal.Ds.catalog.entities.Category;
import com.julioleal.Ds.catalog.entities.Product;
import com.julioleal.Ds.catalog.repositories.CategoryRepository;
import com.julioleal.Ds.catalog.repositories.ProductRepository;
import com.julioleal.Ds.catalog.services.exceptions.DataBaseException;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private CategoryRepository catrepo;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageble) {
		Page<Product> list = repo.findAll(pageble);
		Page<ProductDTO> listDTO = list.map(x -> new ProductDTO(x));
		return listDTO;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product cat = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		ProductDTO catDTO = new ProductDTO(cat, cat.getCategories());
		return catDTO;
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product ctgr = new Product();
		copyDtoToEntity(ctgr, dto);
		ctgr = repo.save(ctgr);
		return new ProductDTO(ctgr);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {

			Product ctgr = repo.getOne(id);
			copyDtoToEntity(ctgr, dto);
			ctgr = repo.save(ctgr);
			return new ProductDTO(ctgr);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}

	public void delete(Long id) {
		try {
		repo.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found!");
		}catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}		
	}
	private void copyDtoToEntity(Product product, ProductDTO dto ) {
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setDate(dto.getDate());
		product.setImgUrl(dto.getImgUrl());
		product.setPrice(dto.getPrice());
		product.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = catrepo.getOne(catDto.getId());
			product.getCategories().add(category);
		}
	}
}
