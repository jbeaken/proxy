package org.titan.proxy;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

public interface CoreEngineeringJenkinsWebhookClient {
    @PostExchange(url = "/github-webhook/")
    String sendWebhook(@RequestBody String requestBody, @RequestHeader Map<String, String> headers);
}
