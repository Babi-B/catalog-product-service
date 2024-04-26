//package com.gogroups.repository;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import com.gogroups.model.Product;
//
//@DataJpaTest
//public class RepositoryTest {
//	@Autowired
//	private ProductJpaRepo pdtRepo;
//	
//	@Autowired
//	private TestEntityManager entityManager;
//	
//	@Test
//	void testInsertProduct() throws IOException {
//		File file = new File("some url");
//
//		Product product = new Product();
//		product.setProductId(product.getProductId());
//		byte[] bytes = Files.readAllBytes(file.toPath());
//		product.setProductImage(bytes);
//		
//		Product savedPdt = pdtRepo.save(product);
//		Product existPdt = entityManager.find(Product.class, savedPdt.getProductId());
//
//	}
//}
