package org.psawesome.testFlux;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedHashMap;

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
    final Mono<Integer> reduce = Flux.range(1, 5)
            .reduce(0, Integer::sum);

    reduce.subscribe(res -> log.info("result : {}", res));
/*
05:07:06.846 [Test worker] INFO org.psawesome.MainTest - result : 15
 */

    log.info(" ===================== split ===================== ");

    final Flux<Integer> scan = Flux.range(1, 5)
            .scan(0, Integer::sum);

    scan.subscribe(res -> log.info("result : {}", res));
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

  @Test
  void testReduceAccStringBuilder() {
    Flux.range(1, 7)
            .map(n -> String.format("my number is %d\n", n))
            .reduce(new StringBuilder(), StringBuilder::append)
            .subscribe(consumer -> log.info(consumer.toString()));
    /*
15:01:24.998 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - my number is 1
my number is 2
my number is 3
my number is 4
my number is 5
my number is 6
my number is 7
     */
  }

  @Test
  void testReduceLinkedHashMap() {
    Flux.range(1, 7)
            .reduce(new LinkedHashMap<>(), (accLinked, element) -> {
              accLinked.put(String.format("key is %d", element), String.format("my value is %d", element));
              return accLinked;
            })
            .subscribe(consumer -> consumer.entrySet().forEach(System.out::println));
    /*
key is 1=my value is 1
key is 2=my value is 2
key is 3=my value is 3
key is 4=my value is 4
key is 5=my value is 5
key is 6=my value is 6
key is 7=my value is 7

     */
  }

  @Test
  void testReduceIntegerSum() {
    Flux.range(1, 3)
            .reduce(3, Integer::sum)
            .subscribe(System.out::println);
  }

  @Test
  void testScanAccStringBuilder() {
    Flux.range(1, 7)
            .scan(new StringBuffer(), StringBuffer::append)
            .subscribe(consumer -> log.info(consumer.toString()));
    /*
subscribe 출력 형태가 계단으로 출력
15:18:44.629 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest -
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 1
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 12
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 123
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 1234
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 12345
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 123456
15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 1234567
     */
  }
}
