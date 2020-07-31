package org.psawesome.client;

import io.netty.handler.timeout.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.server.PsPracticeApplication;
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
class DefaultPasswordVerificationServiceTest {

  @BeforeEach
  void setUp() {
    new Thread(PsPracticeApplication::main).start();
  }

  @Test
  void testRunningNettyServerPasswordEncoder() {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    final DefaultPasswordVerificationService service =
            new DefaultPasswordVerificationService(WebClient.builder());

//    StepVerifier.setDefaultTimeout(Duration.ofSeconds(17));
    StepVerifier.create(service.check("psawesome", encoder.encode("psawesome")), 1)
            .expectSubscription()
            .thenAwait(Duration.ofSeconds(10))
            .expectComplete()
            .verifyThenAssertThat()
            .hasDroppedErrors(10);
  }
}