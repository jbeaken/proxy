package org.titan.proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class AppConfig {
    @Bean
    public CoreEngineeringJenkinsWebhookClient coreEngineeringJenkins(RestClient.Builder restClientBuilder) {

        RestClient webClient = restClientBuilder
                .baseUrl("http://jenkins.sandbox-ci.svc.cluster.local:8080")
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
