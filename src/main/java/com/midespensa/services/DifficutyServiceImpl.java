package com.midespensa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.midespensa.entities.Difficulty;
import com.midespensa.repositories.DifficultyRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de obtener la lista de dificultades
 */
@Service
@RequiredArgsConstructor
public class DifficutyServiceImpl implements DifficultyService {
	private final DifficultyRepository difficultyRepository;

	/**
	 * Método obtener todas las dificultades en base de datos
	 * 
	 * @return Lista de dificultades
	 */
	@Override
	public List<Difficulty> findAll() {
		return difficultyRepository.findAll();
	}

}
