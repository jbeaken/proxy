package org.titan.proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyServiceTest {

    @Mock
    private CoreEngineeringJenkinsWebhookClient coreEngineeringJenkinsWebhookClient;

    @Mock
    private ApimJenkinsWebhookClient apimJenkinsWebhookClient;

    @InjectMocks
    ProxyService proxyService;

    @Test
    void success() {
        proxyService.githubWebhook("body", Map.of());
    }

    @Test
    void coreEngineeringJenkinsWebhookClient_error() {
        //given
        when(coreEngineeringJenkinsWebhookClient.sendWebhook("body", Map.of())).thenThrow(new RuntimeException("error"));

        // when
        var thrown = assertThrows(ProxyException.class, () ->
                proxyService.githubWebhook("body", Map.of()));

        // then
        assertEquals("error", thrown.getMessage());
    }

    @Test
    void apimJenkinsWebhookClient_error() {
        //given
        when(apimJenkinsWebhookClient.sendWebhook("body", Map.of())).thenThrow(new RuntimeException("error"));

        // when
        var thrown = assertThrows(ProxyException.class, () ->
                proxyService.githubWebhook("body", Map.of()));

        // then
        assertEquals("error", thrown.getMessage());
    }

}