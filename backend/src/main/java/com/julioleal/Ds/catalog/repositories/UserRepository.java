package com.julioleal.Ds.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julioleal.Ds.catalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
}
