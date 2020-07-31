package org.psawesome.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.server.PsExampleApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * package: org.psawesome.client
 * author: PS
 * DATE: 2020-07-31 금요일 13:32
 */
class PasswordVerificationServiceTest {

  @BeforeEach
  void setUp() throws InterruptedException {
    new Thread(PsExampleApplication::main).start();
    Thread.sleep(1000);
  }

  @Test
  void testPasswordEncoderByNettyServer() {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    final DefaultPasswordVerificationService service = new DefaultPasswordVerificationService(WebClient.builder());

    StepVerifier.create(service.check("", ""));
  }
}