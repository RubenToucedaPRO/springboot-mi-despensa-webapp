package com.midespensa.services;

import java.util.List;

import com.midespensa.entities.Tag;

public interface TagService {
	// Busca un tag por nombre o lo crea si no existe
	Tag findOrCreate(String tagName);

	// Devuelve una lista de nombres de tags que contienen los caracteres
	// introducidos por el usuario
	List<String> findTagsByNameContaining(String chars);
}
