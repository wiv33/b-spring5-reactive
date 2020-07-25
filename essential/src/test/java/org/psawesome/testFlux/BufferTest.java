package org.psawesome.testFlux;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 7. 25. Saturday
 */
public class BufferTest {
  public static final Logger log = LoggerFactory.getLogger(BufferTest.class);

  @Test
  void testBuffer() {
    Flux.range(1, 17)
            .buffer(4)
            .subscribe(e -> log.info("onNext : {}", e));
    /*
19:49:31.845 [Test worker] INFO org.psawesome.testFlux.FluxBufferTest - onNext : [1, 2, 3, 4]
19:49:31.847 [Test worker] INFO org.psawesome.testFlux.FluxBufferTest - onNext : [5, 6, 7, 8]
19:49:31.847 [Test worker] INFO org.psawesome.testFlux.FluxBufferTest - onNext : [9, 10, 11, 12]
19:49:31.847 [Test worker] INFO org.psawesome.testFlux.FluxBufferTest - onNext : [13, 14, 15, 16]
19:49:31.847 [Test worker] INFO org.psawesome.testFlux.FluxBufferTest - onNext : [17]
     */
  }
}
