package org.psawesome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 7. Monday
 */
public class NettyApplicationTest {

  WebTestClient testClient;

  @BeforeEach
  void setUp() throws InterruptedException {
    new Thread(NettyApplication::main)
            .start();
    Thread.sleep(10000);

  }

  @Test
  void testNettyApplicationRun() throws InterruptedException {

  }
}
