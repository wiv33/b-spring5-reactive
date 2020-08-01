package org.psawesome.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.server.ThirdNettyApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 8. 1. Saturday
 */
public class DefaultVerificationServiceTest {

  @BeforeEach
  void setUp() {
    new Thread(ThirdNettyApplication::main).start();
  }

  @Test
  void testVerificationService() {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    final DefaultPasswordVerificationService service = new DefaultPasswordVerificationService(WebClient.builder());
    StepVerifier.create(service.check("pass", encoder.encode("pass")), 1)
            .expectSubscription()
            .thenAwait(Duration.ofSeconds(10))
            .expectComplete()
            .verifyThenAssertThat()
            .tookLessThan(Duration.ofSeconds(20));
  }
}
