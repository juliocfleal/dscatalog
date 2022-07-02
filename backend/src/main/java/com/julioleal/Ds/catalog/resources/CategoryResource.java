package com.julioleal.Ds.catalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julioleal.Ds.catalog.dto.CategoryDTO;
import com.julioleal.Ds.catalog.services.CategoryService;

@RestController
@RequestMapping(value= "/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryeService;

	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = categoryeService.findAll();
		
		return ResponseEntity.ok().body(list);
	}
	
}
