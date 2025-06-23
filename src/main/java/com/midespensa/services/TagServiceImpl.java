package com.midespensa.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.midespensa.entities.Tag;
import com.midespensa.repositories.TagRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de los tags
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

	private final TagRepository tagRepository;

	/**
	 * Método obtener y crear tag en BD en base de datos
	 * 
	 * @param tagName nombre del tag insertado por el usuario
	 * @return tag creado u obtenido
	 */
	@Override
	public Tag findOrCreate(String tagName) {
		return tagRepository.findByName(tagName).orElseGet(() -> {
			Tag newTag = new Tag();
			newTag.setName(tagName);
			return tagRepository.save(newTag);
		});
	}

	/**
	 * Método obtener lista de tags de la base de datos en función de caracteres
	 * introducidos
	 * 
	 * @param chars caracteres introducidos por el usuario que deben contener los
	 *              tags solicitados a BD
	 * @return lista de tags obtenidos que contienen esos caracteres
	 */
	@Override
	public List<String> findTagsByNameContaining(String chars) {
		return tagRepository.findByNameContainingIgnoreCase(chars).stream().map(Tag::getName)
				.collect(Collectors.toList());
	}
}
