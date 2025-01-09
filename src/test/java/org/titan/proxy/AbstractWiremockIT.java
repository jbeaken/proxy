package org.titan.proxy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "captain-hook.jenkins.apim.url=http://localhost:${wiremock.server.port}",
        "captain-hook.jenkins.core-engineering.url=http://localhost:${wiremock.server.port}"})
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.CLASSPATH,
        generateStubs = true,
        ids = {"org.titan.proxy:jenkins-contract-producer:${wiremock.server.port}"})
public class AbstractWiremockIT {

    @Autowired
    ProxyService proxyService;

    // Using the WireMock APIs in the normal way:
    @Test
    public void contextLoads() throws Exception {
        proxyService.githubWebhook("body", Map.of("Content-Type", "application/json"));
    }
}
