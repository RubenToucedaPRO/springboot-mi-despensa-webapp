package com.midespensa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.midespensa.entities.Barcode;
import com.midespensa.exceptions.OpenFoodFactsApiException;

import pl.coderion.model.Product;
import pl.coderion.model.ProductResponse;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

/**
 * Repositorio que obtiene a partir del código de barras los datos del producto
 * de la API Externa OpenFoodsFacts
 */
@Repository
public class OpenFoodFactsRepository {

	/**
	 * Obtiene los datos de un producto desde la API de Open Food Facts usando su
	 * código de barras.
	 *
	 * @param code Código de barras del producto a buscar
	 * @return Un Optional que contiene el objeto Barcode si se encuentra el
	 *         producto, o un Optional vacío si no existe o hay un fallo en la
	 *         llamada a la API.
	 * @throws OpenFoodFactsApiException si ocurre un error en la conexión con la
	 *                                   API externa
	 */
	public Optional<Barcode> getProduct(String code) {
		OpenFoodFactsWrapper wrapper = new OpenFoodFactsWrapperImpl();

		try {
			ProductResponse productResponse = wrapper.fetchProductByCode(code);

			// Verifica si la respuesta es válida
			if (productResponse.isStatus() && productResponse.getStatusVerbose().equals("product found")) {
				Product productBarcode = productResponse.getProduct();

				// Crea un nuevo objeto Barcode y asigna los valores
				Barcode barcode = new Barcode();
				barcode.setTitle(productBarcode.getProductName());
				barcode.setBarcode(productBarcode.getCode());
				barcode.setAmount(productBarcode.getQuantity());
				barcode.setBrand(productBarcode.getBrands());
				barcode.setImage(productBarcode.getImageSmallUrl());

				// Devuelve el objeto Product envuelto en un Optional
				return Optional.of(barcode);
			}
		} catch (Exception e) {
			// Capturamos la excepcion pero no damos error, retornamos producto vacio
			// throw new OpenFoodFactsApiException("Error al conectar con Open Food Facts
			// para el código de barras: " + code, e);
		}
		// Si no hay producto válido, devuelve un Optional vacío
		return Optional.empty();

	}
}
