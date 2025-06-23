package com.midespensa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midespensa.entities.Difficulty;

@Repository
public interface DifficultyRepository extends JpaRepository<Difficulty, Integer> {

}
