package org.psawesome.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.server.StandaloneApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

/**
 * package: org.psawesome.client
 * author: PS
 * DATE: 2020-07-31 금요일 12:38
 */
class PasswordVerificationServiceTest {

  @BeforeEach
  void setUp() throws InterruptedException {
    final Thread thread = new Thread(StandaloneApplication::main);
    thread.start();
    Thread.sleep(10000);
  }

  @Test
  void testRunApplicationBCryptPassword() {
    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(18);
    final DefaultPasswordVerificationService service =
            new DefaultPasswordVerificationService(WebClient.builder());
//    new ReactorClientHttpConnector()
//            .connect()
    StepVerifier.create(service.check("psawesome", passwordEncoder.encode("psawesome")))
            .expectSubscription()
            .expectComplete()
            .verify();
  }
}