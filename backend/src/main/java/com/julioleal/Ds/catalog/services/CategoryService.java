package com.julioleal.Ds.catalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julioleal.Ds.catalog.dto.CategoryDTO;
import com.julioleal.Ds.catalog.entities.Category;
import com.julioleal.Ds.catalog.repositories.CategoryRepository;
import com.julioleal.Ds.catalog.services.exceptions.EntityNotfindException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repo.findAll();
		List<CategoryDTO> listDTO = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		return listDTO;
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category cat = repo.findById(id).orElseThrow(() -> new EntityNotfindException("Entity not found!")); 
		CategoryDTO catDTO = new CategoryDTO(cat);
		return catDTO;
	}
}
