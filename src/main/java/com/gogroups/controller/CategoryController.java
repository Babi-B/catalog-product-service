package com.gogroups.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gogroups.model.Category;
import com.gogroups.model.User;
import com.gogroups.payload.response.ResponseMessage;
import com.gogroups.service.CategoryService;
import com.gogroups.service.ImageService;
import com.gogroups.service.UserSerivce;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Category Controller")
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

	@Autowired
	private CategoryService catService;

	@Autowired
	private UserSerivce userService;
	@Autowired
	private ImageService imgService;

	@Authorization(value = "jwtToken")
	@ApiOperation(value = "This method is used to get all the categories.", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/categories")
	public ResponseEntity<?> retrieveAllCategories() {
		if (catService.listCategories().isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("No categories available"), HttpStatus.OK);
		} else {
			List<Category> categories = catService.listCategories();
			for (Category cat : categories) {
				cat.setCategoryImage(imgService.convertToBase64(cat.getCategoryImage()));
			}
		}
		return new ResponseEntity<>(catService.listCategories(), HttpStatus.OK);
	}

	@ApiOperation(value = "This method is used to get all the categories.", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> retrieveCategoryById(@PathVariable("id") long id) {
		Optional<Category> categorytData = catService.listCategoryById(id);
		byte[] img = categorytData.get().getCategoryImage();
		if (categorytData.isPresent()) {
			categorytData.get().setCategoryImage(imgService.convertToBase64(img));
			return new ResponseEntity<>(categorytData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "This method is used to create a category.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PostMapping("/categories")
	@ResponseBody
	public ResponseEntity<?> createCategory(@Valid @RequestBody Category category, Authentication authentication) {
		if (catService.categoryExists(category.getCategoryName())) {
			return ResponseEntity.badRequest().body(new ResponseMessage("Error: Category already exists!"));
		} else {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Category newCategory = catService.saveCategory(new Category(category.getCategoryId(),
					category.getCategoryName(), null, category.getCategoryDescription(),
					(userService.loadUserDetails(userDetails.getUsername())).get()));

			return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
		}
	}

	@ApiOperation(value = "This method is used to edit a category.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PutMapping("/categories/{id}")
	public ResponseEntity<?> editCategory(@PathVariable long id, @RequestBody Category category) {

		Optional<Category> categoryData = catService.listCategoryById(id);

		if (categoryData.isPresent()) {
			if (catService.categoryExists(category.getCategoryName()) && categoryData.get().getCategoryId() != id) {
				return ResponseEntity.badRequest().body(new ResponseMessage("Error: Category already exists!"));
			} else {
				Category editCategory = categoryData.get();
				editCategory.setCategoryName(category.getCategoryName());
				editCategory.setCategoryDescription(category.getCategoryDescription());

				return new ResponseEntity<>(catService.saveCategory(editCategory), HttpStatus.NO_CONTENT);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "This method is used to delete a category.", authorizations = {
			@Authorization(value = "jwtToken") })
	@DeleteMapping("/categories/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteCategoryById(Authentication authentication, @PathVariable long id) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.loadUserDetails(userDetails.getUsername()).get();
		Category categoryData = (catService.listCategoryById(id)).get();

		if (user == categoryData.getUser() || user.getRoles().toString().toLowerCase().equals("admin")) {
			try {
				catService.deleteCategory(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else
			return new ResponseEntity<>(new ResponseMessage("You do not have the authority to perform this operation"),
					HttpStatus.UNAUTHORIZED);

	}

}
