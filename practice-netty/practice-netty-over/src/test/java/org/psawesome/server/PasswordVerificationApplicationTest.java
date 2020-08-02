package org.psawesome.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.common.PsPasswordDTO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

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
public class PasswordVerificationApplicationTest {

  WebTestClient testClient;

  @BeforeEach
  void setUp() throws InterruptedException {
    new Thread(PasswordVerificationApplication::main).start();
    Thread.sleep(10000);

    testClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080")
            .responseTimeout(Duration.ofSeconds(20))
            .build();
    
  }

  @Test
  void testServerRun() {
    System.out.println("PasswordVerificationApplicationTest.testServerRun");
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    testClient.post()
            .uri("/check")
            .body(BodyInserters.fromPublisher(
                    Mono.just(new PsPasswordDTO("test", encoder.encode("test"))),
                    PsPasswordDTO.class)
            )
            .exchange()
            .expectStatus().is2xxSuccessful();
  }
}
