package com.gogroups.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogroups.model.Category;
import com.gogroups.repository.CategoryJpaRepo;

@Service
public class CategoryService {

	@Autowired
	private CategoryJpaRepo repo;

	public List<Category> listCategories() {
		return repo.findAll();
	}

	public Optional<Category> listCategoryById(long id) {
		return repo.findById(id);
	}

	public Category saveCategory(Category category) {
		return repo.save(category);
	}

	public void deleteCategory(long id) {
		repo.deleteById(id);
	}
	public boolean categoryExists(String categoryName) {
		return repo.existsByCategoryName(categoryName);
	}

}
