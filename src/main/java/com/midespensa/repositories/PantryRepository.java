package com.midespensa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.Pantry;

@Repository
public interface PantryRepository extends JpaRepository<Pantry, IdProductIdUser> {

	Page<Pantry> findAllById_IdUser(int idUser, Pageable pageable);

	void deleteAllById_IdUser(int idUser);

	boolean existsById_Id(int id);

}
