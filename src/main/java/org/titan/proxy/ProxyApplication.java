package org.titan.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
public class ProxyApplication {

	Logger log = LoggerFactory.getLogger(ProxyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@RestController
	class ProxyController {
		private final JenkinsWebhookClient jenkinsWebhookClient;

        ProxyController(JenkinsWebhookClient jenkinsWebhookClient) {
            this.jenkinsWebhookClient = jenkinsWebhookClient;
        }

        @PostMapping("/proxy")
		void post(@RequestBody String body, HttpServletRequest request) {
			ArrayList<String> headerNames = Collections.list(request.getHeaderNames());
			log.info(request.toString());
			log.info(request.getRequestURI());
			log.info("headerNames: {}", headerNames);
			log.info("request body {}", body);

			jenkinsWebhookClient.sendWebhook(body);
		}

		@PostMapping("/jenkins")
		void jenkins(@RequestBody String body, HttpServletRequest request) {
			ArrayList<String> headerNames = Collections.list(request.getHeaderNames());
			log.info(request.toString());
			log.info(request.getRequestURI());
			log.info("headerNames: {}", headerNames);
			log.info("request body {}", body);
			
			jenkinsWebhookClient.sendWebhook(body);
		}
	}

	@Configuration
    static class AppConfig {
		@Bean
		public JenkinsWebhookClient kongAdminApiClient(RestClient.Builder restClientBuilder) {

			RestClient webClient = restClientBuilder
					.baseUrl("https://jenkins.sandbox.kong-nonprod.cortex.elsevier.systems")
					.defaultHeader("test", "test")
					.build();

			HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
					.builderFor(RestClientAdapter.create(webClient))
					.build();

			return httpServiceProxyFactory.createClient(JenkinsWebhookClient.class);
		}
	}

	public interface JenkinsWebhookClient {
		@PostExchange(url = "/github-webhook", contentType = MediaType.APPLICATION_JSON_VALUE)
		String sendWebhook(@RequestBody String requestBody);
	}
}
