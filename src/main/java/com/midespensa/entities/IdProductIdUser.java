package com.midespensa.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IdProductIdUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	@Column(name = "id_user")
	private int idUser;

	// equals y hashCode (obligatorio para claves compuestas)
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		IdProductIdUser that = (IdProductIdUser) o;
		return idUser == that.idUser && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idUser);
	}
}
