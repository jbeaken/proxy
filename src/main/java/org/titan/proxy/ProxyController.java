package org.titan.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class ProxyController {
    private final ProxyApplication proxyApplication;
    private final CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient;
    private final ApimJenkinsWebhookClient apimJenkinsWebhookClient;

    ProxyController(ProxyApplication proxyApplication, CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient, ApimJenkinsWebhookClient apimJenkinsWebhookClient) {
        this.proxyApplication = proxyApplication;
        this.coreEngineeringJenkinsWebhookClient = coreEngineeringJenkinsWebhookClient;
        this.apimJenkinsWebhookClient = apimJenkinsWebhookClient;
    }

    @PostMapping("/github-webhook/")
    void githubWebhook(@RequestBody String body, HttpServletRequest request) {
        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .filter(headerName -> !headerName.equals("content-length"))
                .collect(Collectors.toMap(h -> h, request::getHeader));

        proxyApplication.log.info(request.toString());
        proxyApplication.log.info(request.getRequestURI());
        proxyApplication.log.debug("headers: {}", headers);
        proxyApplication.log.debug("request body {}", body);

        String ceResult = coreEngineeringJenkinsWebhookClient.sendWebhook(body, headers);
        String apimResult = apimJenkinsWebhookClient.sendWebhook(body, headers);

        proxyApplication.log.info("ceResult: {}", ceResult);
        proxyApplication.log.info("apimResult {}", apimResult);
    }
}
