package com.gogroups.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gogroups.model.Category;
import com.gogroups.model.Product;
import com.gogroups.model.User;
import com.gogroups.payload.response.ResponseMessage;
import com.gogroups.service.CategoryService;
import com.gogroups.service.ImageService;
import com.gogroups.service.ProductService;
import com.gogroups.service.UserSerivce;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/api")
@Api(tags = "Image Controller")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

	@Autowired
	private ProductService pdtService;

	@Autowired
	Product product;

	@Autowired
	private CategoryService catService;

	@Autowired
	Category category;

	@Autowired
	private ImageService imgService;

	@Autowired
	UserSerivce userService;

	@ApiOperation(value = "This method is used to upload an image for a category.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PutMapping("/categories/{categoryId}/upload")
	public ResponseEntity<ResponseMessage> uploadCategoryImage(@PathVariable("categoryId") long catID,
			@RequestParam MultipartFile file) {
		Optional<Category> category = catService.listCategoryById(catID);
		if (category.isPresent()) {
			if (imgService.fileIsAnImage(file)) {
				try {
					category.get().setCategoryImage(imgService.storeImage(file));
					catService.saveCategory(category.get());
					return new ResponseEntity<>(
							new ResponseMessage("Uploaded " + file.getOriginalFilename() + " successfully!"),
							HttpStatus.OK);
				} catch (Exception e) {
					return new ResponseEntity<>(
							new ResponseMessage("Failed to upload " + file.getOriginalFilename() + "!"),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				return new ResponseEntity<>(new ResponseMessage("Incorrect file type."), HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<>(new ResponseMessage("Category Does not exist"), HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "This method is used to upload an image for a product.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PutMapping(value = "/products/{productId}/upload")
	public ResponseEntity<ResponseMessage> uploadProductImage(@PathVariable("productId") long pdtID,
			@RequestParam MultipartFile file) {
		Optional<Product> product = pdtService.listProductById(pdtID);
		if (product.isPresent()) {
			if (imgService.fileIsAnImage(file)) {
				try {
					product.get().setProductImage(imgService.storeImage(file));
					pdtService.saveProduct(product.get());
					return new ResponseEntity<>(
							new ResponseMessage("Uploaded  " + file.getOriginalFilename() + " successfully!"),
							HttpStatus.OK);

				} catch (Exception e) {

					return new ResponseEntity<>(
							new ResponseMessage("Failed to upload " + file.getOriginalFilename() + "!"),
							HttpStatus.EXPECTATION_FAILED);

				}
			} else {
				return new ResponseEntity<>(new ResponseMessage("Incorrect file type!"), HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<>(new ResponseMessage("Product does not exist!"), HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "This method is used to upload an image for a user.", authorizations = {
			@Authorization(value = "jwtToken") })
	@PutMapping("/user/upload")
	public ResponseEntity<ResponseMessage> uploadUserImage(Authentication authentication,
			@RequestParam MultipartFile file) {
		User user = (User) authentication.getPrincipal();
		if (imgService.fileIsAnImage(file)) {
			try {
				user.setUserImage(imgService.storeImage(file));
				userService.saveUser(user);
				return new ResponseEntity<>(
						new ResponseMessage("Uploaded  " + file.getOriginalFilename() + " successfully!"),
						HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<>(new ResponseMessage("Failed to upload " + file.getOriginalFilename() + "!"),
						HttpStatus.EXPECTATION_FAILED);
			}
		} else {
			return new ResponseEntity<>(new ResponseMessage("Incorrect file type!"), HttpStatus.NOT_ACCEPTABLE);
		}
	}

}