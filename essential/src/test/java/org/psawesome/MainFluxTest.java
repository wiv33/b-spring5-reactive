package org.psawesome;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

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
@DisplayName("Flux Mono 출력으로 확인하는 테스트 코드 by subscribe ")
@ContextConfiguration(locations = "classpath:application.yml")
class MainFluxTest {
  private final static Logger log = LoggerFactory.getLogger(MainFluxTest.class);


  @Test
  void testDispose() throws InterruptedException {
    final Disposable disposable = Flux.interval(Duration.ofMillis(50))
            .subscribe(interval -> log.info("onNext: {}", interval));
    assertFalse(disposable.isDisposed());
    Thread.sleep(2000);
    disposable.dispose();

    assertTrue(disposable.isDisposed());
  }

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

  @Test
  void testFluxAny() {
    log.info("MainTest.testFluxAny");
    Flux.generate(sink -> sink.next(ThreadLocalRandom.current().nextInt()))
            .cast(Integer.class)
            .any(n -> n % 2 == 0)
            .subscribe(event -> log.info("Has event :{}", event),
                    throwable -> log.error(throwable.getMessage()),
                    () -> log.info("testFluxAny Complete"),
                    subscription -> subscription.request(7));
  }

  @Test
  @DisplayName("리스트의 모든 원소를 수집하고 정렬")
  void testCollectSortedList() {
    Flux.just(1, 6, 8, 9, 2, 5, 6, 1, 7, 15, 6, 3, 72)
            .log()
            .collectSortedList(Comparator.reverseOrder())
            .subscribe(System.out::println,
                    this::error,
                    this::onComplete,
                    subscription -> subscription.request(1));
  }


  @Test
  @DisplayName("timestamp와 index의 기능 확인")
  void timestampAndIndex() {
    log.info("timestamp와 index의 기능 확인");
    Flux.range(2018, 5)
            .timestamp()
            .log("timestamp ==> ")
            .index()
            .log()
            .subscribe(e -> log.info("index: {}, ts: {}, value: {}",
                    e.getT1(),
                    Instant.ofEpochMilli(e.getT2().getT1()),
                    e.getT2().getT2())
            );
  }




  void error(Throwable throwable) {
    log.error("Throwable is : {}", throwable.getMessage());
  }

  void onComplete() {
    log.info("onComplete");
  }
}
