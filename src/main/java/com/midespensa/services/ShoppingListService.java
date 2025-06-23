package com.midespensa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.User;

public interface ShoppingListService {

	Page<ProductDTO> getAllByIdUser(int userId, Pageable pageable);

	void addProductFromPantry(int idProduct, int idUser);

	void addProductManual(ProductDTO product, int idUser);

	void updateUnity(int id, int unity, int idUser);

	void delete(int idProduct, int idUser);

	void deleteAll(int idUser);

	long getShoppingListSize(int id);

	boolean sendShoppingList(User user);
}
