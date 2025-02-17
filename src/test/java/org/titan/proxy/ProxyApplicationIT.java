package org.titan.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "captain-hook.jenkins.apim.url=http://localhost:${wiremock.server.port}",
        "captain-hook.jenkins.core-engineering.url=http://localhost:${wiremock.server.port}"})
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.CLASSPATH,
        generateStubs = true,
        ids = {"org.titan.proxy:jenkins-contract-producer:stubs:${wiremock.server.port}"})
@AutoConfigureMockMvc
public class ProxyApplicationIT extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void success() throws Exception {
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/test/resources/payload.json")));

        mockMvc.perform(post("/github-webhook/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonContent)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void error() throws Exception {
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/test/resources/payload-error.json")));

        mockMvc.perform(post("/github-webhook/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonContent)
                        .content(jsonContent))
                .andExpect(status().is5xxServerError());
    }
}
