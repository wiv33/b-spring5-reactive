package org.psawesome.client;

import org.junit.jupiter.api.Test;

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
public class DefaultPasswordVerificationLastServiceTest {
  @Test
  void testVerificationPassword() {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    final DefaultPasswordVerificationLastService service = new DefaultPasswordVerificationLastService();
  }
}
