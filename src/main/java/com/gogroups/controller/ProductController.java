package com.gogroups.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gogroups.model.Category;
import com.gogroups.model.Product;
import com.gogroups.payload.response.ResponseMessage;
import com.gogroups.service.CategoryService;
import com.gogroups.service.ImageService;
import com.gogroups.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/api")
@Api(tags = "Product Controller")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
	@Autowired
	ProductService pdtService;

	@Autowired
	CategoryService catService;

	@Autowired
	ImageService imgService;

	@Autowired
	Product product;

	@Autowired
	Category category;

	@ApiOperation(value = "This method is used to get all the products.", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/products")
	public ResponseEntity<?> retrieveAllProducts() {
		if (pdtService.listAllProducts().isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("No products available"), HttpStatus.OK);
		} else {
			List<Product> products = pdtService.listAllProducts();
			for (Product pdt : products) {
				pdt.setProductImage(imgService.convertToBase64(pdt.getProductImage()));
			}
			return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "This method is used to get a product by ID.", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> retrieveProductById(@PathVariable("id") long id) {
		Optional<Product> productData = pdtService.listProductById(id);
		byte[] img = productData.get().getProductImage();

		if (productData.isPresent()) {
			productData.get().setProductImage(imgService.convertToBase64(img));
			return new ResponseEntity<>(productData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "This method is used to get all products under a category.")
	@GetMapping("/products/category")
	public ResponseEntity<List<Product>> retrieveProductByCategoryId(@RequestParam("id") Category category) {

		List<Product> products = pdtService.listAllProductsByCategory(category);
		for (Product pdt : products) {
			pdt.setProductImage(imgService.convertToBase64(pdt.getProductImage()));
		}
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}

	@ApiOperation(value = "This method is used to create a product.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PostMapping("/products/category/{categoryId}")
	public ResponseEntity<Product> createProduct(@Valid @RequestBody Product _product,
			@PathVariable Category categoryId) {
		if (categoryId == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Product newProduct = pdtService.saveProduct(new Product(_product.getProductId(), _product.getProductName(),
					null, _product.getQuantity(), _product.getUnitPrice(), _product.getCurrency(), categoryId));

			return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
		}
	}

	@ApiOperation(value = "This method is used to get edit a product.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PutMapping("/products/{productId}/category/{categoryId}")
	public ResponseEntity<Product> updateProduct(@PathVariable("productId") long productId,
			@PathVariable("categoryId") Category categoryId, @Valid @RequestBody Product product) {

		Optional<Product> productlData = pdtService.listProductById(productId);
		if (productlData.isPresent()) {
			Product editProduct = productlData.get();
			editProduct.setCategory(categoryId);
			editProduct.setProductName(product.getProductName());
			editProduct.setQuantity(product.getQuantity());
			editProduct.setUnitPrice(product.getUnitPrice());
			editProduct.setCurrency(product.getCurrency());

			return new ResponseEntity<>(pdtService.saveProduct(editProduct), HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "This method is used to delete a product.", authorizations = {
			@Authorization(value = "jwtToken") })
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<HttpStatus> deleteProductById(@PathVariable long productId) {
		try {
			pdtService.deleteProduct(productId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
