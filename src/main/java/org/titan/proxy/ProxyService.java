package org.titan.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class ProxyService {

    final Logger log = LoggerFactory.getLogger(ProxyService.class);

    private final CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient;
    private final ApimJenkinsWebhookClient apimJenkinsWebhookClient;

    ProxyService(CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient, ApimJenkinsWebhookClient apimJenkinsWebhookClient) {
        this.coreEngineeringJenkinsWebhookClient = coreEngineeringJenkinsWebhookClient;
        this.apimJenkinsWebhookClient = apimJenkinsWebhookClient;
    }

    void githubWebhook(final String body, final Map<String, String> headers) {

        log.debug("headers: {}", headers);
        log.debug("request body {}", body);

        String ceResult = coreEngineeringJenkinsWebhookClient.sendWebhook(body, headers);
        String apimResult = apimJenkinsWebhookClient.sendWebhook(body, headers);

        log.info("ceResult: {}", ceResult);
        log.info("apimResult {}", apimResult);
    }
}
