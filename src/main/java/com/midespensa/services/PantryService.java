package com.midespensa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.midespensa.dtos.ProductDTO;

public interface PantryService {

	Page<ProductDTO> getAllByIdUser(int userId, Pageable pageable);

	ProductDTO getByIdProduct(int id);

	void addProductManual(ProductDTO product, int idUser);

	void addProductFromShoppingList(int id, int idUser);

	void updateProduct(ProductDTO product);

	void updateUnity(int id, int unity, int idUser);

	void deleteProduct(int id, int idUser);

	void deleteAll(int idUser);

}
