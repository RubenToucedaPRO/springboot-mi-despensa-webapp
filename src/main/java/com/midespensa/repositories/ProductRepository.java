package com.midespensa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	Optional<Product> findByBarcodeAndIdUser(String code, int i);

	void deleteById(int id);

	boolean existsByBarcodeAndIdUser(String barcode, int idUser);

	Page<Product> findAll(Pageable pageable);

	Page<Product> findByIdUserNot(Pageable pageable, int idUser);
}
