package dev.sivalabs.geeksclub;

import org.junit.jupiter.api.Test;

class GeeksClubApplicationTests extends BaseIntegrationTest {

    @Test
    void contextLoads() {
        restTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
    }
}
