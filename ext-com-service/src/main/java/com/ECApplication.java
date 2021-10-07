package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.io.IOException;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ECApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ECApplication.class, args);
	}

}
