package org.psawesome.testFlux;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.Arrays;

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
public class ReduceScanTest {
  public static final Logger log = LoggerFactory.getLogger(ReduceScanTest.class);

  @Test
  @DisplayName("Reduce와 Scan의 차이점")
  void testFluxReduceAndScan() {
    Flux.range(1, 5)
            .reduce(0, Integer::sum)
            .subscribe(res -> log.info("result : {}", res));
/*
05:07:06.846 [Test worker] INFO org.psawesome.MainTest - result : 15
 */

    log.info(" ===================== split ===================== ");

    Flux.range(1, 5)
            .scan(0, Integer::sum)
            .subscribe(res -> log.info("result : {}", res));
    /*
05:07:06.850 [Test worker] INFO org.psawesome.MainTest - result : 0
05:07:06.851 [Test worker] INFO org.psawesome.MainTest - result : 1
05:07:06.851 [Test worker] INFO org.psawesome.MainTest - result : 3
05:07:06.851 [Test worker] INFO org.psawesome.MainTest - result : 6
05:07:06.851 [Test worker] INFO org.psawesome.MainTest - result : 10
05:07:06.851 [Test worker] INFO org.psawesome.MainTest - result : 15
     */
  }

  @Test
  @DisplayName("scan을 이용하여 스트림의 평균 이동을 계산할 수 있다.")
  void testFluxScan() {
    log.info("MainTest.testFluxScan");
    Hooks.onOperatorDebug();
    int bucketSize = 5; // 다섯 가지의 이벤트 == 관심있는 이벤트
    Flux.range(1, 500)
            .index() // 각 원소에 인덱스를 부여한다.
            .checkpoint("i want point trace", false)
            .log()
//            .filter(n -> n.getT1() % 3 == 0)
            .scan(
                    new int[bucketSize],
                    (acc, elem) -> {
                      acc[elem.getT1().intValue() % bucketSize] = elem.getT2();
                      return acc;
                    })
            .log()
            .skip(bucketSize)
            .log()
            .map(arr -> Arrays.stream(arr).sum() * 1.0 / bucketSize)
            .subscribe(average -> log.info("Running average : {}", average),
                    throwable -> log.error(throwable.getMessage()),
                    () -> log.info("onComplete"),
                    subscription -> subscription.request(Long.MAX_VALUE));
  }

}
