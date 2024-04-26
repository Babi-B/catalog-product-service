package com.gogroups.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogroups.model.ERole;
import com.gogroups.model.Role;

@Repository
public interface RoleJpaRepo extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
	Optional<Role> findById(int id);

}