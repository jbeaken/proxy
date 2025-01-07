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

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class ProxyApplication {

	Logger log = LoggerFactory.getLogger(ProxyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@RestController
	class ProxyController {
		private final CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient;
		private final ApimJenkinsWebhookClient apimJenkinsWebhookClient;

        ProxyController(CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient, ApimJenkinsWebhookClient apimJenkinsWebhookClient) {
            this.coreEngineeringJenkinsWebhookClient = coreEngineeringJenkinsWebhookClient;
            this.apimJenkinsWebhookClient = apimJenkinsWebhookClient;
        }

		@PostMapping("/github-webhook/")
		void githubWebhook(@RequestBody String body, HttpServletRequest request) {
			Map<String, String> headers = Collections.list(request.getHeaderNames())
					.stream()
					.filter(headerName -> !headerName.equals("content-length"))
					.collect(Collectors.toMap(h -> h, request::getHeader));

			log.info(request.toString());
			log.info(request.getRequestURI());
			log.info("headers: {}", headers);
			log.info("request body {}", body);

//			apimJenkinsWebhookClient.sendWebhook(body);
			apimJenkinsWebhookClient.sendWebhook(body, headers);
		}

        @PostMapping("/apim")
		void apim(@RequestBody String body, HttpServletRequest request) {
			Map<String, String> headers = Collections.list(request.getHeaderNames())
					.stream()
					.filter(headerName -> !headerName.equals("content-length"))
					.collect(Collectors.toMap(h -> h, request::getHeader));

			log.info(request.toString());
			log.info(request.getRequestURI());
			log.info("headers: {}", headers);
			log.info("request body {}", body);

//			apimJenkinsWebhookClient.sendWebhook(body);
			apimJenkinsWebhookClient.sendWebhook(body, headers);
		}

		@PostMapping("/core")
		void core(@RequestBody String body, HttpServletRequest request) {
			Map<String, String> headers = Collections.list(request.getHeaderNames())
					.stream()
					.collect(Collectors.toMap(h -> h, request::getHeader));

			log.info(request.toString());
			log.info(request.getRequestURI());
			log.info("headers: {}", headers);
			log.info("request body {}", body);
			
			coreEngineeringJenkinsWebhookClient.sendWebhook(body, headers);
		}
	}

	@Configuration
    static class AppConfig {
		@Bean
		public CoreEngineeringJenkinsWebhookClient coreEngineeringJenkins(RestClient.Builder restClientBuilder) {

			RestClient webClient = restClientBuilder
					.baseUrl("https://jenkins.sandbox.kong-nonprod.cortex.elsevier.systems")
					.build();

			HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
					.builderFor(RestClientAdapter.create(webClient))
					.build();

			return httpServiceProxyFactory.createClient(CoreEngineeringJenkinsWebhookClient.class);
		}

		@Bean
		public ApimJenkinsWebhookClient apimJenkins(RestClient.Builder restClientBuilder) {

			RestClient webClient = restClientBuilder
					.baseUrl("http://dev-jenkins.dev-jenkins.svc.cluster.local:8080")
					.build();

			HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
					.builderFor(RestClientAdapter.create(webClient))
					.build();

			return httpServiceProxyFactory.createClient(ApimJenkinsWebhookClient.class);
		}
	}

	public interface CoreEngineeringJenkinsWebhookClient {
		@PostExchange(url = "/github-webhook/")
		String sendWebhook(@RequestBody String requestBody, @RequestHeader Map<String, String> headers);
	}

	public interface ApimJenkinsWebhookClient {
		@PostExchange(url = "/github-webhook/")
		String sendWebhook(@RequestBody String requestBody, @RequestHeader Map<String, String> headers);
//		String sendWebhook(@RequestBody String requestBody);
	}
}
