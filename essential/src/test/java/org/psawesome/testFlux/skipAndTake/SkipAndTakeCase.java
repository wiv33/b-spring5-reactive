package org.psawesome.testFlux.skipAndTake;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

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
public class SkipAndTakeCase {
  Mono<String> startCommandDefer() {
    return Mono.defer(() -> Mono
            .from(Flux.fromIterable(List.of("Hello", "my name", "is", "ps"))
                    .delaySubscription(Duration.ofMillis(500))));
  }

  Mono<Integer> stopCommandCallable() {
    return Mono.fromCallable(() -> 3).delayElement(Duration.ofMillis(4000));
  }

  Mono<String> startCommandJust() {
    return Mono
            .just("my Body is")
            .delaySubscription(Duration.ofMillis(1000));
  }

  Mono<String> stopCommandJust() {
    return Mono.just(" ps ")
            .delaySubscription(Duration.ofMillis(3500));
  }
}
