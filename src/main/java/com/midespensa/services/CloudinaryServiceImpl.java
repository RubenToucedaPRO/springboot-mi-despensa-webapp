package com.midespensa.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.midespensa.exceptions.CloudinaryException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * Servicio encargado de manejar la logica de envio de imágenes a la nube
 * Cloudinary
 */
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

	private final Cloudinary cloudinary;

	public CloudinaryServiceImpl(@Value("${CLOUDINARY_URL}") String cloudinaryUrl) {
        this.cloudinary = new Cloudinary(cloudinaryUrl);
    }

	/**
	 * Método para subir imagen a Cloudinary
	 * 
	 * @param fichero de imagen
	 * @return resultado de la operación
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map uploadFile(MultipartFile multipartFile) {

		// LLamada al metodo para covertir la imagen a un fichero temporal y
		// redimensionado
		File file = convert(multipartFile);
		Map result;
		try {
			// Subida a cloudinary
			result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
			// Eliminamos el archivo temporal
			if (!Files.deleteIfExists(file.toPath())) {
				throw new IOException("Fallo al borrar el fichero redimensionado");
			}
		} catch (IOException e) {
			throw new CloudinaryException("Fallo al subir el fichero");
		}
		return result;
	}

	private File convert(MultipartFile multipartFile) {
		// Renombramos los ficheros temporales para evitar inyección de código
		// malintencionado y repetición de archivos
		String fileName = UUID.randomUUID().toString().concat("Original.JPG");
		File file = new File(Objects.requireNonNull(fileName));
		File resizedFile = new File(UUID.randomUUID().toString().concat("Resized.JPG"));
		// Creamos un fichero físico
		try (FileOutputStream fo = new FileOutputStream(file)) {
			fo.write(multipartFile.getBytes());
		} catch (IOException e) {
			throw new CloudinaryException("Fallo al crear el fichero temporal");
		}

		try {
			// Reducimos el tamaño de la imagen
			Thumbnails.of(file).size(800, 800).keepAspectRatio(true).toFile(resizedFile);
		} catch (IOException e) {
			throw new CloudinaryException("Fallo al reducir tamaño de la foto");
		}
		try {
			// Eliminamos la imagen original
			Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			throw new CloudinaryException("Fallo al borrar el fichero original");
		}
		return resizedFile;
	}

	/**
	 * Método que elimina un archivo en Cloudinary por su ID.
	 * 
	 * @param idFile el identificador único del fichero en la nube
	 * @return resultado de la operación
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map delete(String idFile) {
		try {
			return cloudinary.uploader().destroy(idFile, ObjectUtils.emptyMap());
		} catch (IOException e) {
			throw new CloudinaryException("Fallo al borrar el fichero");
		}
	}

}
