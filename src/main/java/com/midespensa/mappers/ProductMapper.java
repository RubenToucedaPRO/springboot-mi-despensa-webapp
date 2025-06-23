package com.midespensa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.midespensa.dtos.ProductDTO;
import com.midespensa.entities.Barcode;
import com.midespensa.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	Barcode ToBarcode(Product product);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idUser", ignore = true)
	@Mapping(target = "dateUpdate", ignore = true)
	@Mapping(target = "pantries", ignore = true)
	Product toProduct(Barcode barcode);

	@Mapping(target = "unity", ignore = true)
	ProductDTO toDto(Product product);

	@Mapping(target = "pantries", ignore = true)
	Product toProduct(ProductDTO productDTO);
}
