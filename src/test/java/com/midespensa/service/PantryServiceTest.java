package com.midespensa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Sort;

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
import com.midespensa.services.PantryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PantryServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private PantryRepository pantryRepository;

	@Mock
	private ShoppingListRepository shoppingListRepository;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private PantryServiceImpl pantryService;

	@Test
	void testGetAllByIdUser() {
		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");

		Pantry pa1 = new Pantry();
		pa1.setId(new IdProductIdUser(1, 2));
		pa1.setProduct(p1);
		pa1.setUnity(4);

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(1);
		dto1.setTitle("Producto 1");

		Pageable pageable = PageRequest.of(0, 10);
		Page<Pantry> page = new PageImpl<>(List.of(pa1));

		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, "dateUpdate"));

		when(pantryRepository.findAllById_IdUser(2, sortedPageable)).thenReturn(page);
		when(productMapper.toDto(p1)).thenReturn(dto1);

		Page<ProductDTO> result = pantryService.getAllByIdUser(2, pageable);

		assertEquals(1, result.getContent().size());
		assertEquals("Producto 1", result.getContent().get(0).getTitle());
	}

	@Test
	void testGetByIdProduct_Exist() {
		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(1);
		dto1.setTitle("Producto 1");

		when(productRepository.findById(1)).thenReturn(Optional.of(p1));
		when(productMapper.toDto(p1)).thenReturn(dto1);

		ProductDTO result = pantryService.getByIdProduct(1);

		assertEquals(result.getId(), result.getId());
		assertEquals("Producto 1", result.getTitle());
		assertEquals(result.getTitle(), result.getTitle());
		verify(productRepository, times(1)).findById(1);
		verify(productMapper, times(1)).toDto(p1);
	}

	@Test
	void testGetByIdProduct_Exception() {
		int id = 1;

		when(productRepository.findById(id)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			pantryService.getByIdProduct(id);
		});

		assertEquals("Producto no encontrado", exception.getMessage());
	}

	@Test
	void testAddProductManual_withUnities() {
		int id = 2;
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(2);
		dto1.setBarcode("1234");
		dto1.setTitle("Producto 1");
		dto1.setUnity(2);

		when(productRepository.findByBarcodeAndIdUser(dto1.getBarcode(), idUser)).thenReturn(Optional.empty())
				.thenReturn(Optional.empty());
		when(productMapper.toProduct(dto1)).thenReturn(p1);
		when(productRepository.save(p1)).thenReturn(p1);
		when(pantryRepository.findById(new IdProductIdUser(id, idUser))).thenReturn(Optional.empty());

		pantryService.addProductManual(dto1, idUser);

		verify(pantryRepository, times(1)).save(any());
	}

	@Test
	void testAddProductManual_withoutUnity() {
		int id = 2;
		int idUser = 2;

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(2);
		dto1.setBarcode("1234");
		dto1.setTitle("Producto 1");
		dto1.setUnity(0);

		when(productRepository.findByBarcodeAndIdUser(dto1.getBarcode(), idUser)).thenReturn(Optional.empty())
				.thenReturn(Optional.empty());
		when(productMapper.toProduct(dto1)).thenReturn(p1);
		when(productRepository.save(p1)).thenReturn(p1);
		when(pantryRepository.findById(new IdProductIdUser(id, idUser))).thenReturn(Optional.empty());

		pantryService.addProductManual(dto1, idUser);

		verify(pantryRepository, times(1)).save(any());
	}

	@Test
	void testAddProductFromShoppingList_WithoutProductInPantry() {
		int id = 2;
		int idUser = 2;
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		ShoppingListItem shoppingListItem = new ShoppingListItem();
		shoppingListItem.setId(productKey);
		shoppingListItem.setProduct(p1);
		shoppingListItem.setUnity(2);

		when(shoppingListRepository.getReferenceById(productKey)).thenReturn(shoppingListItem);
		when(pantryRepository.findById(productKey)).thenReturn(Optional.empty());

		// pantryRepository.save(new Pantry(new IdProductIdUser(id, idUser),
		// shoppingListItem.getUnity(), LocalDate.now()));

		pantryService.addProductFromShoppingList(id, idUser);

		verify(pantryRepository, times(1)).save(any());
		verify(shoppingListRepository, times(1)).deleteById(productKey);
	}

	@Test
	void testAddProductFromShoppingList_WithProductInPantry() {
		int id = 2;
		int idUser = 2;
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		ShoppingListItem shoppingListItem = new ShoppingListItem();
		shoppingListItem.setId(productKey);
		shoppingListItem.setProduct(p1);
		shoppingListItem.setUnity(2);

		Pantry pantry = new Pantry();
		pantry.setId(productKey);
		pantry.setProduct(p1);
		pantry.setUnity(2);

		when(shoppingListRepository.getReferenceById(productKey)).thenReturn(shoppingListItem);
		when(pantryRepository.findById(productKey)).thenReturn(Optional.of(pantry));

		pantryService.addProductFromShoppingList(id, idUser);

		verify(pantryRepository, times(1)).save(pantry);
		assertEquals(pantry.getUnity(), 4);
		verify(shoppingListRepository, times(1)).deleteById(productKey);
	}

	@Test
	void updateProduct() {
		ProductDTO dto1 = new ProductDTO();
		dto1.setId(2);
		dto1.setBarcode("1234");
		dto1.setTitle("Producto 1");
		dto1.setUnity(2);

		pantryService.updateProduct(dto1);

		verify(productRepository, times(1)).save(any());
	}

	@Test
	void testUpdateUnity() {
		int unity = 4;
		int id = 2;
		int idUser = 2;
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		Pantry pantry = new Pantry();
		pantry.setId(productKey);
		pantry.setProduct(p1);
		pantry.setUnity(2);

		when(pantryRepository.findById(productKey)).thenReturn(Optional.of(pantry));

		pantryService.updateUnity(id, unity, idUser);

		verify(pantryRepository, times(1)).save(pantry);
		assertEquals(pantry.getUnity(), unity);
	}

	@Test
	void testDeleteProduct() {
		int id = 999;
		int idUser = 2;

		pantryService.deleteProduct(id, idUser);

		verify(pantryRepository, times(1)).deleteById(any());
	}

	@Test
	void testDeleteAllProduct() {
		int idUser = 2;

		pantryService.deleteAll(idUser);

		verify(pantryRepository, times(1)).deleteAllById_IdUser(idUser);
	}
}
