package org.psawesome.testFlux.using;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

class UsingByConnectionTest {

  public static final Logger log = LoggerFactory.getLogger(UsingByConnectionTest.class);

  @Test
  void testCloseable() {
    try (PsConnection conn = PsConnection.newConnection()) {
      conn.getData().forEach(data -> log.info("receive Data : {}", data));
    } catch (RuntimeException e) {
      log.info("err : {}", e.getMessage());
    }

    /*
16:15:12.526 [Test worker] INFO org.psawesome.testFlux.using.PsConnection - IO Connection created
16:15:12.530 [Test worker] INFO org.psawesome.testFlux.using.PsConnection - IO Connection Closed
16:15:12.531 [Test worker] INFO org.psawesome.testFlux.using.PsConnectionTest - err : Communication error
     */
  }

  @Test
  void testUsing() {
    Flux.using(
            PsConnection::newConnection,
            psConnection -> Flux.fromIterable(psConnection.getData()),
            PsConnection::close,
            true // default
    ).subscribe(
            data -> log.info("Receive data: {}", data),
            throwable -> log.info("err : {}", throwable.getMessage()),
            () -> log.info("stream finished")
    );
  }
}