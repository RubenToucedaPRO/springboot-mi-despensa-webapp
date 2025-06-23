package com.midespensa.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	private int id;

	private int idUser;

	private String barcode;

	private String title;

	private String brand;

	private String amount;

	private String image;

	private LocalDate dateUpdate;

	private int unity;

	public ProductDTO(int id) {
		this.id = id;
	}

	public ProductDTO(int id, String barcode, int idUser, String title, String brand, String amount) {
		super();
		this.id = id;
		this.barcode = barcode;
		this.idUser = idUser;
		this.title = title;
		this.brand = brand;
		this.amount = amount;
	}

}
