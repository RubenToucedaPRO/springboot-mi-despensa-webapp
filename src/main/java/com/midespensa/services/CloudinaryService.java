package com.midespensa.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

	@SuppressWarnings("rawtypes")
	Map uploadFile(MultipartFile multipartFile);

	@SuppressWarnings("rawtypes")
	Map delete(String nameFile);
}
