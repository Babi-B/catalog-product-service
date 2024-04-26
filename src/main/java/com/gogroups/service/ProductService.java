package com.gogroups.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogroups.model.Category;
import com.gogroups.model.Product;
import com.gogroups.repository.ProductJpaRepo;

@Service
public class ProductService {
	@Autowired
	private ProductJpaRepo repo;

	public List<Product> listAllProducts() {
		return repo.findAll();
	}

	public List<Product> listAllProductsByCategory(Category category) {
		return repo.findByCategory(category);
	}

	public Optional<Product> listProductById(long id) {
		return repo.findById(id);
	}

	public Product saveProduct(Product product) {
		return repo.save(product);
	}

	public void deleteProduct(long id) {
		repo.deleteById(id);
	}

}
