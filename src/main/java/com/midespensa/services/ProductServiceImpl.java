package com.midespensa.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.Barcode;
import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.Pantry;
import com.midespensa.entities.Product;
import com.midespensa.entities.ShoppingListItem;
import com.midespensa.exceptions.ProductNotFoundException;
import com.midespensa.exceptions.ProductUpdateException;
import com.midespensa.mappers.ProductMapper;
import com.midespensa.repositories.OpenFoodFactsRepository;
import com.midespensa.repositories.PantryRepository;
import com.midespensa.repositories.ProductRepository;
import com.midespensa.repositories.ShoppingListRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de manejar la lógica de productos en la aplicación
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final PantryRepository pantryRepository;
	private final ShoppingListRepository shoppingListRepository;
	private final OpenFoodFactsRepository openFoodFactsRepository;
	private final ProductMapper productMapper;

	private final static String SIN_IDENTIFICAR = "Sin nombre";

	/**
	 * Método obtener lista de productos para el administrador
	 * 
	 * @param pageable parámetros de paginación
	 * @return lista de productos paginados
	 */
	@Override
	public Page<ProductDTO> getAllProducts(Pageable pageable) {
		Page<Product> listP = productRepository.findAll(pageable);
		return listP.map(productMapper::toDto);
	}

	/**
	 * Método obtener un producto
	 * 
	 * @param id id del producto
	 * @return dto del producto
	 */
	@Override
	public ProductDTO getById(int id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Id producto no valido:" + id));
		return productMapper.toDto(product);
	}

	/**
	 * Método obtener lista de productos que no sean de un usuario
	 * 
	 * @param idUser   id del usuario del cual descartar sus productos
	 * @param pageable parámetros de paginación
	 * @return lista de productos paginados
	 */
	@Override
	public Page<ProductDTO> getAllProductsNotIdUser(Pageable pageable, int idUser) {
		Page<Product> listP = productRepository.findByIdUserNot(pageable, idUser);
		return listP.map(productMapper::toDto);
	}

	/**
	 * Método para añadir producto a la BD
	 * 
	 * @param productDto datos del producto
	 */
	@Override
	public void addProduct(ProductDTO productDto) {
		// Verificamos que tiene titulo y o existe el el código de barras del producto
		// como público
		if (!productDto.getTitle().isEmpty()
				&& !productRepository.existsByBarcodeAndIdUser(productDto.getBarcode(), 1)) {
			productDto.setDateUpdate(LocalDate.now());
			this.productRepository.save(productMapper.toProduct(productDto));
		}
	}

	/**
	 * Método para añadir producto a la despensa con scanner de codigo de barras
	 * 
	 * @param barcode código de barras obtido co scanner
	 * @param idUser  id do usuario que realizou a lectura
	 */
	@Override
	public ProductDTO addProductFromPantry(String barcode, int idUser) {
		// Comprobamos si existe el producto para el código leido
		ProductDTO productDto = getByBarcode(barcode, true, idUser);
		// Verificamos si existe el producto en BD, si el titulo no está sin identificar
		// es que existe
		if (!productDto.getTitle().equals(SIN_IDENTIFICAR)) {
			// Si el usuario tiene ese producto en su despensa incrementamos las unidades en
			// 1
			if (pantryRepository.existsById(new IdProductIdUser(productDto.getId(), idUser))) {
				Pantry pantryP = pantryRepository.getReferenceById(new IdProductIdUser(productDto.getId(), idUser));
				pantryP.setUnity(pantryP.getUnity() + 1);
				pantryRepository.save(pantryP);
			} else {
				// Si no tiene ese producto en su despensa
				// Obtenemos los datos del producto para el usuario especifico(personalizado)
				Optional<Product> product = productRepository.findByBarcodeAndIdUser(barcode, idUser);
				if (product.isEmpty()) {
					// Si no se obtuvieron para el usuario especifico los obtenemos del producto
					// público
					product = productRepository.findByBarcodeAndIdUser(barcode, 1);
				}
				// Si existe lo guardamos en la despensa del usuario con 1 unidad
				if (product.isPresent()) {
					pantryRepository
							.save(new Pantry(new IdProductIdUser(product.get().getId(), idUser), 1, LocalDate.now()));
				}
			}
		}
		return productDto;
	}

	@Override
	public ProductDTO addProductFromShoppingList(String barcode, int idUser) {
		// Comprobamos si existe el producto para el código leido
		ProductDTO productDTO = getByBarcode(barcode, true, idUser);
		// Verificamos si existe el producto en BD, si el titulo no está sin identificar
		// es que existe
		if (!productDTO.getTitle().equals(SIN_IDENTIFICAR)) {
			// Si el usuario tiene ese producto en su despensa incrementamos las unidades en
			// 1
			if (shoppingListRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))) {
				ShoppingListItem shoppingListItem = shoppingListRepository
						.getReferenceById(new IdProductIdUser(productDTO.getId(), idUser));
				shoppingListItem.setUnity(shoppingListItem.getUnity() + 1);
				shoppingListRepository.save(shoppingListItem);
			} else {
				// Obtenemos los datos del producto para el usuario especifico
				Optional<Product> product = productRepository.findByBarcodeAndIdUser(barcode, idUser);
				if (product.isEmpty()) {
					// Si no se obtuvieron para el usuario especifico los obtenemos del producto
					// público
					product = productRepository.findByBarcodeAndIdUser(barcode, 1);
				}
				// Si existe lo guardamos en la despensa del usuario con 1 unidad
				if (product.isPresent()) {
					shoppingListRepository.save(new ShoppingListItem(new IdProductIdUser(product.get().getId(), idUser),
							1, LocalDate.now()));
				}
			}
		}
		return productDTO;
	}

	/**
	 * Método para pbtener los datos de un producto
	 *
	 * @param code       código de barras del producto
	 * @param pantryOrSl boolean para indicar que se está relaizando la obtención
	 *                   desde despensa o lista de la compra. En caso de ser desde
	 *                   productos del admin no se añade directamente para que el
	 *                   admin compruebe en el formulario los datos
	 * @param idUser     id del usuario al que pertenece el producto
	 */
	@Override
	public ProductDTO getByBarcode(String code, boolean pantryOrSL, int idUser) {
		boolean productAPI = false;
		// Buscamos el producto de la BD customizado para ese usuario
		Optional<Product> product = productRepository.findByBarcodeAndIdUser(code, idUser);

		// Si no se obtuvo producto
		if (product.isEmpty()) {
			// Buscamos el producto publico en la BD
			product = productRepository.findByBarcodeAndIdUser(code, 1);
		}
		// Si sigue sin obtenerse
		if (product.isEmpty()) {
			// Se solicita datos producto escaneado a OpenFoodFacts
			Optional<Barcode> barcode = openFoodFactsRepository.getProduct(code);
			if (barcode.isPresent() && barcode.get().getTitle() != null) {
				product = Optional.of(productMapper.toProduct(barcode.get()));
				productAPI = true;
			}
		}
		// Si siguen sin obtenerse se indica en el title que es "sin identificar"
		if (product.isEmpty()) {
			product = Optional.of(new Product());
			product.get().setBarcode(code);
			product.get().setTitle(SIN_IDENTIFICAR);
		}
		// Se mapea al DTO
		ProductDTO productDTO = productMapper.toDto(product.get());

		productDTO.setIdUser(idUser);
		// Si productAPI=true es porque fue recogido de OpenFoodsFacts
		// Si la lectura no fue desde productos lo añadimos directamente
		if (productAPI && pantryOrSL) {
			// Si setea idUser=1 para indicar que es un producto publico que será utilizable
			// por todos
			productDTO.setIdUser(1);
			addProduct(productDTO);
		}
		return productDTO;

	}

	/**
	 * Método para actualizar producto
	 *
	 * @param productDto datos del producto a actualizar
	 */
	@Override
	public void updateProduct(ProductDTO productDto) {
		try {
			// Si el producto existe en BD con idUser!=1 entonces si admin
			// convierte el producto en publico cambiando el idUser a 1, se crea un nuevo
			// producto manteniendo el personalizado del usuario eliminando el id del
			// producto antes de guardarlo en BD
			if (productDto.getIdUser() == 1 && productRepository.findById(productDto.getId()).get().getIdUser() != 1) {
				productDto.setId(0);
			}
			// Actualizamos la fecha de actualización
			productDto.setDateUpdate(LocalDate.now());
			// Guardamos el producto en base de datos
			productRepository.save(productMapper.toProduct(productDto));
		} catch (DataAccessException ex) {
			throw new ProductUpdateException("No se pudo actualizar el producto.", ex);
		}
	}

	/**
	 * Método para borrar producto en la BD
	 * 
	 * @param id id del producto que se borra
	 * @return boolean confirmando borrado
	 */
	@Override
	public boolean deleteProduct(int id) {
		// Solo permite borrarlo si no está siendo usado
		if (!pantryRepository.existsById_Id(id) && !shoppingListRepository.existsById_Id(id)) {
			productRepository.deleteById(id);
			return true;
		}
		return false;
	}

	/**
	 * Método para actualizar los productos de la BD según la info obtenida de estos
	 * de la API externa
	 * 
	 */
	@Override
	public void updateProductsFromOpenFoodFacts() {
		List<Product> listP = productRepository.findAll();
		for (Product product : listP) {
			Optional<Barcode> barcode = openFoodFactsRepository.getProduct(product.getBarcode());
			if (barcode.isPresent() && barcode.get().getTitle() != null) {
				product.setAmount(barcode.get().getAmount());
				product.setBrand(barcode.get().getBrand());
				product.setImage(barcode.get().getImage());
				productRepository.save(product);
			}
		}
	}

}
