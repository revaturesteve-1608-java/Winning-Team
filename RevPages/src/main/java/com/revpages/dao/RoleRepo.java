package com.revpages.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.revpages.dto.Role;

@RepositoryRestResource
public interface RoleRepo extends JpaRepository<Role, Integer>{
	
}