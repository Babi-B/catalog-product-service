package com.gogroups.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gogroups.repository.CategoryJpaRepo;
import com.gogroups.repository.ProductJpaRepo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/public")
@Api(tags = "Contains all APIs that are public")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {
	@Autowired
	ProductJpaRepo pdtRepo;

	@Autowired
	CategoryJpaRepo catRepo;

	@ApiOperation(value = "This method is used to get all the categories and products for public display.")
	@GetMapping(value = { "/landing-page", "/", "/welcome" })
	public ResponseEntity<?> displayAll() {

		Map<String, List<?>> pageContent = new HashMap<String, List<?>>();
		pageContent.put("products", pdtRepo.findAll());
		pageContent.put("categories", catRepo.findAll());

		return ResponseEntity.status(HttpStatus.OK).body(pageContent);
	}
}
