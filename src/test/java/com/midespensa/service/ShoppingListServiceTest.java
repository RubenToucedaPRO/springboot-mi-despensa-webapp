package com.midespensa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Sort;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.IdProductIdUser;
import com.midespensa.entities.Product;
import com.midespensa.entities.ShoppingListItem;
import com.midespensa.entities.User;
import com.midespensa.exceptions.EmailSendException;
import com.midespensa.mappers.ProductMapper;
import com.midespensa.repositories.PantryRepository;
import com.midespensa.repositories.ProductRepository;
import com.midespensa.repositories.ShoppingListRepository;
import com.midespensa.repositories.UsersRepository;
import com.midespensa.services.EmailService;
import com.midespensa.services.ShoppingListServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ShoppingListRepository shoppingListRepository;

	@Mock
	private PantryRepository aaRepository;

	@Mock
	private EmailService emailService;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ShoppingListServiceImpl shoppingListService;

	@Test
	void testGetAllByIdUser() {
		Product p1 = new Product();
		p1.setId(1);
		p1.setTitle("Producto 1");

		ShoppingListItem shoppingListItem = new ShoppingListItem();
		shoppingListItem.setId(new IdProductIdUser(1, 2));
		shoppingListItem.setProduct(p1);
		shoppingListItem.setUnity(4);

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(1);
		dto1.setTitle("Producto 1");

		Pageable pageable = PageRequest.of(0, 10);
		Page<ShoppingListItem> page = new PageImpl<>(List.of(shoppingListItem));

		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, "dateUpdate"));

		when(shoppingListRepository.findAllById_IdUser(2, sortedPageable)).thenReturn(page);
		when(productMapper.toDto(p1)).thenReturn(dto1);

		Page<ProductDTO> result = shoppingListService.getAllByIdUser(2, pageable);

		assertEquals(1, result.getContent().size());
		assertEquals("Producto 1", result.getContent().get(0).getTitle());
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
		when(shoppingListRepository.findById(new IdProductIdUser(id, idUser))).thenReturn(Optional.empty());

		shoppingListService.addProductManual(dto1, idUser);

		verify(shoppingListRepository, times(1)).save(any());
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
		when(shoppingListRepository.findById(new IdProductIdUser(id, idUser))).thenReturn(Optional.empty());

		shoppingListService.addProductManual(dto1, idUser);

		verify(shoppingListRepository, times(1)).save(any());
	}

	@Test
	void testAddProductFromPantry_WithoutProductInShoppingList() {
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

		when(shoppingListRepository.findById(productKey)).thenReturn(Optional.empty());

		// pantryRepository.save(new Pantry(new IdProductIdUser(id, idUser),
		// shoppingListItem.getUnity(), LocalDate.now()));

		shoppingListService.addProductFromPantry(id, idUser);

		verify(shoppingListRepository, times(1)).save(any());
	}

	@Test
	void testAddProductFromPantry_WithProductInShoppingList() {
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

		when(shoppingListRepository.findById(productKey)).thenReturn(Optional.of(shoppingListItem));

		shoppingListService.addProductFromPantry(id, idUser);

		verify(shoppingListRepository, times(1)).save(shoppingListItem);
		assertEquals(shoppingListItem.getUnity(), 3);
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

		ShoppingListItem shoppingListItem = new ShoppingListItem();
		shoppingListItem.setId(productKey);
		shoppingListItem.setProduct(p1);
		shoppingListItem.setUnity(2);

		when(shoppingListRepository.findById(productKey)).thenReturn(Optional.of(shoppingListItem));

		shoppingListService.updateUnity(id, unity, idUser);

		verify(shoppingListRepository, times(1)).save(shoppingListItem);
		assertEquals(shoppingListItem.getUnity(), unity);
	}

	@Test
	void testDeleteProduct() {
		int id = 999;
		int idUser = 2;

		shoppingListService.delete(id, idUser);

		verify(shoppingListRepository, times(1)).deleteById(any());
	}

	@Test
	void testDeleteAllProduct() {
		int idUser = 2;

		shoppingListService.deleteAll(idUser);

		verify(shoppingListRepository, times(1)).deleteAllById_IdUser(idUser);
	}

	@Test
	void testShoppingListSize() {
		int id = 2;

		shoppingListService.getShoppingListSize(id);

		verify(shoppingListRepository, times(1)).countById_IdUser(id);
	}

	@Test
	void testSendShoppingList() {
		int id = 2;
		int idUser = 2;
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);

		User user = new User();
		user.setId(2);
		user.setEmail("a@a.com");

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		ShoppingListItem shoppingListItem = new ShoppingListItem();
		shoppingListItem.setId(productKey);
		shoppingListItem.setProduct(p1);
		shoppingListItem.setUnity(2);

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(2);
		dto1.setBarcode("1234");
		dto1.setTitle("Producto 1");
		dto1.setUnity(2);

		ArrayList<ShoppingListItem> shoppingList = new ArrayList<ShoppingListItem>();
		shoppingList.add(shoppingListItem);

		when(shoppingListRepository.findAllById_IdUser(2)).thenReturn(shoppingList);
		when(productMapper.toDto(any())).thenReturn(dto1);
		doNothing().when(emailService).sendMailHtmlShoppingList(anyString(), anyList());

		shoppingListService.sendShoppingList(user);

		verify(emailService, times(1)).sendMailHtmlShoppingList(anyString(), anyList());

	}

	@Test
	void testSendShoppingList_Exception() {
		int id = 2;
		int idUser = 2;
		IdProductIdUser productKey = new IdProductIdUser(id, idUser);

		User user = new User();
		user.setId(2);
		user.setEmail("a@a.com");

		Product p1 = new Product();
		p1.setId(2);
		p1.setTitle("Producto 1");

		ShoppingListItem shoppingListItem = new ShoppingListItem();
		shoppingListItem.setId(productKey);
		shoppingListItem.setProduct(p1);
		shoppingListItem.setUnity(2);

		ProductDTO dto1 = new ProductDTO();
		dto1.setId(2);
		dto1.setBarcode("1234");
		dto1.setTitle("Producto 1");
		dto1.setUnity(2);

		ArrayList<ShoppingListItem> shoppingList = new ArrayList<ShoppingListItem>();
		shoppingList.add(shoppingListItem);

		when(shoppingListRepository.findAllById_IdUser(2)).thenReturn(shoppingList);
		when(productMapper.toDto(any())).thenReturn(dto1);
		doThrow(new EmailSendException("Error al enviar el correo de la lista de la compra")).when(emailService)
				.sendMailHtmlShoppingList(anyString(), anyList());

		EmailSendException exception = assertThrows(EmailSendException.class, () -> {
			shoppingListService.sendShoppingList(user);
		});

		assertEquals("Error al enviar el correo de la lista de la compra", exception.getMessage());
	}
}
