package org.psawesome.testFlux;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

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
 * @since 20. 7. 26. Sunday
 */
public class IndexAndTimestampTest {
  public static final Logger log = LoggerFactory.getLogger(IndexAndTimestampTest.class);

  @Test
  void testTimestamp() {
    final Flux<Tuple2<Long, String>> timestamp = getJust().timestamp();
    timestamp.subscribe(consumer -> log.info("t1 = [{}], t2 = [{}] ", Instant.ofEpochMilli(consumer.getT1()), consumer.getT2()));
    /*
17:41:08.055 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.054Z], t2 = [a]
17:41:08.058 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.058Z], t2 = [b]
17:41:08.059 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.059Z], t2 = [c]
17:41:08.059 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.059Z], t2 = [d]
17:41:08.059 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.059Z], t2 = [e]
17:41:08.059 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.059Z], t2 = [f]
17:41:08.060 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - t1 = [2020-07-26T08:41:08.060Z], t2 = [g]
     */
  }

  @Test
  void testIndex() {
    final Flux<Tuple2<Long, String>> index = getJust().index();
    index.subscribe(consumer -> log.info("T1 = [{}], T2 = [{}]",consumer.getT1(), consumer.getT2()));

    /*
17:38:25.110 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [0], T2 = [a]
17:38:25.111 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [1], T2 = [b]
17:38:25.111 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [2], T2 = [c]
17:38:25.111 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [3], T2 = [d]
17:38:25.111 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [4], T2 = [e]
17:38:25.111 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [5], T2 = [f]
17:38:25.111 [Test worker] INFO org.psawesome.testFlux.IndexAndTimestampTest - T1 = [6], T2 = [g]
     */
  }

  private Flux<String> getJust() {
    return Flux.just("a", "b", "c", "d", "e", "f", "g");
  }
}
