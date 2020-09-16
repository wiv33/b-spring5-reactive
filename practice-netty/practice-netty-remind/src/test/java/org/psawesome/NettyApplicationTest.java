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

  @BeforeEach
  void setUp() throws InterruptedException {
    new Thread(NettyApplication::main).start();
  }

  @Test
  void testNettyApplicationRun(){
    System.out.println("NettyApplicationTest.testNettyApplicationRun");
  }
}
