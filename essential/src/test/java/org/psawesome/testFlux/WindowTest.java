package org.psawesome.testFlux;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

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
public class WindowTest {
  public static final Logger log = LoggerFactory.getLogger(WindowTest.class);

  @Test
  void testWindow() {
    final Flux<Flux<Integer>> windowedFlux = Flux.range(101, 20)
            .windowUntil(this::isPrime, true)
            .log();
    windowedFlux.subscribe(window -> window
            .collectList()
            .subscribe(n -> log.info("window: {}", n)));
  }

  private boolean isPrime(Integer integer) {
    return integer > 2
            && IntStream.rangeClosed(2, (int) StrictMath.sqrt(integer))
            .noneMatch(n -> integer % n == 0);
  }

  /*
20:12:49.114 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onSubscribe([Fuseable] FluxWindowPredicate.WindowPredicateMain)
20:12:49.115 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | request(unbounded)
20:12:49.116 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onNext(WindowFlux)
20:12:49.133 [Test worker] INFO org.psawesome.testFlux.WindowTest - window: []
20:12:49.134 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onNext(WindowFlux)
20:12:49.134 [Test worker] INFO org.psawesome.testFlux.WindowTest - window: [101, 102]
20:12:49.134 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onNext(WindowFlux)
20:12:49.134 [Test worker] INFO org.psawesome.testFlux.WindowTest - window: [103, 104, 105, 106]
20:12:49.134 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onNext(WindowFlux)
20:12:49.134 [Test worker] INFO org.psawesome.testFlux.WindowTest - window: [107, 108]
20:12:49.135 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onNext(WindowFlux)
20:12:49.135 [Test worker] INFO org.psawesome.testFlux.WindowTest - window: [109, 110, 111, 112]
20:12:49.135 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onNext(WindowFlux)
20:12:49.136 [Test worker] INFO org.psawesome.testFlux.WindowTest - window: [113, 114, 115, 116, 117, 118, 119, 120]
20:12:49.136 [Test worker] INFO reactor.Flux.WindowPredicate.1 - | onComplete()

   */
}
