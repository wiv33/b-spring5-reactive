package org.psaewsome.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psawesome.server.PsExampleApplication;

/**
 * package: org.psaewsome.server
 * author: PS
 * DATE: 2020-07-31 금요일 13:21
 */
public class PsExampleApplicationTest {

  @BeforeEach
  void setUp() throws InterruptedException {
    new Thread(PsExampleApplication::main).start();
    Thread.sleep(1000);
  }

  @Test
  void testRunningServer() throws InterruptedException {
    Thread.sleep(1000);
  }
}
