package org.psawesome.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class ThirdNettyApplicationTest {
  Thread thread;

  @BeforeEach
  void setUp() {
    thread = new Thread(ThirdNettyApplication::main);
    thread.start();
  }

  @Test
  void testServerRun() {
    System.out.println("ThirdNettyApplicationTest.testServerRun");
  }
}
