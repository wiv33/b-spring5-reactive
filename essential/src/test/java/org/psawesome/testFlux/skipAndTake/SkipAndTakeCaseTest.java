package org.psawesome.testFlux.skipAndTake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.Instant;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 8. 8. Saturday
 */
public class SkipAndTakeCaseTest {
  Flux<Tuple2<Long, String>> sourceStream;
  SkipAndTakeCase aCase;

  @BeforeEach
  void setUp() {
    sourceStream = Flux.interval(Duration.ofMillis(333))
            .index()
            .map(m -> Tuples.
                    of(m.getT1(), Instant.ofEpochMilli(m.getT2()).toString())
            );
    aCase = new SkipAndTakeCase();

  }

  @Test
  void testSkipAndTake() throws InterruptedException {
    final Mono<String> startCommand = aCase.startCommandDefer();
    final Mono<Integer> stopCommand = aCase.stopCommandCallable();
    final Disposable on_complete = this.sourceStream
            .skipUntilOther(startCommand)
            .takeUntilOther(stopCommand)
            .subscribe(System.out::println,
                    System.err::println,
                    () -> System.out.println("on Complete"));

    Thread.sleep(4000);
    on_complete.dispose();

  }

  @Test
  void testSkipAndTakeJust() throws InterruptedException {
    final Mono<String> startJust = aCase.startCommandJust();
    final Mono<String> stopJust = aCase.stopCommandJust();
    final Disposable on_complete = this.sourceStream
            .skipUntilOther(startJust)
            .takeUntilOther(stopJust)
            .subscribe(System.out::println,
                    System.err::println,
                    () -> System.out.println("on Complete"));

    Thread.sleep(4000);
    on_complete.dispose();
  }
}
