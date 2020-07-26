package org.psawesome.testFlux;

import org.junit.jupiter.api.Test;
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
 * @since 20. 7. 26. Sunday
 */
public class ConcatTest {

  @Test
  void testConcat() {
    Flux.concat(
            Flux.range(1, 3),
            Flux.range(4, 2),
            Flux.range(6, 5),
            Flux.just("a", "b", "c", "d", "e")
    ).subscribe(System.out::println);
  }
}
