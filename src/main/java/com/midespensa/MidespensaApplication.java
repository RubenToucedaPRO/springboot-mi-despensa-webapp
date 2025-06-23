package com.midespensa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
public class MidespensaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidespensaApplication.class, args);
	}

}
