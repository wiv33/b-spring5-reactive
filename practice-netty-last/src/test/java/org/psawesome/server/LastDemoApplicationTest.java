package org.psawesome.server;

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
public class LastDemoApplicationTest {
  @BeforeEach
  void setUp() {
    new Thread(LastDemoApplication::main).start();
  }

  @Test
  void testServerRun() {
    System.out.println("LastDemoApplicationTest.testServerRun");
  }
}
