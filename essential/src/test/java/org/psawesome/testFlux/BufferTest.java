package org.psawesome.testFlux;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
public class BufferTest {
  public static final Logger log = LoggerFactory.getLogger(BufferTest.class);

  Flux<Integer> range17;
  Flux<Integer> delayRange17;
  Subscriber<List<Integer>> subscriber;
  Subscriber<List<Long>> intervalSubscriber;

  @BeforeEach
  void setUp() {
    range17 = rangeFlux();
    delayRange17 = delayElements1sRange17();
    subscriber = getSubscriber();
    intervalSubscriber = getSubscriber();
  }

  @Test
  @DisplayName("onComplete까지 버퍼")
  void testUntilOnComplete() {
    final Flux<List<Integer>> buffer = range17.buffer();

    buffer.subscribe(subscriber);

    StepVerifier.create(range17)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
            .verifyComplete();
    /*
23:50:08.180 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
23:50:08.181 [Test worker] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("버퍼의 최대 사이즈 4")
  void testSize4() {
    range17.buffer(4)
            .subscribe(subscriber);
    /*
23:46:27.114 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3, 4]
23:46:27.115 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7, 8]
23:46:27.115 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11, 12]
23:46:27.115 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15, 16]
23:46:27.115 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
23:46:27.116 [Test worker] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("두 번째 인자인 skip의 사이즈 2")
  void testSize4AndSkip2() {
    final Flux<List<Integer>> buffer = range17.buffer(4, 2);
    buffer.subscribe(subscriber);
    /*
00:08:19.484 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3, 4]
00:08:19.485 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5, 6]
00:08:19.485 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7, 8]
00:08:19.486 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9, 10]
00:08:19.486 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11, 12]
00:08:19.486 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13, 14]
00:08:19.486 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15, 16]
00:08:19.487 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
00:08:19.487 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
00:08:19.487 [Test worker] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("두 번째 인자인 max size 4, skip의 사이즈 7 - list의 사이즈는 4이고, 원소는 7개 단위로 스킵하여 각 배열마다 3개씩 데이터 손실이 일어난다.")
  void testSize4AndSkip7() {
    final Flux<List<Integer>> buffer = range17.buffer(4, 7);
    buffer.subscribe(subscriber);
    /*
00:09:51.758 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3, 4]
00:09:51.759 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10, 11]
00:09:51.759 [Test worker] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
00:09:51.759 [Test worker] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("10초 지연 publisher, buffer 3초 모음 - 버퍼 한다는 점에서 큰 의미 없음")
  void testDelay10sPublisherAndBuffer3s() throws InterruptedException {
    this.delayRange17
            .log()
            .buffer(Duration.of(3, ChronoUnit.SECONDS))
            .subscribe(subscriber);

    Thread.sleep(11_000);
    /*
13:28:21.565 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
13:28:21.565 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("700ms마다 지연하는 publisher, buffer 3s")
  void testDelay700msPublisherBuffer3s() throws InterruptedException {
    delayArgRange17(300)
            .log()
            .buffer(Duration.ofMillis(ThreadLocalRandom.current().nextInt(2_000)))
            .subscribe(subscriber);

    log.info("whoami {}", Thread.currentThread().getName());
    Thread.sleep(15_000);
    log.info("whoami {}", Thread.currentThread().getName());

  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 3간 모음 - 의미 있음")
  void testPublisherAndBuffer3s() throws InterruptedException {
    this.intervalFluxAndLog()
            .log()
            .buffer(Duration.of(3, ChronoUnit.SECONDS))
            .subscribe(intervalSubscriber);

    Thread.sleep(11_000);
    /*
01:39:43.927 [Test worker] INFO reactor.Flux.Interval.1 - request(unbounded)
01:39:44.629 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(0)
01:39:44.630 [parallel-2] INFO reactor.Flux.Log.2 - onNext(0)
01:39:45.327 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(1)
01:39:45.328 [parallel-2] INFO reactor.Flux.Log.2 - onNext(1)
01:39:46.027 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(2)
01:39:46.028 [parallel-2] INFO reactor.Flux.Log.2 - onNext(2)
01:39:46.727 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(3)
01:39:46.728 [parallel-2] INFO reactor.Flux.Log.2 - onNext(3)
01:39:46.924 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2, 3]
01:39:47.427 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(4)
01:39:47.428 [parallel-2] INFO reactor.Flux.Log.2 - onNext(4)
01:39:48.127 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(5)
01:39:48.128 [parallel-2] INFO reactor.Flux.Log.2 - onNext(5)
01:39:48.827 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(6)
01:39:48.828 [parallel-2] INFO reactor.Flux.Log.2 - onNext(6)
01:39:49.527 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(7)
01:39:49.528 [parallel-2] INFO reactor.Flux.Log.2 - onNext(7)
01:39:49.923 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6, 7]
01:39:50.227 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(8)
01:39:50.228 [parallel-2] INFO reactor.Flux.Log.2 - onNext(8)
01:39:50.927 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(9)
01:39:50.928 [parallel-2] INFO reactor.Flux.Log.2 - onNext(9)
01:39:51.627 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(10)
01:39:51.628 [parallel-2] INFO reactor.Flux.Log.2 - onNext(10)
01:39:52.328 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(11)
01:39:52.328 [parallel-2] INFO reactor.Flux.Log.2 - onNext(11)
01:39:52.923 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10, 11]
01:39:53.027 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(12)
01:39:53.028 [parallel-2] INFO reactor.Flux.Log.2 - onNext(12)
01:39:53.727 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(13)
01:39:53.728 [parallel-2] INFO reactor.Flux.Log.2 - onNext(13)
01:39:54.427 [parallel-2] INFO reactor.Flux.Interval.1 - onNext(14)
01:39:54.428 [parallel-2] INFO reactor.Flux.Log.2 - onNext(14)
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, interval 300ms, 모으는 시간 300ms, 이동 속도 450ms - 데이터 손실이 일어남")
  void testIntervalPublisherAndBuffer2_000msShift100ms() throws InterruptedException {
    this.intervalFluxAndLog(300)
            .buffer(Duration.ofMillis(300), Duration.ofMillis(450))
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
14:26:20.487 [Test worker] INFO reactor.Flux.Take.1 - onSubscribe(FluxTake.TakeSubscriber)
14:26:20.489 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
14:26:20.792 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
14:26:20.799 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0]
14:26:21.091 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
14:26:21.242 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [1]
14:26:21.391 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
14:26:21.690 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
14:26:21.691 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3]
14:26:21.991 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
14:26:22.142 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [4]
14:26:22.290 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
14:26:22.591 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
14:26:22.591 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [5]
14:26:22.891 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
14:26:23.041 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [7]
14:26:23.191 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
14:26:23.491 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
14:26:23.492 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9]
14:26:23.791 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
14:26:23.942 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [10]
14:26:24.090 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
14:26:24.390 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
14:26:24.391 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12]
14:26:24.690 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
14:26:24.842 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [13]
14:26:24.991 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
14:26:25.291 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
14:26:25.291 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15]
14:26:25.590 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
14:26:25.742 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [16]
14:26:25.890 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
14:26:25.892 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
14:26:25.893 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
14:26:25.893 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2000ms 모음, shift 300ms")
  void testDelayPublisherAndBuffer2000msShift300ms() throws InterruptedException {
    getBuffer700ms(2_000, 300).subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:20:58.209 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:20:58.912 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:20:59.611 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:21:00.217 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:21:00.311 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:21:00.512 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2]
02:21:00.812 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2]
02:21:01.011 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:21:01.112 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:21:01.412 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:21:01.711 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:21:01.711 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3]
02:21:02.012 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4]
02:21:02.311 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4]
02:21:02.411 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:21:02.612 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:21:02.911 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:21:03.111 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:21:03.212 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6]
02:21:03.511 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6]
02:21:03.811 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:21:03.812 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:21:04.112 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:21:04.411 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:21:04.511 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:21:04.712 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:21:05.011 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:21:05.211 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:21:05.312 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9]
02:21:05.612 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9]
02:21:05.911 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:21:05.912 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:21:06.212 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:21:06.512 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:21:06.611 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:21:06.812 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:21:07.112 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:21:07.311 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:21:07.412 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12]
02:21:07.712 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12]
02:21:08.011 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:21:08.012 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:21:08.312 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:21:08.612 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:21:08.711 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:21:08.912 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [12, 13, 14]
02:21:09.212 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [12, 13, 14]
02:21:09.411 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:21:09.512 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15]
02:21:09.812 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15]
02:21:10.111 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:21:10.111 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15]
02:21:10.412 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16]
02:21:10.712 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16]
02:21:10.811 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:21:10.813 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:21:10.815 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:21:10.816 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:21:10.816 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:21:10.817 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:21:10.817 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:21:10.817 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:21:10.818 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:21:10.818 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
    */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 400ms")
  void testPublisherAndBuffer2sShift1() throws InterruptedException {
    getBuffer700ms(2_000, 400)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:22:46.830 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:22:47.533 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:22:48.233 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:22:48.842 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:22:48.932 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:22:49.234 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2]
02:22:49.633 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:22:49.634 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:22:50.034 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:22:50.333 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:22:50.434 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4]
02:22:50.834 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4]
02:22:51.033 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:22:51.234 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:22:51.634 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:22:51.733 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:22:52.034 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6]
02:22:52.432 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:22:52.433 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:22:52.834 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:22:53.133 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:22:53.234 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:22:53.633 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:22:53.833 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:22:54.034 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9]
02:22:54.433 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9]
02:22:54.533 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:22:54.834 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:22:55.233 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:22:55.234 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:22:55.633 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:22:55.933 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:22:56.034 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12]
02:22:56.434 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12]
02:22:56.633 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:22:56.834 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:22:57.234 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:22:57.333 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:22:57.634 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [12, 13, 14]
02:22:58.033 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:22:58.034 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14]
02:22:58.434 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15]
02:22:58.733 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:22:58.834 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16]
02:22:59.234 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16]
02:22:59.433 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:22:59.435 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:22:59.437 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:22:59.437 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:22:59.438 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:22:59.438 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:22:59.438 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:22:59.438 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!

     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 700ms - 17 ")
  void testBuffer2sShift700ms() throws InterruptedException {
    getBuffer700ms(2_000, 700)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:23:18.650 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:23:19.354 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:23:20.052 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:23:20.662 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:23:20.752 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:23:21.353 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2]
02:23:21.452 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:23:22.053 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:23:22.152 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:23:22.753 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4]
02:23:22.852 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:23:23.453 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:23:23.552 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:23:24.153 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6]
02:23:24.252 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:23:24.853 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:23:24.952 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:23:25.553 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:23:25.652 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:23:26.253 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9]
02:23:26.352 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:23:26.953 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:23:27.052 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:23:27.653 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:23:27.752 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:23:28.353 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12]
02:23:28.452 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:23:29.053 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:23:29.152 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:23:29.753 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14]
02:23:29.852 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:23:30.453 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15]
02:23:30.552 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:23:31.153 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16]
02:23:31.252 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:23:31.254 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:23:31.255 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:23:31.255 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:23:31.255 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:23:31.256 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 1200ms")
  void testPublisherAndBuffer2sShift1200() throws InterruptedException {
    getBuffer700ms(2_000, 1_200)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:24:10.439 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:24:11.142 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:24:11.841 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:24:12.448 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:24:12.541 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:24:13.241 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:24:13.642 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:24:13.941 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:24:14.641 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:24:14.842 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:24:15.341 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:24:16.041 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:24:16.042 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:24:16.741 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:24:17.242 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:24:17.441 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:24:18.141 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:24:18.442 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:24:18.841 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:24:19.541 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:24:19.642 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12]
02:24:20.241 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:24:20.842 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:24:20.941 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:24:21.641 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:24:22.042 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15]
02:24:22.341 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:24:23.041 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:24:23.044 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:24:23.046 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:24:23.047 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:24:23.048 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 1300ms - 17 ")
  void testPublisherAndBuffer2sShift1300ms() throws InterruptedException {
    getBuffer700ms(2_000, 1_300)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:24:38.790 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:24:39.493 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:24:40.192 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:24:40.799 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:24:40.892 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:24:41.592 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:24:42.094 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3]
02:24:42.292 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:24:42.992 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:24:43.393 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:24:43.692 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:24:44.392 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:24:44.693 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:24:45.092 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:24:45.792 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:24:45.993 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9]
02:24:46.492 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:24:47.192 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:24:47.293 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:24:47.892 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:24:48.592 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:24:48.593 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:24:49.292 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:24:49.893 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [12, 13, 14]
02:24:49.992 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:24:50.692 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:24:51.193 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16]
02:24:51.392 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:24:51.396 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:24:51.398 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:24:51.399 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 2100ms - drop 14 ")
  void testPublisherAndBuffer2sShift2100ms() throws InterruptedException {
    getBuffer700ms(2_000, 2_100)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:25:09.133 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:25:09.837 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:25:10.535 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:25:11.142 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:25:11.235 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:25:11.935 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:25:12.635 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:25:13.236 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4]
02:25:13.335 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:25:14.035 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:25:14.735 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:25:15.336 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7]
02:25:15.435 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:25:16.135 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:25:16.835 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:25:17.436 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10]
02:25:17.535 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:25:18.235 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:25:18.935 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:25:19.536 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]

02:25:19.635 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)

02:25:20.335 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:25:21.035 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:25:21.636 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16]
02:25:21.735 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:25:21.739 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:25:21.741 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:25:21.741 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 2300ms - drop 2, 12  ")
  void testDelay10sPublisherAndBuffer3sShift2300() throws InterruptedException {
    getBuffer700ms(2_000, 2_300)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:26:16.225 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:26:16.929 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:26:17.627 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:26:18.235 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:26:18.327 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:26:19.027 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:26:19.727 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:26:20.427 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:26:20.528 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:26:21.128 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:26:21.828 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:26:22.528 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:26:22.828 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8]
02:26:23.227 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:26:23.928 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:26:24.628 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:26:25.128 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11]
02:26:25.327 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:26:26.027 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:26:26.727 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:26:27.427 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:26:27.428 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15]
02:26:28.127 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:26:28.827 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:26:28.832 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:26:28.833 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:26:28.833 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
    */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 2초 모음, shift 2700ms - 2, 6, 10, 14 손실")
  void testDelay10sPublisherAndBuffer2sShift1() throws InterruptedException {
    getBuffer700ms(2000, 2_700)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:27:17.707 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:27:18.410 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:27:19.109 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:27:19.716 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1]
02:27:19.809 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:27:20.509 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:27:21.209 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:27:21.909 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:27:22.410 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5]
02:27:22.609 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:27:23.309 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:27:24.009 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:27:24.709 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:27:25.110 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9]
02:27:25.409 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:27:26.109 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:27:26.809 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:27:27.509 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:27:27.810 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13]
02:27:28.209 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:27:28.909 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:27:29.609 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:27:30.309 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:27:30.312 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:27:30.313 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:27:30.314 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 7초 모음, shift 500ms")
  void testDelay10sPublisherAndBuffer7sShift500ms() throws InterruptedException {
    getBuffer700ms(7000, 500)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:28:08.454 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:28:09.157 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:28:09.856 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:28:10.556 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:28:11.256 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:28:11.956 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:28:12.656 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:28:13.356 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:28:14.056 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:28:14.756 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:28:15.456 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:28:15.463 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
02:28:15.957 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
02:28:16.156 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:28:16.457 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
02:28:16.856 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:28:16.957 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
02:28:17.457 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
02:28:17.556 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:28:17.957 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
02:28:18.256 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:28:18.457 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
02:28:18.956 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:28:18.957 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
02:28:19.457 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
02:28:19.656 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:28:19.957 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
02:28:20.356 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:28:20.457 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
02:28:20.957 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
02:28:21.056 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:28:21.060 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:28:21.062 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
02:28:21.063 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [9, 10, 11, 12, 13, 14, 15, 16, 17]
02:28:21.063 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12, 13, 14, 15, 16, 17]
02:28:21.064 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [10, 11, 12, 13, 14, 15, 16, 17]
02:28:21.064 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12, 13, 14, 15, 16, 17]
02:28:21.065 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [12, 13, 14, 15, 16, 17]
02:28:21.065 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [12, 13, 14, 15, 16, 17]
02:28:21.066 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [13, 14, 15, 16, 17]
02:28:21.066 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16, 17]
02:28:21.066 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [14, 15, 16, 17]
02:28:21.067 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [15, 16, 17]
02:28:21.067 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [16, 17]
02:28:21.067 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:28:21.067 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:28:21.067 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 350ms 모음, shift 700ms - 손실 없음")
  void testPublisherAndBuffer7sShift500ms() throws InterruptedException {
    getBuffer700ms(350, 700)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:31:22.579 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:31:22.939 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : []
02:31:23.283 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:31:23.633 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [0]
02:31:23.982 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:31:24.333 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [1]
02:31:24.682 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:31:25.033 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [2]
02:31:25.382 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:31:25.733 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [3]
02:31:26.082 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:31:26.433 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [4]
02:31:26.782 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:31:27.133 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [5]
02:31:27.482 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:31:27.833 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [6]
02:31:28.182 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:31:28.533 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [7]
02:31:28.882 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:31:29.233 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [8]
02:31:29.582 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:31:29.933 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [9]
02:31:30.282 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:31:30.633 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [10]
02:31:30.982 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:31:31.333 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [11]
02:31:31.682 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:31:32.033 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [12]
02:31:32.382 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:31:32.733 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [13]
02:31:33.082 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:31:33.433 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [14]
02:31:33.782 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:31:34.133 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [15]
02:31:34.482 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:31:34.833 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [16]
02:31:35.182 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:31:35.185 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:31:35.187 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:31:35.187 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 350ms 모음, shift 1400ms - 손실 50%")
  void testPublisherAndBuffer350msShift1_400ms() throws InterruptedException {
    getBuffer700ms(350, 1_400)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);
    /*
02:34:12.604 [Test worker] INFO reactor.Flux.Take.1 - request(unbounded)
02:34:12.963 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : []
02:34:13.307 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:34:14.006 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:34:14.356 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [1]
02:34:14.706 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:34:15.406 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:34:15.757 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [3]
02:34:16.106 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:34:16.806 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:34:17.157 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [5]
02:34:17.506 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:34:18.206 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:34:18.556 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [7]
02:34:18.906 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:34:19.606 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:34:19.957 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [9]
02:34:20.306 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:34:21.006 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:34:21.357 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [11]
02:34:21.706 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:34:22.406 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:34:22.757 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [13]
02:34:23.106 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:34:23.806 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:34:24.157 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [15]
02:34:24.506 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:34:25.206 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:34:25.209 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:34:25.211 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [17]
02:34:25.211 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }

  @Test
  @DisplayName("띄엄띄엄(700ms) publisher, buffer 700ms 모음, shift 1400ms - 손실 발생")
  void testPublisherAndBuffer700msShift1_400ms() throws InterruptedException {
    getBuffer700ms(700, 1_400)
            .subscribe(intervalSubscriber);

    Thread.sleep(13_000);

    /*
02:35:41.251 [parallel-1] INFO reactor.Flux.Take.1 - onNext(0)
02:35:41.257 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [0]
02:35:41.950 [parallel-1] INFO reactor.Flux.Take.1 - onNext(1)
02:35:42.650 [parallel-1] INFO reactor.Flux.Take.1 - onNext(2)
02:35:42.651 [parallel-4] INFO org.psawesome.testFlux.BufferTest - onNext : [1]
02:35:43.350 [parallel-1] INFO reactor.Flux.Take.1 - onNext(3)
02:35:44.050 [parallel-1] INFO reactor.Flux.Take.1 - onNext(4)
02:35:44.051 [parallel-5] INFO org.psawesome.testFlux.BufferTest - onNext : [3]
02:35:44.750 [parallel-1] INFO reactor.Flux.Take.1 - onNext(5)
02:35:45.450 [parallel-1] INFO reactor.Flux.Take.1 - onNext(6)
02:35:45.451 [parallel-6] INFO org.psawesome.testFlux.BufferTest - onNext : [5, 6]
02:35:46.150 [parallel-1] INFO reactor.Flux.Take.1 - onNext(7)
02:35:46.850 [parallel-1] INFO reactor.Flux.Take.1 - onNext(8)
02:35:46.851 [parallel-7] INFO org.psawesome.testFlux.BufferTest - onNext : [7, 8]
02:35:47.550 [parallel-1] INFO reactor.Flux.Take.1 - onNext(9)
02:35:48.250 [parallel-1] INFO reactor.Flux.Take.1 - onNext(10)
02:35:48.251 [parallel-8] INFO org.psawesome.testFlux.BufferTest - onNext : [9]
02:35:48.950 [parallel-1] INFO reactor.Flux.Take.1 - onNext(11)
02:35:49.650 [parallel-1] INFO reactor.Flux.Take.1 - onNext(12)
02:35:49.651 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : [11, 12]
02:35:50.350 [parallel-1] INFO reactor.Flux.Take.1 - onNext(13)
02:35:51.050 [parallel-1] INFO reactor.Flux.Take.1 - onNext(14)
02:35:51.051 [parallel-2] INFO org.psawesome.testFlux.BufferTest - onNext : [14]
02:35:51.750 [parallel-1] INFO reactor.Flux.Take.1 - onNext(15)
02:35:52.450 [parallel-1] INFO reactor.Flux.Take.1 - onNext(16)
02:35:52.451 [parallel-3] INFO org.psawesome.testFlux.BufferTest - onNext : [16]
02:35:53.150 [parallel-1] INFO reactor.Flux.Take.1 - onNext(17)
02:35:53.152 [parallel-1] INFO reactor.Flux.Take.1 - onComplete()
02:35:53.153 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onNext : []
02:35:53.153 [parallel-1] INFO org.psawesome.testFlux.BufferTest - onComplete !!!
     */
  }


  private Flux<Integer> rangeFlux() {
    return Flux.range(1, 17);
  }

  private Flux<Long> intervalFluxAndLog() {
    return Flux.interval(Duration.ofMillis(700))
            .take(17 + 1)
            .log();
  }

  private Flux<Long> intervalFluxAndLog(int millis) {
    return Flux.interval(Duration.ofMillis(millis))
            .take(17 + 1)
            .log();
  }

  private Flux<Integer> delayElements1sRange17() {
    return this.rangeFlux().delayElements(Duration.ofSeconds(1));
  }

  private Flux<Integer> delayArgRange17(int millis) {
    return this.rangeFlux().delayElements(Duration.ofMillis(millis));
  }

  private Flux<List<Long>> getBuffer700ms(int timespan, int timeShift) {
    return intervalFluxAndLog()
            .buffer(Duration.of(timespan, ChronoUnit.MILLIS), Duration.ofMillis(timeShift));
  }

  private <T> BufferSubscriber<T> getSubscriber() {
    return new BufferSubscriber<>();
  }

  static class BufferSubscriber<T> extends BaseSubscriber<T> {
    @Override
    protected void hookOnNext(T value) {
      log.info("onNext : {}", value);
    }

    @Override
    protected void hookOnComplete() {
      log.info("onComplete !!!");
    }
  }

}
