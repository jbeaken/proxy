package org.titan.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class ProxyController {

    final Logger log = LoggerFactory.getLogger(ProxyController.class);

    private final ProxyService proxyService;

    ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @PostMapping("/github-webhook/")
    void githubWebhook(@RequestBody String body, HttpServletRequest request) {
        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .filter(headerName -> !headerName.equals("content-length"))
                .collect(Collectors.toMap(h -> h, request::getHeader));

        log.info(request.toString());
        log.info(request.getRequestURI());

        proxyService.githubWebhook(body, headers);
    }
}
