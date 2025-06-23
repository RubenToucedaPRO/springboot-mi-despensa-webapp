package com.midespensa.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.ShoppingListItem;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingListItem, IdProductIdUser> {

	Page<ShoppingListItem> findAllById_IdUser(int idUser, Pageable pageable);

	boolean existsById_Id(int id);

	long countById_IdUser(int idUser);

	List<ShoppingListItem> findAllById_IdUser(int id);

	void deleteAllById_IdUser(int idUser);
}
