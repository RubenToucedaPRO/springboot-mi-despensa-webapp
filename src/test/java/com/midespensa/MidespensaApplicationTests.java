package com.midespensa;

import com.midespensa.controllers.ProductController;
import com.midespensa.entities.Product;
import com.midespensa.repositories.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MidespensaApplicationTests {

	/*@InjectMocks
	private ProductController productController;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private Model model;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testFindAll() {
		// Arrange
		List<Product> products = new ArrayList<>();
		products.add(new Product("1111", "Product 1", "brand","food","amount","20/01/2025"));
		products.add(new Product("2222", "Product 2", "brand","food","amount",null));

		Mockito.when(productRepository.findAll()).thenReturn(products);

		// Act
		String viewName = productController.findAll(model);

		// Assert
		assertEquals("product-list", viewName);
		Mockito.verify(model).addAttribute("products", products);
	}*/

	// Test other methods similarly
}
