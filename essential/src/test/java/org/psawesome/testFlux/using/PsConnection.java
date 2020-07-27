package org.psawesome.testFlux.using;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PsConnection implements Closeable {

  public static final Logger log = LoggerFactory.getLogger(PsConnection.class);

  @Override
  public void close() {
    log.info("IO Connection Closed");
  }

  public Iterable<String> getData() {
    if (ThreadLocalRandom.current().nextInt(10) < 3) {
      throw new RuntimeException("Communication error");
    }
    return List.of("some", "data");
  }

  public static PsConnection newConnection() {
    log.info("IO Connection created");
    return new PsConnection();
  }
}
