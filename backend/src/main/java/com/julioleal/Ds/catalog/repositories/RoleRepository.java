package com.julioleal.Ds.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julioleal.Ds.catalog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
