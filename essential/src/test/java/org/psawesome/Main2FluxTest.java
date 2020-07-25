package org.psawesome;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

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
public class Main2FluxTest {

  @Test
  @DisplayName("어떤 퍼블리셔가 도착하기 전까지 실행하라")
  void testStartUntilStopPublisher() throws InterruptedException {
    Mono<?> startEvent = Mono.delay(Duration.ofSeconds(3))
            .log("i am start Event");
    Mono<?> endEvent = Mono.delay(Duration.ofSeconds(17))
            .log("stop ! interval");

    final Flux<Long> interval = Flux.interval(Duration.ofSeconds(1)).log("Schedule interval");
    interval.skipUntilOther(startEvent)
            .takeUntilOther(endEvent)
            .subscribe(System.out::println);

    Thread.sleep(33000);
  }
}
