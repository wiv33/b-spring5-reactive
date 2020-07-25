package org.psawesome.testFlux;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.SignalType;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

  @Test
  void testImplSubscriber() throws InterruptedException {

    Subscriber<Integer> subscriber = this.mySubscriber();
    final Flux<Integer> cold = Flux.range(0, 33)
            .log()
            .repeat(7);
    cold.subscribe(subscriber);

    Thread.sleep(10000);
  }

  private Subscriber<Integer> mySubscriber() {
    return new Subscriber<>() {
      Subscription subscription;
      AtomicInteger integer = new AtomicInteger(0);

      @Override
      public void onSubscribe(Subscription s) {
        this.subscription = s;
        log.info("onSubscriber ! this thread name is [{}]", Thread.currentThread().getName());
        subscription.request(7);
        // tag::onComplete and request test[]
//        subscription.request(Long.MAX_VALUE);
        // end::onComplete and request test[]

      }

      @Override
      public void onNext(Integer aInteger) {
        log.info("onNext is {}", aInteger);
        if (integer.getAndIncrement() > 2) {
          subscription.request(3);
          integer.set(0);
        }
      }

      @Override
      public void onError(Throwable t) {
        log.error(t.getMessage());
      }

      @Override
      public void onComplete() {
        log.info("onComplete and Request");
        // tag::onComplete and request test[]
//        subscription.request(3);
        // end::onComplete and request test[]
      }
    };
  }

  // tag::extendsMySubscriberBaseSubscriber[]
  @Test
  void testExtendsMySubscriber() {
    MySubscriber<Integer> mySubscriber = new MySubscriber<>();
    final Flux<Integer> repeat = Flux.range(1, 33)
            .log()
            .repeat(7);

    repeat.subscribe(mySubscriber);
  }

  static class MySubscriber<T> extends BaseSubscriber<T> {
    static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    protected Subscription upstream() {
      log.info("My upstream ==== ");
      return super.upstream();
    }

    public MySubscriber() {
      super();
      log.info("MySubscriber constructor");
    }

    @Override
    protected void hookOnSubscribe(Subscription sub) {
      log.info("hook on subscribe request 7");
      request(7);
    }

    @Override
    protected void hookOnNext(T value) {
      log.info("onNext is {}", value);
      super.hookOnNext(value);
      if (atomicInteger.getAndIncrement() > 2) {
        final String name = ManagementFactory.getRuntimeMXBean().getName();
        log.info("app PID is [{}]", name.split("@")[0]);
        request(4);
        atomicInteger.set(0);
      }
    }

    @Override
    protected void hookFinally(SignalType type) {
      log.info("hookFinally === ");
      super.hookFinally(type);
      if (super.isDisposed()) {
        log.info("finally dispose");
        super.dispose();
      }
    }
  }
  // end::extendsMySubscriberBaseSubscriber[]

  @Test
  void testThenMany() {
    // 상위 스트림이 완료될 때
    Flux.just(1, 2, 3)
            .log("just log")
            .thenMany(Flux.just(4, 5))
            .log("thenMany log")
            .subscribe(s -> log.info("onNext: {}", s));
    /*
17:25:55.823 [Test worker] INFO thenMany log - onSubscribe(FluxConcatArray.ConcatArraySubscriber)
17:25:55.825 [Test worker] INFO thenMany log - request(unbounded)
17:25:55.827 [Test worker] INFO just log - | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
17:25:55.827 [Test worker] INFO just log - | request(unbounded)
17:25:55.828 [Test worker] INFO just log - | onNext(1)
17:25:55.828 [Test worker] INFO just log - | onNext(2)
17:25:55.828 [Test worker] INFO just log - | onNext(3)
17:25:55.828 [Test worker] INFO just log - | onComplete()
17:25:55.829 [Test worker] INFO thenMany log - onNext(4)
17:25:55.829 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext: 4
17:25:55.829 [Test worker] INFO thenMany log - onNext(5)
17:25:55.829 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext: 5
17:25:55.829 [Test worker] INFO thenMany log - onComplete()
     */
  }

  @Test
  void testConcat() {
    Flux.concat(
            Flux.range(1, 3),
            Flux.range(4, 2),
            Flux.range(6, 5)
    ).subscribe(n -> log.info("onNext : {}", n));
    /*
17:36:11.902 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 1
17:36:11.903 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 2
17:36:11.903 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 3
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 4
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 5
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 6
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 7
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 8
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 9
17:36:11.904 [Test worker] INFO org.psawesome.testFlux.MainFluxTest - onNext : 10
     */
  }

  void error(Throwable throwable) {
    log.error("Throwable is : {}", throwable.getMessage());
  }

  void onComplete() {
    log.info("onComplete");
  }
}
