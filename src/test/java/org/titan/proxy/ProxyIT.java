package org.titan.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class ProxyIT extends AbstractWiremockIT {

    @Test
    void contextLoads() {
    }

}
