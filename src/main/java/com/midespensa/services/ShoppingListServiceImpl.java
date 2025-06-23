package com.midespensa.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.Product;
import com.midespensa.entities.ShoppingListItem;
import com.midespensa.entities.User;
import com.midespensa.exceptions.EmailSendException;
import com.midespensa.mappers.ProductMapper;
import com.midespensa.repositories.ProductRepository;
import com.midespensa.repositories.ShoppingListRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de manejar la lista de la compra
 */
@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {
	private final ShoppingListRepository shoppingListRepository;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final EmailService emailService;

	/**
	 * Método obtener lista de productos de la lista de la compra del usuario
	 * paginados
	 * 
	 * @param idUser   id del usuario del cual obtener la lista de productos de la
	 *                 lista de la compra
	 * @param pageable parámetros de paginación
	 * @return lista de productos paginados
	 */
	@Override
	public Page<ProductDTO> getAllByIdUser(int idUser, Pageable pageable) {
		// Objeto Pageable con ordenación por dateUpdate descendente
		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, "dateUpdate"));
		Page<ShoppingListItem> shoppingListItems = shoppingListRepository.findAllById_IdUser(idUser, sortedPageable);
		return shoppingListItems.map(x -> {
			ProductDTO p = productMapper.toDto(x.getProduct());
			p.setUnity(x.getUnity());
			return p;
		});
	}

	/**
	 * Método para añadir producto con datos a mano
	 * 
	 * @param idUser     id del usuario al cual asigna el producto
	 * @param productDto datos del producto
	 */
	@Override
	public void addProductManual(ProductDTO productDto, int idUser) {
		// Añadimos el idUser al producto para saber que es de ese usuario
		productDto.setIdUser(idUser);
		// Verificamos que el producto no fue añadido ya por ese usuario y tampoco
		// existe ya como producto público
		Product product = productRepository.findByBarcodeAndIdUser(productDto.getBarcode(), idUser)
				.orElseGet(() -> productRepository.findByBarcodeAndIdUser(productDto.getBarcode(), 1).orElseGet(() -> {
					// Mapeamos el producto a la entidad correspondiente en BD
					Product newProduct = productMapper.toProduct(productDto);
					// Guardamos el producto en BD
					return productRepository.save(newProduct);
				}));

		int unity = productDto.getUnity();
		// Si unity es -1 era que solo es edicion de caracteristicas, omitimos el resto
		// del metodo
		if (unity >= 0) {
			// Buscamos los datos de despensa para el producto y el usuario
			IdProductIdUser productKey = new IdProductIdUser(product.getId(), idUser);
			ShoppingListItem productSL = shoppingListRepository.findById(productKey)
					.orElse(new ShoppingListItem(new IdProductIdUser(product.getId(), idUser), 0, LocalDate.now()));
			// Si las unidades =0 establecemos establecemos 1 unidad por defecto
			if (unity == 0) {
				unity += 1;
			}
			// Inrementamos la unidades al producto de la despensa del usuario
			productSL.setUnity(productSL.getUnity() + unity);
			// Guardamos en BD
			this.shoppingListRepository.save(productSL);
		}
	}

	/**
	 * Método para añadir producto a la lista de la compra desde la despensa
	 * 
	 * @param id     id del producto que se añade a la lista de la compra
	 * @param idUser id del usuario al que se le añade
	 */
	@Override
	public void addProductFromPantry(int id, int idUser) {
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);
		// Buscamos el producto en la lista de la compra
		Optional<ShoppingListItem> shoppingListItem = shoppingListRepository.findById(productKey);
		if (shoppingListItem.isEmpty()) {
			// Si no existe en la lista de la compra crearlo y guardarlo
			shoppingListRepository.save(new ShoppingListItem(new IdProductIdUser(id, idUser), 1, LocalDate.now()));
		} else {
			// Si ya existe en la lista de la compra, actualizar la cantidad añadiendole las
			// unidades de la lista de la compra
			shoppingListItem.get().setUnity(shoppingListItem.get().getUnity() + 1);
			shoppingListRepository.save(shoppingListItem.get());
		}
	}

	/**
	 * Método para actualizar unidades de un producto
	 * 
	 * @param id     id del producto que se incrementa las unidades
	 * @param unity  unidades que se asignan al producto
	 * @param idUser id del usuario al que pertenece el producto
	 */
	@Override
	public void updateUnity(int id, int unity, int idUser) {
		Optional<ShoppingListItem> shoppOptional = shoppingListRepository.findById(new IdProductIdUser(id, idUser));
		if (shoppOptional.isPresent()) {
			shoppOptional.get().setUnity(unity);
			shoppingListRepository.save(shoppOptional.get());
		}
	}

	/**
	 * Método para borrar producto en la lista de la compra
	 * 
	 * @param id     id del producto que se borra
	 * @param idUser id del usuario al que se le borra
	 */
	@Override
	public void delete(int id, int idUser) {
		shoppingListRepository.deleteById(new IdProductIdUser(id, idUser));
	}

	/**
	 * Método para borrar todos los productos de la lista de la compra de un usuario
	 * específico
	 * 
	 * @param idUser id del usuario al que se le añade
	 */
	@Override
	@Transactional
	public void deleteAll(int idUser) {
		shoppingListRepository.deleteAllById_IdUser(idUser);
	}

	/**
	 * Método para saber cuantos productos tiene la lista de la compra de un usuario
	 * específico
	 * 
	 * @param idUser id del usuario
	 */
	@Override
	public long getShoppingListSize(int id) {
		return shoppingListRepository.countById_IdUser(id);
	}

	/**
	 * Método para enviar la lista de la compra por correo
	 * 
	 * @param idUser id del usuario
	 */
	@Override
	public boolean sendShoppingList(User user) {
		// Obtenemos los productos de la lista de la compra para el usuario
		List<ShoppingListItem> shoppingListItems = shoppingListRepository.findAllById_IdUser(user.getId());
		// Mapeamos los productos a una lista de productosDto
		List<ProductDTO> productList = shoppingListItems.stream().map(x -> {
			ProductDTO p = productMapper.toDto(x.getProduct());
			// Asignamos las unidades al producto
			p.setUnity(x.getUnity());
			return p;
		}).toList();
		try {
			// Llamamos al metodo para enviar la lista de la compra
			emailService.sendMailHtmlShoppingList(user.getEmail(), productList);
			return true;
		} catch (Exception e) {
			throw new EmailSendException("Error al enviar el correo de la lista de la compra");
		}
	}

}
