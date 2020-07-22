package org.psawesome.chapters.chap04;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class ReactorEssentialsTest {

  @Test
  void testCreateFlux() {
    Flux<String> stream1 = Flux.just("Hello", "world");
    Flux<Integer> stream2 = Flux.fromArray(new Integer[]{1, 2, 3, 4});
    Flux<Integer> stream3 = Flux.range(1, 500);

    final Flux<String> empty = Flux.empty();
    final Flux<String> streamWithError = Flux.error(new RuntimeException("err!"));
  }
}
