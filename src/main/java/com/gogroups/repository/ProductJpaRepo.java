package com.gogroups.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogroups.model.Category;
import com.gogroups.model.Product;

@Repository
public interface ProductJpaRepo extends JpaRepository<Product, Long> {
	public List<Product> findByCategory(Category category);
}
