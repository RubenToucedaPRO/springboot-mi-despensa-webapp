package com.midespensa.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.midespensa.services.IngredientService;

/**
 * Controlador encargado de manejar las llamadas Rest del formulario
 * ingredientes
 * 
 */
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

	private final IngredientService ingredientService;

	public IngredientController(IngredientService tagService) {
		this.ingredientService = tagService;
	}

	/**
	 * Endpoint para obtener sugerencias de ingredientes basados en una consulta
	 * parcial por los caracteres introducidos
	 * 
	 * @param query Cadena de texto usada para buscar coincidencias en los nombres
	 *              de los ingredientes
	 * @return ingredientes sugeridos
	 */
	@GetMapping("/suggest")
	public ResponseEntity<List<String>> suggestTags(@RequestParam String query) {
		List<String> suggestions = ingredientService.findIngredientsByNameContaining(query);
		return ResponseEntity.ok(suggestions);
	}
}
