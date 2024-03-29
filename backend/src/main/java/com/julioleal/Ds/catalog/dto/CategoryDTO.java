package com.julioleal.Ds.catalog.dto;

import java.io.Serializable;

import com.julioleal.Ds.catalog.entities.Category;

public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
		
	public CategoryDTO() {
		
	}
	
	public CategoryDTO(Category categoryEntity) {
		this.id = categoryEntity.getId();
		this.name = categoryEntity.getName();
		
	}

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
