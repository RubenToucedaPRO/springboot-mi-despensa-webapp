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
import com.midespensa.services.ShoppingListService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Controlador encargado de manejar las rutas de la lista de la compra
 */
@Controller
@RequestMapping("/shoppingList")
@RequiredArgsConstructor
public class ShoppingListController {

	private final ShoppingListService shoppingListService;

	/**
	 * Muestra la página de la lista de la compra del usuario autenticado.
	 *
	 * @param request Objeto HttpServletRequest para detectar si se accede desde
	 *                móvil
	 * @param model   Objeto Model usado para pasar datos a la vista Thymeleaf
	 * @param page    Número de página para la paginación
	 * @param size    Tamaño de página (por defecto 11)
	 * @param user    Usuario autenticado obtenido del AuthenticationPrincipal
	 * @return El nombre de la vista Thymeleaf
	 */
	@GetMapping("/list")
	public String findAll(HttpServletRequest request, Model model, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size) {
		boolean isMobile = request.getHeader("User-Agent").toLowerCase().contains("mobile");
		if (isMobile) {
			size = 10000;
		}
		Pageable pageable = PageRequest.of(page, size);
		Page<ProductDTO> products = shoppingListService.getAllByIdUser(user.getId(), pageable);
		model.addAttribute("productDTOs", products);
		model.addAttribute("context", "shoppingList");
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", products.getTotalPages());
		model.addAttribute("size", size);
		return "shoppingList/shopping-list";
	}

	/**
	 * Añadir producto desde modal scanner con datos a mano.
	 *
	 * @param productDTO Objeto ProductDTO usado para pasar datos del producto desde
	 *                   la vista Thymeleaf
	 * @param user       Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page       Número de página para la paginación
	 * @param size       Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/new")
	public String addProduct(@ModelAttribute ProductDTO productDTO, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size) {
		shoppingListService.addProductManual(productDTO, user.getId());
		return "redirect:/shoppingList/list?page=" + page + "&size=" + size;
	}

	/**
	 * Modificar unidades del producto.
	 *
	 * @param id    entero usado para pasar id del producto desde la vista Thymeleaf
	 * @param unity entero usado para pasar las unidade del producto desde la vista
	 *              Thymeleaf
	 * @param user  Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page  Número de página para la paginación
	 * @param size  Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/updateUnits")
	public String updateUnity(@RequestParam int id, @RequestParam int unity, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size) {
		shoppingListService.updateUnity(id, unity, user.getId());
		return "redirect:/shoppingList/list?page=" + page + "&size=" + size;
	}

	/**
	 * Añadir producto a la lista de la compra desde la despensa
	 *
	 * @param id                 entero usado para pasar id del producto desde la
	 *                           vista Thymeleaf
	 * @param user               Usuario autenticado obtenido del
	 *                           AuthenticationPrincipal
	 * @param page               Número de página para la paginación
	 * @param size               Tamaño de página (por defecto 11)
	 * @param redirectAttributes redirigir los mensajes de operacion existosa a la
	 *                           vista
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/{id}/add")
	public String addProductFromPantry(@PathVariable int id, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size,
			RedirectAttributes redirectAttributes) {
		shoppingListService.addProductFromPantry(id, user.getId());
		redirectAttributes.addFlashAttribute("success", "Producto añadido a la lista de la compra.");
		return "redirect:/pantry/list?page=" + page + "&size=" + size;
	}

	/**
	 * Borrar el producto seleccionado
	 *
	 * @param id   entero usado para pasar id del producto a borrar desde la vista
	 *             Thymeleaf
	 * @param user Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page Número de página para la paginación
	 * @param size Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/{id}/delete")
	public String deleteProduct(@PathVariable int id, @AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size) {
		shoppingListService.delete(id, user.getId());
		return "redirect:/shoppingList/list?page=" + page + "&size=" + size;
	}

	/**
	 * Vaciar la lista de la compra desde la pagina de ajustes usuario
	 *
	 * @param user Usuario autenticado obtenido del AuthenticationPrincipal
	 * @param page Número de página para la paginación
	 * @param size Tamaño de página (por defecto 11)
	 * @return Redirección a la vista Thymeleaf
	 */
	@PostMapping("/deleteAll")
	public String deleteAllProduct(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size) {
		shoppingListService.deleteAll(user.getId());
		return "redirect:/shoppingList/list?page=" + page + "&size=" + size;
	}

	/**
	 * Enviar por correo la lista de compra
	 *
	 * @param user               Usuario autenticado obtenido del
	 *                           AuthenticationPrincipal
	 * @param page               Número de página para la paginación
	 * @param size               Tamaño de página (por defecto 11)
	 * @param redirectAttributes redirigir los mensajes de operacion existosa o no a
	 *                           la vista
	 * @return Redirección a la vista Thymeleaf
	 */
	@GetMapping("/sendMail")
	public String sendMail(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "#{'${app.default.page.size}'}") int size, RedirectAttributes redirectAttributes) {

		if (shoppingListService.sendShoppingList(user)) {
			redirectAttributes.addFlashAttribute("success", "Lista de la compra enviada con éxito.");
		} else {
			redirectAttributes.addFlashAttribute("error", "Lista de la compra enviada con éxito.");
		}
		return "redirect:/shoppingList/list?page=" + page + "&size=" + size;
	}

}
