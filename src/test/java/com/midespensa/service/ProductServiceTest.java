package com.midespensa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.Barcode;
import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.Pantry;
import com.midespensa.entities.Product;
import com.midespensa.entities.ShoppingListItem;
import com.midespensa.exceptions.ProductNotFoundException;
import com.midespensa.mappers.ProductMapper;
import com.midespensa.repositories.OpenFoodFactsRepository;
import com.midespensa.repositories.PantryRepository;
import com.midespensa.repositories.ProductRepository;
import com.midespensa.repositories.ShoppingListRepository;
import com.midespensa.services.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private PantryRepository pantryRepository;

	@Mock
	private ShoppingListRepository shoppingListRepository;

	@Mock
	private OpenFoodFactsRepository openFoodFactsRepository;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	void testGetAllProducts() {
		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(1);
		dto1.setTitle("Producto 1");

		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> page = new PageImpl<>(List.of(p1));

		when(productRepository.findAll(pageable)).thenReturn(page);
		when(productMapper.toDto(p1)).thenReturn(dto1);

		Page<ProductDTO> result = productService.getAllProducts(pageable);

		assertEquals(1, result.getContent().size());
		assertEquals("Producto 1", result.getContent().get(0).getTitle());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testGetById_existingId_returnsProductDTO() {
		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");

		when(productRepository.findById(1)).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);

		ProductDTO result = productService.getById(1);

		assertEquals("Producto 1", result.getTitle());
		assertEquals(1, result.getId());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testGetById_nonExistingId_throwsException() {
		when(productRepository.findById(999)).thenReturn(Optional.empty());

		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
			productService.getById(999);
		});

		assertEquals("Id producto no valido:999", exception.getMessage());
	}

	@Test
	void testGetgetAllProductsNotIdUser() {
		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");

		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> page = new PageImpl<>(List.of(p1));

		when(productRepository.findByIdUserNot(pageable, 1)).thenReturn(page);
		when(productMapper.toDto(p1)).thenReturn(productDTO);

		Page<ProductDTO> result = productService.getAllProductsNotIdUser(pageable, 1);

		assertEquals("Producto 1", result.getContent().get(0).getTitle());
		assertEquals(1, result.getContent().get(0).getId());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testAddProduct_DontSaveIfExist() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		when(productRepository.existsByBarcodeAndIdUser("1234", 1)).thenReturn(true);

		productService.addProduct(productDTO);

		verify(productRepository, never()).save(any());
	}

	@Test
	void testAddProduct_DontSaveProductWithoutTitle() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setTitle("");
		productDTO.setBarcode("1234");

		productService.addProduct(productDTO);

		verify(productRepository, never()).save(any());
	}

	@Test
	void testAddProduct_Save() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		when(productRepository.existsByBarcodeAndIdUser("1234", 1)).thenReturn(false);

		productService.addProduct(productDTO);

		verify(productRepository, times(1)).save(any());
	}

	@Test
	void testAddProductFromPantry_WithTitle() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		Pantry pantry = new Pantry(new IdProductIdUser(1, idUser), idUser, LocalDate.now());
		pantry.setUnity(1);

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);
		when(pantryRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(true);
		when(pantryRepository.getReferenceById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(pantry);

		productService.addProductFromPantry(barcode, idUser);
		assertEquals(2, pantry.getUnity());
		verify(pantryRepository, times(1)).save(any());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testAddProductFromPantry_WithoutTitle() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Sin nombre");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Sin nombre");
		productDTO.setBarcode("1234");

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.empty());
		when(productRepository.findByBarcodeAndIdUser(barcode, 1)).thenReturn(Optional.empty());
		when(openFoodFactsRepository.getProduct(barcode)).thenReturn(Optional.empty());
		when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

		productService.addProductFromPantry(barcode, idUser);
		verify(pantryRepository, times(0)).save(any());
		verify(productMapper, times(0)).toDto(p1);
	}

	@Test
	void testAddProductFromPantry_NewProductToPantry() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		Pantry pantry = new Pantry(new IdProductIdUser(1, idUser), idUser, LocalDate.now());
		pantry.setUnity(1);

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);
		when(pantryRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(false);

		productService.addProductFromPantry(barcode, idUser);
		assertEquals(1, pantry.getUnity());
		verify(pantryRepository, times(1)).save(any());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testAddProductFromPantry_NewProducToPantryWithoutSave() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		Pantry pantry = new Pantry(new IdProductIdUser(1, idUser), idUser, LocalDate.now());
		pantry.setUnity(1);

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.of(p1))
				.thenReturn(Optional.empty()).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);
		when(pantryRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(false);

		productService.addProductFromPantry(barcode, idUser);
		assertEquals(1, pantry.getUnity());
		verify(pantryRepository, times(0)).save(any());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testAddProductFromShoppingList_WithTitle() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		ShoppingListItem shoppingList = new ShoppingListItem(new IdProductIdUser(1, idUser), idUser, LocalDate.now());
		shoppingList.setUnity(1);

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);
		when(shoppingListRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(true);
		when(shoppingListRepository.getReferenceById(new IdProductIdUser(productDTO.getId(), idUser)))
				.thenReturn(shoppingList);

		productService.addProductFromShoppingList(barcode, idUser);
		assertEquals(2, shoppingList.getUnity());
		verify(shoppingListRepository, times(1)).save(any());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testAddProductFromShoppingList_WithoutTitle() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Sin nombre");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Sin nombre");
		productDTO.setBarcode("1234");

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.empty());
		when(productRepository.findByBarcodeAndIdUser(barcode, 1)).thenReturn(Optional.empty());
		when(openFoodFactsRepository.getProduct(barcode)).thenReturn(Optional.empty());
		when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

		productService.addProductFromShoppingList(barcode, idUser);
		verify(shoppingListRepository, times(0)).save(any());
		verify(productMapper, times(0)).toDto(p1);
	}

	@Test
	void testAddProductFromShoppingList_NewProductToShoppingList() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		ShoppingListItem shoppingList = new ShoppingListItem(new IdProductIdUser(1, idUser), idUser, LocalDate.now());
		shoppingList.setUnity(1);

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);
		when(shoppingListRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(false);

		productService.addProductFromShoppingList(barcode, idUser);
		assertEquals(1, shoppingList.getUnity());
		verify(shoppingListRepository, times(1)).save(any());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testAddProductFromShoppingList_NewProducToShoppingListWithoutSave() {
		String barcode = "1234";
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");

		ShoppingListItem shoppingList = new ShoppingListItem(new IdProductIdUser(1, idUser), idUser, LocalDate.now());
		shoppingList.setUnity(1);

		when(productRepository.findByBarcodeAndIdUser(barcode, idUser)).thenReturn(Optional.of(p1))
				.thenReturn(Optional.empty()).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(productDTO);
		when(shoppingListRepository.existsById(new IdProductIdUser(productDTO.getId(), idUser))).thenReturn(false);

		productService.addProductFromShoppingList(barcode, idUser);
		assertEquals(1, shoppingList.getUnity());
		verify(shoppingListRepository, times(0)).save(any());
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testUpdateProduct() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setTitle("Producto 1");
		productDTO.setBarcode("1234");
		productDTO.setIdUser(1);

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		when(productRepository.findById(productDTO.getId())).thenReturn(Optional.of(p1));
		when(productMapper.toProduct(productDTO)).thenReturn(p1);

		productService.updateProduct(productDTO);

		verify(productRepository, times(1)).save(any());
		verify(productMapper, times(0)).toDto(p1);
	}

	@Test
	void testDeleteProduct_True() {
		when(pantryRepository.existsById_Id(1)).thenReturn(false);
		when(shoppingListRepository.existsById_Id(1)).thenReturn(false);

		boolean result = productService.deleteProduct(1);

		assertTrue(result);
		verify(productRepository, times(1)).deleteById(1);
	}

	@Test
	void testDeleteProduct_False() {
		when(pantryRepository.existsById_Id(1)).thenReturn(true);
		// when(shoppingListRepository.existsById_Id(1)).thenReturn(false);

		boolean result = productService.deleteProduct(1);

		assertTrue(!result);
		verify(productRepository, times(0)).deleteById(1);
	}

	@Test
	void testUpdateProductsFromOpenFoodFacts() {
		List<Product> listP = new ArrayList<Product>();

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");
		p1.setBarcode("1234");

		Product p2 = new Product();
		p2.setId(2);
		p2.setTitle("Producto 2");
		p2.setBarcode("5678");

		listP.add(p1);
		listP.add(p2);

		Barcode b1 = new Barcode();
		b1.setTitle("p1");
		b1.setBarcode("1234");

		Barcode b2 = new Barcode();
		b2.setTitle("p1");
		b2.setBarcode("1234");

		when(productRepository.findAll()).thenReturn(listP);
		when(openFoodFactsRepository.getProduct(any())).thenReturn(Optional.of(b1)).thenReturn(Optional.of(b2));

		productService.updateProductsFromOpenFoodFacts();

		verify(productRepository, times(2)).save(any());
	}
}