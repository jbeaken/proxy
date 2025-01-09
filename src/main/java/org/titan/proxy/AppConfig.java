package org.titan.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class AppConfig {

    private final String apimJenkinsWebhookUrl;
    private final String coreEngineeringJenkinsWebhookUrl;

    AppConfig(@Value("${captain-hook.jenkins.apim.url}") final String apimJenkinsWebhookUrl,
              @Value("${captain-hook.jenkins.core-engineering.url}") final String coreEngineeringJenkinsWebhookUrl) {
        this.apimJenkinsWebhookUrl = apimJenkinsWebhookUrl;
        this.coreEngineeringJenkinsWebhookUrl = coreEngineeringJenkinsWebhookUrl;
    }

    @Bean
    public CoreEngineeringJenkinsWebhookClient coreEngineeringJenkins(RestClient.Builder restClientBuilder) {

        RestClient webClient = restClientBuilder
                .baseUrl(coreEngineeringJenkinsWebhookUrl)
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(CoreEngineeringJenkinsWebhookClient.class);
    }

    @Bean
    public ApimJenkinsWebhookClient apimJenkins(RestClient.Builder restClientBuilder) {

        RestClient webClient = restClientBuilder
                .baseUrl(apimJenkinsWebhookUrl)
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(ApimJenkinsWebhookClient.class);
    }
}
