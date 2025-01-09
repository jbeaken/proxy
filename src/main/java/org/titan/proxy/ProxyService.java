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

        try {
           coreEngineeringJenkinsWebhookClient.sendWebhook(body, headers);
        } catch (Exception e) {
            throw new ProxyException(e.getMessage());
        }

        try {
            apimJenkinsWebhookClient.sendWebhook(body, headers);
        } catch (Exception e) {
            throw new ProxyException(e.getMessage());
        }
    }
}
