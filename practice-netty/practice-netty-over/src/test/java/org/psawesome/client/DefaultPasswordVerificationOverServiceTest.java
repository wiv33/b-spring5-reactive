package org.psawesome.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.common.PsPasswordDTO;
import org.psawesome.server.PasswordVerificationApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
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
 * @since 20. 8. 2. Sunday
 */
class DefaultPasswordVerificationOverServiceTest {

  PasswordEncoder encoder;

  WebClient client;

  @BeforeEach
  void setUp() {
    new Thread(PasswordVerificationApplication::main).start();
    encoder = new BCryptPasswordEncoder(18);
    client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();
  }

  @Test
  void testPasswordVerificationFromServiceByProjectReactor() {
    StepVerifier.create(client
            .post()
            .uri("/check")
            .body(BodyInserters.fromPublisher(
                    Mono.just(new PsPasswordDTO("psawesome", encoder.encode("psawesome")))
                    , PsPasswordDTO.class))
            .exchange()
    )
            .expectSubscription()
            .thenAwait(Duration.ofSeconds(10))
            .assertNext(
                    response -> Assertions.assertTrue(response.statusCode().is2xxSuccessful())
            )
            .expectComplete()
            .verifyThenAssertThat()
            .tookLessThan(Duration.ofSeconds(20));

  }
}