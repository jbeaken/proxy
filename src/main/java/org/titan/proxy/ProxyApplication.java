package org.titan.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProxyApplication {

	Logger log = LoggerFactory.getLogger(ProxyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
}
