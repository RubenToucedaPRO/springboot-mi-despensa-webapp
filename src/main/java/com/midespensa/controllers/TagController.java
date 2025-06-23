package com.midespensa.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.midespensa.services.TagService;

/**
 * Controlador encargado de manejar las llamadas Rest del formulario tags
 * 
 */
@RestController
@RequestMapping("/tags")
public class TagController {

	private final TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	/**
	 * Endpoint para obtener sugerencias de tags basados en una consulta parcial por
	 * los caracteres introducidos
	 * 
	 * @param query Cadena de texto usada para buscar coincidencias en los nombres
	 *              de los tags
	 * @return tags sugeridos
	 */
	@GetMapping("/suggest")
	public ResponseEntity<List<String>> suggestTags(@RequestParam String query) {
		List<String> suggestions = tagService.findTagsByNameContaining(query);
		return ResponseEntity.ok(suggestions);
	}

}
