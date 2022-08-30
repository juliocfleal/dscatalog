package com.julioleal.Ds.catalog.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julioleal.Ds.catalog.dto.RoleDTO;
import com.julioleal.Ds.catalog.dto.UserDTO;
import com.julioleal.Ds.catalog.dto.UserInsertDTO;
import com.julioleal.Ds.catalog.dto.UserUpdateDTO;
import com.julioleal.Ds.catalog.entities.Role;
import com.julioleal.Ds.catalog.entities.User;
import com.julioleal.Ds.catalog.repositories.RoleRepository;
import com.julioleal.Ds.catalog.repositories.UserRepository;
import com.julioleal.Ds.catalog.services.exceptions.DataBaseException;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional
	public Page<UserDTO> findAllPaged(Pageable pageRequest){
		Page<User> list = repository.findAll(pageRequest);
		return list.map(x -> new UserDTO(x));
	}
	
	@Transactional
	public UserDTO findById(Long id) {
		User obj = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Entity not found!"));
		return new UserDTO(obj);
	}
	
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User obj = new User();
		copyDtoToEntity(dto, obj);
		obj.setPassword(passwordEncoder.encode(dto.getPassword()));
		obj = repository.save(obj);
		return new UserDTO(obj);
	}
	
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
		User obj = repository.getReferenceById(id);
		obj.setFirstName(dto.getFirstName());
		obj.setLastNAme(dto.getLastName());
		obj.setEmail(dto.getEmail());
		obj.setPassword(dto.getEmail());
		obj = repository.save(obj);
		return new UserDTO(obj);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	@Transactional
	public void delete (Long id) {
		try {
			repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found!");
		}catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(UserDTO dto, User obj) {
		obj.setFirstName(dto.getFirstName());
		obj.setLastNAme(dto.getLastName());
		obj.setEmail(dto.getEmail());

		for(RoleDTO roleDTO: dto.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDTO.getId());
			obj.getRoles().add(role);
		}
	}
}
