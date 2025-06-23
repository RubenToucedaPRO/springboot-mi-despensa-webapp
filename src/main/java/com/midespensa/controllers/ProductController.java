package com.midespensa.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.User;
import com.midespensa.services.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Controlador encargado de manejar las rutas de los productos: crear, editar,
 * ver, eliminar, escanear y actualizar
 */
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	/**
	 * Muestra la página de productos al administrador autenticado.
	 *
	 * @param request Objeto HttpServletRequest para detectar si se accede desde
	 *                móvil
	 * @param model   Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param page    Número de página para la paginación
	 * @param size    Tamaño de página (por defecto 6)
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/list")
	public String findAll(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size) {
		boolean isMobile = request.getHeader("User-Agent").toLowerCase().contains("mobile");
		if (isMobile) {
			size = 10000;
		}
		Pageable pageable = PageRequest.of(page, size);
		Page<ProductDTO> products = productService.getAllProducts(pageable);
		model.addAttribute("productDTOs", products.getContent());
		model.addAttribute("context", "products");
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("pageTitle", "Productos");
		model.addAttribute("idUser", 1);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", products.getTotalPages());
		model.addAttribute("size", size);
		return "product/product-list";
	}

	/**
	 * Muestra la página de productos personalizados al administrador autenticado.
	 *
	 * @param request Objeto HttpServletRequest para detectar si se accede desde
	 *                móvil
	 * @param model   Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param page    Número de página para la paginación
	 * @param size    Tamaño de página (por defecto 6)
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/custom")
	public String findAllFilter(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ProductDTO> products = productService.getAllProductsNotIdUser(pageable, 1);
		model.addAttribute("productDTOs", products.getContent());
		model.addAttribute("context", "products");
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("pageTitle", "Productos personalizados");
		model.addAttribute("idUser", 1);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", products.getTotalPages());
		model.addAttribute("size", size);
		return "product/product-list";
	}

	/**
	 * Muestra la página de detalles del producto al usuario autenticado.
	 *
	 * @param context String para saber el contexto desde el que se accede para
	 *                luego retornar al mismo
	 * @param model   Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param page    Número de página para la paginación
	 * @param size    Tamaño de página (por defecto 6)
	 * @param user    Usuario autenticado obtenido del AuthenticationPrincipal
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/view")
	public String viewProduct(@RequestParam int id,
			@RequestParam(required = false, defaultValue = "product") String context, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size,
			@AuthenticationPrincipal User user) {
		ProductDTO productDTO = productService.getById(id);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("context", context);
		model.addAttribute("idUser", user.getId());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		return "product/product-view";
	}

	/**
	 * Añadir producto desde modal con datos a mano
	 *
	 * @param productDTO Objeto ProductDTO usado para pasar datos del producto desde
	 *                   la vista Thymeleaf
	 * @param user       Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page       Número de página para la paginación
	 * @param size       Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/new")
	public String addProductManual(@ModelAttribute ProductDTO productDTO, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size) {
		productDTO.setIdUser(user.getId());
		productService.addProduct(productDTO);
		return "redirect:/products/list?page=" + page + "&size=" + size;
	}

	/**
	 * Muestra la página de formulario del producto al administrador.
	 *
	 * @param context String para saber el contexto desde el que se accede para
	 *                luego retornar al mismo
	 * @param model   Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param page    Número de página para la paginación
	 * @param size    Tamaño de página (por defecto 6)
	 * @param user    Usuario autenticado obtenido del AuthenticationPrincipal
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/{id}/edit")
	public String editProduct(@PathVariable int id,
			@RequestParam(required = false, defaultValue = "product") String context,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size, Model model) {
		ProductDTO productDTO = productService.getById(id);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("context", context);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		return "product/product-form"; // Devolvemos la misma vista de formulario pero con los datos cargados
	}

	/**
	 * Editar producto desde admin
	 *
	 * @param productDTO Objeto ProductDTO usado para pasar datos del producto desde
	 *                   la vista Thymeleaf
	 * @param page       Número de página para la paginación
	 * @param size       Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/edit")
	public String updateProduct(@ModelAttribute("product") ProductDTO productDTO,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size) {
		productService.updateProduct(productDTO);
		return "redirect:/products/list?page=" + page + "&size=" + size;
	}

	/**
	 * Escanear un producto (puede ser scaneado desde productos, despensa o lista de
	 * la compra
	 *
	 * @param barcode String para código de barras
	 * @param context String para saber el contexto desde el que se accede para
	 *                luego retornar al mismo
	 * @param model   Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param page    Número de página para la paginación
	 * @param size    Tamaño de página (por defecto 11)
	 * @param user    Usuario autenticado obtenido del AuthenticationPrincipal
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/scanner")
	public String getProduct(@RequestParam String barcode,
			@RequestParam(required = false, defaultValue = "product") String context, Model model,
			@AuthenticationPrincipal User user) {
		ProductDTO productDto = null;
		// Si la lectura es desde la despensa se añade en pantry y en products si no
		// existe
		if (context.equals("pantry")) {
			productDto = productService.addProductFromPantry(barcode, user.getId());
			if (productDto.getTitle().equals("Sin nombre")) {
				model.addAttribute("barcode", barcode);
				model.addAttribute("productDTO", productDto);
				model.addAttribute("context", context);
				return "pantry/pantry-form";
			}
			return "redirect:/pantry/list";
		}
		// Si la lectura es desde la lista de la compra se añade en lista de la compra y
		// en products si no existe
		if (context.equals("shoppingList")) {
			productDto = productService.addProductFromShoppingList(barcode, user.getId());
			if (productDto.getTitle().equals("Sin nombre")) {
				model.addAttribute("barcode", barcode);
				model.addAttribute("productDTO", productDto);
				model.addAttribute("context", context);
				model.addAttribute("idUser", user.getId());
				return "pantry/pantry-form";
			}
			return "redirect:/shoppingList/list";
		}
		// Si la lectura es desde products se muestra formulario con los datos obtenidos
		// o sin datos
		productDto = productService.getByBarcode(barcode, false, user.getId());
		model.addAttribute("context", context);
		model.addAttribute("productDTO", productDto);
		return "product/product-form";
	}

	/**
	 * Borrar el producto seleccionado
	 *
	 * @param id   entero usado para pasar id del producto a borrar desde la vista
	 *             Thymeleaf
	 * @param page Número de página para la paginación
	 * @param size Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/{id}/delete")
	public String deleteProduct(Model model, @PathVariable int id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size, RedirectAttributes redirectAttributes) {
		if (productService.deleteProduct(id)) {
			// Si el producto fue borrado, añade un mensaje de éxito
			redirectAttributes.addFlashAttribute("success", "El producto ha sido eliminado correctamente.");
		} else {
			// Si el producto no fue borrado, añade un mensaje de error
			redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el producto (en uso)");
		}
		model.addAttribute("page", page);
		redirectAttributes.addAttribute("page", page);
		redirectAttributes.addAttribute("size", size);
		return "redirect:/products/list?page=" + page + "&size=" + size;
	}

	/**
	 * Actualizar productos desde API externa
	 *
	 * @param user Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page Número de página para la paginación
	 * @param size Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/updateOpenFoodFacts")
	public String updateProductsFromOpenFoodFacts(@AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size.products}'}") int size) {
		productService.updateProductsFromOpenFoodFacts();
		return "redirect:/products/list?page=" + page + "&size=" + size;
	}
}
