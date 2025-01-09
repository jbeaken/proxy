package org.titan.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProxyController.class)
class ProxyControllerTest {

    @MockitoBean
    private ProxyService proxyService;

    @Autowired
    private MockMvc mockMvc;  // MockMvc instance for performing requests

    @Test
    void githubWebhook_Success() throws Exception {
        mockMvc.perform(post("/github-webhook/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("json"))
                .andExpect(status().isOk());
    }

    @Test
    void githubWebhook_Error() throws Exception {
        willThrow(new ProxyException("exception")).given(proxyService).githubWebhook(any(), any());
        mockMvc.perform(post("/github-webhook/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("json"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("exception"));
    }
}