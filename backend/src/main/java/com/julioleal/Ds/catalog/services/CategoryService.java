package com.julioleal.Ds.catalog.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julioleal.Ds.catalog.dto.CategoryDTO;
import com.julioleal.Ds.catalog.entities.Category;
import com.julioleal.Ds.catalog.repositories.CategoryRepository;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repo.findAll();
		List<CategoryDTO> listDTO = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		return listDTO;
	}

	@Transactional(readOnly = true)
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

			Category ctgr = repo.getReferenceById(id);
			ctgr.setName(dto.getName());
			ctgr = repo.save(ctgr);
			return new CategoryDTO(ctgr);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}
}
