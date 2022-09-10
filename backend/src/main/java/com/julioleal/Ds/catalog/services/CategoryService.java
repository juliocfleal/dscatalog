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
import com.julioleal.Ds.catalog.entities.Category;
import com.julioleal.Ds.catalog.repositories.CategoryRepository;
import com.julioleal.Ds.catalog.services.exceptions.DataBaseException;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable peageble) {
		Page<Category> list = repo.findAll(peageble);
		Page<CategoryDTO> listDTO = list.map(x -> new CategoryDTO(x));
		return listDTO;
	}

	@Transactional
	public CategoryDTO findById(Long id) {
		Category cat = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		CategoryDTO catDTO = new CategoryDTO(cat);
		return catDTO;
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category ctgr = new Category();
		ctgr.setName(dto.getName());
		ctgr = repo.save(ctgr);
		return new CategoryDTO(ctgr);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {

			Category ctgr = repo.getOne(id);
			ctgr.setName(dto.getName());
			ctgr = repo.save(ctgr);
			return new CategoryDTO(ctgr);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
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
}
