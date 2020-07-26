package org.psawesome.testFlux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

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
 * @since 20. 7. 26. Sunday
 */
public class ZipTest {

  @Test
  void testZip() throws InterruptedException {
    final Flux<Integer> range = Flux.range(1, 7);
    final Flux<Long> interval = Flux.interval(Duration.ofMillis(700));

    final Flux<Tuple2<Integer, Long>> zip = Flux.zip(range, interval);
    zip.subscribe(System.out::println);

    Thread.sleep(10_000);
    /*
[1,0]
[2,1]
[3,2]
[4,3]
[5,4]
[6,5]
[7,6]
     */
  }
}
