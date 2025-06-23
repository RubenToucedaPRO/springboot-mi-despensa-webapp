package com.midespensa.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shopping_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListItem {

	@EmbeddedId
	private IdProductIdUser id;

	@Column(name = "unity")
	int unity;

	@Column(name = "date_update")
	private LocalDate dateUpdate;

	@ManyToOne
	@JoinColumn(name = "id", insertable = false, updatable = false) // Referencia a Product
	private Product product;

	public ShoppingListItem(IdProductIdUser id, int unity, LocalDate dateUpdate) {
		this.id = id;
		this.unity = unity;
		this.dateUpdate = dateUpdate;
	}
}
