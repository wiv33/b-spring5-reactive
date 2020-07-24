package org.psawesome;

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
public class MainTest {
  private final static Logger log = LoggerFactory.getLogger(MainTest.class);
  @Test
  void testFluxRange() {
    Flux.range(1, 5)
            .reduce(0, Integer::sum)
            .subscribe(
                    res -> log.info("result : {}", res)
            );
  }

}
