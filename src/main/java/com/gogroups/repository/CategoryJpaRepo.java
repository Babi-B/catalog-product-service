package com.gogroups.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogroups.model.Category;

@Repository
public interface CategoryJpaRepo extends JpaRepository<Category, Long> {
	Boolean existsByCategoryName(String name);
}
