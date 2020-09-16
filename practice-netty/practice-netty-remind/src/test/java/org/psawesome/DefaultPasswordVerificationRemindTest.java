package org.psawesome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
 * @since 20. 9. 16. Wednesday
 */
public class DefaultPasswordVerificationRemindTest {
  PasswordEncoder encoder;

  @BeforeEach
  void setUp() {
    new Thread(NettyApplication::main).start();
    encoder = new BCryptPasswordEncoder(18);
  }

  @Test
  void testDefaultPasswordVerificationFromService() {
    final PasswordVerificationRemindService service =
            new DefaultNettyPasswordVerificationRemindService(WebClient.builder());

    StepVerifier.create(service.check("ps", encoder.encode("ps")))
            .expectSubscription()
//            10초 기다리고
            .thenAwait(Duration.ofSeconds(10))
            .expectComplete()
            .verifyThenAssertThat()
//            20초 이내 완료된다는 것을 의미
            .tookLessThan(Duration.ofSeconds(20));

  }
}
