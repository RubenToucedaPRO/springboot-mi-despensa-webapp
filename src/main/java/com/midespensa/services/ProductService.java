package com.midespensa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.midespensa.dtos.ProductDTO;

public interface ProductService {

	Page<ProductDTO> getAllProducts(Pageable pageable);

	Page<ProductDTO> getAllProductsNotIdUser(Pageable pageable, int idUser);

	ProductDTO getById(int id);

	void addProduct(ProductDTO productDto);

	ProductDTO addProductFromPantry(String barcode, int idUser);

	ProductDTO addProductFromShoppingList(String barcode, int idUser);

	void updateProduct(ProductDTO productDto);

	boolean deleteProduct(int id);

	// Obtiene producto de products, si no de productUsers y si no de openFoodFacts
	ProductDTO getByBarcode(String barcode, boolean pantry, int idUser);

	void updateProductsFromOpenFoodFacts();
}
