package org.psawesome.client;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

  @Test
  void testVerificationService() {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    final DefaultVerificationService service = new DefaultVerificationService();
    StepVerifier.create(servie.check("pass", encoder.encode("pass")));
  }
}
