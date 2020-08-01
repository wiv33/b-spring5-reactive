package org.psawesome.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.server.LastDemoApplication;
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
public class DefaultPasswordVerificationLastServiceTest {

  @BeforeEach
  void setUp() {
    new Thread(LastDemoApplication::main).start();
  }

  @Test
  void testVerificationPassword() {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
    final DefaultPasswordVerificationLastService service =
            new DefaultPasswordVerificationLastService(WebClient.builder());

//    check에 이 비밀번호가 맞는지 물어보고
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
