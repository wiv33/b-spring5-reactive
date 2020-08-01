package org.psawesome.server;

import org.junit.jupiter.api.BeforeEach;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 7. 31. Friday
 */
public class PsPracticeApplicationTest {
  @BeforeEach
  void setUp() {
    new Thread(PsPracticeApplication::main).start();
  }
}
