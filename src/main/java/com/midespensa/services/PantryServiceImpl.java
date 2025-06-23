package com.midespensa.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.Pantry;
import com.midespensa.entities.Product;
import com.midespensa.entities.ShoppingListItem;
import com.midespensa.mappers.ProductMapper;
import com.midespensa.repositories.PantryRepository;
import com.midespensa.repositories.ProductRepository;
import com.midespensa.repositories.ShoppingListRepository;
import com.midespensa.repositories.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de manejar la despensa
 */
@Service
@RequiredArgsConstructor
public class PantryServiceImpl implements PantryService {
	private final PantryRepository pantryRepository;
	private final ProductRepository productRepository;
	private final ShoppingListRepository shoppingListRepository;
	private final UsersRepository usersRepository;
	private final ProductMapper productMapper;

	/**
	 * Método obtener lista de productos de la despensa del usuario paginados
	 * 
	 * @param idUser   id del usuario del cual obtener la lista de productos de la
	 *                 despensa
	 * @param pageable parámetros de paginación
	 * @return lista de productos paginados
	 */
	@Override
	public Page<ProductDTO> getAllByIdUser(int idUser, Pageable pageable) {
		// Actualizamos la fecha de ultima conexion del usuario dado que después de
		// login llama este metodo
		usersRepository.updateLastLogin(idUser, LocalDate.now());
		// Objeto Pageable con ordenación por dateUpdate descendente
		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, "dateUpdate"));
		Page<Pantry> pantryList = pantryRepository.findAllById_IdUser(idUser, sortedPageable);
		return pantryList.map(x -> {
			ProductDTO p = productMapper.toDto(x.getProduct());
			p.setUnity(x.getUnity());
			return p;
		});
	}

	/**
	 * Método para obtener los datos de un producto de la despensa
	 * 
	 * @param id id del producto de la despensa del que se quieren obtener los datos
	 */
	@Override
	public ProductDTO getByIdProduct(int id) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return productMapper.toDto(product.get());
		} else {
			throw new RuntimeException("Producto no encontrado");
		}
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
			Pantry pantry = pantryRepository.findById(productKey)
					.orElse(new Pantry(new IdProductIdUser(product.getId(), idUser), 0, LocalDate.now()));
			// Si las unidades =0 establecemos establecemos 1 unidad por defecto
			if (unity == 0) {
				unity += 1;
			}
			// Inrementamos la unidades al producto de la despensa del usuario
			pantry.setUnity(pantry.getUnity() + unity);
			// Guardamos en BD
			this.pantryRepository.save(pantry);
		}
	}

	/**
	 * Método para añadir producto a la despensa desde la lista de la compra
	 * 
	 * @param id     id del producto que se añade a la despensa
	 * @param idUser id del usuario al que se le añade
	 */
	@Override
	public void addProductFromShoppingList(int id, int idUser) {
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);
		// Obtenemos la cantidad de la lista de la compra para guardar en la despensa
		ShoppingListItem shoppingListItem = shoppingListRepository.getReferenceById(productKey);
		// Buscamos el producto en la despensa
		Optional<Pantry> pantry = pantryRepository.findById(productKey);

		if (pantry.isEmpty()) {
			// Si no existe en la despensa, crearlo y guardarlo
			pantryRepository
					.save(new Pantry(new IdProductIdUser(id, idUser), shoppingListItem.getUnity(), LocalDate.now()));
		} else {
			// Si ya existe en la despensa, actualizar la cantidad añadiendole las unidades
			// de la lista de la compra
			pantry.get().setUnity(pantry.get().getUnity() + shoppingListItem.getUnity());
			pantryRepository.save(pantry.get());
		}
		shoppingListRepository.deleteById(productKey);
	}

	/**
	 * Método para actualizar producto
	 *
	 * @param productDto datos del producto a actualizar
	 */
	@Override
	public void updateProduct(ProductDTO productDto) {
		productRepository.save(productMapper.toProduct(productDto));
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
		Optional<Pantry> pantry = pantryRepository.findById(new IdProductIdUser(id, idUser));
		if (pantry.isPresent()) {
			pantry.get().setUnity(unity);
			pantryRepository.save(pantry.get());
		}
	}

	/**
	 * Método para borrar producto en la despensa
	 * 
	 * @param id     id del producto que se borra
	 * @param idUser id del usuario al que se le borra
	 */
	@Override
	public void deleteProduct(int id, int idUser) {
		pantryRepository.deleteById(new IdProductIdUser(id, idUser));
	}

	/**
	 * Método para borrar todos los productos de la despensa de un usuario
	 * específico
	 * 
	 * @param idUser id del usuario al que se le añade
	 */
	@Override
	@Transactional
	public void deleteAll(int idUser) {
		pantryRepository.deleteAllById_IdUser(idUser);
	}

}
