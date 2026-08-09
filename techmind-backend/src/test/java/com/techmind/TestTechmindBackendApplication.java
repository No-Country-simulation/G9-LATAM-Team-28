package com.techmind;

import org.springframework.boot.SpringApplication;

public class TestTechmindBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(TechmindBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
