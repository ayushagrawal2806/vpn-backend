package com.vpn.vpn_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class VpnBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpnBackendApplication.class, args);
	}

}
