package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.test.StepVerifier;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 12. Saturday
 */
@SpringBootTest
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.psawesome:producer:+:8080")
class ReservationClientTest {

  @Autowired
  ReservationClient client;

  @Test
  void testFirst() {
    StepVerifier.create(this.client.getAll())
            .expectNextMatches(r -> r.getId() == 1 && r.getName().equalsIgnoreCase("ps"))
            .verifyComplete();
  }
}
