b-spring5-reactive
=

`Flux`와 `Mono`의 기능을 정리하는 저장소
=

# Flux

<hr/>

## `index` : 원소 열거

[Details Test code in this repository](essential/src/test/java/org/psawesome/testFlux/IndexAndTimestampTest.java)

    index 연산 추가 시
    Flux<Tuple2<Long, T>>로 다운스트림 된다.
    
- index()
    -
    
    ```java
    @Test
    void testIndex() {
        final Flux<Tuple2<Long, String>> index = getJust().index();
        index.subscribe(consumer ->
                         log.info("T1 = [{}], T2 = [{}]", 
                                  consumer.getT1(), 
                                  consumer.getT2())
                        );
    }
    ```
    
--- 

## `timestamp` : 원소 열거

[Details Test code in this repository](essential/src/test/java/org/psawesome/testFlux/IndexAndTimestampTest.java)

    timestamp 연산 추가 시 
    Flux<Tuple2<Long, T>>로 다운 스트림 된다.
    
- timestamp()
    -
      
    ```java
    @Test
    void testTimestamp() {
        final Flux<Tuple2<Long, String>> timestamp = 
                        Flux.just("a", "b", "c", "d", "e", "f", "g")
                            .timestamp();
        timestamp.subscribe(consumer -> 
                            log.info("t1 = [{}], t2 = [{}] ", 
                                      Instant.ofEpochMilli(consumer.getT1()), 
                                      consumer.getT2())
                            );
    }
    ```
<hr/>

## `concat` : 스트림 조합

[Details Test code in this repository](essential/src/test/java/org/psawesome/testFlux/IndexAndTimestampTest.java)
    
    n개의 스트림을 연결    
    
- concat(Publisher<? extends T>... sources)
    -

    ```java
    @Test
    void testConcat() {
        Flux.concat(
                Flux.range(1, 3),
                Flux.range(4, 2),
                Flux.range(6, 5),
                Flux.just("a", "b", "c", "d", "e")
        ).subscribe(System.out::println);
    }
    ```

---

## `merge` : 스트림 조합

[Buffer Duration - Reactor document](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#merge-int-org.reactivestreams.Publisher...-)

    여러 개의 다운 스트림을
    하나의 스트림으로 병합
    Flux<I> 반환
    
- merge(int prefetch, Publisher<? extends I>... sources)
    -
    
        구독중인 상위 스트림 소스는 
        parallel하다.

---

## `zip` : 스트림 조합

    스트림 조합으로
    worker thread 블록을 의미하는 zip
    구독하는 업스트림이 데이터를 모두 내려줄 때까지 대기
    
    
- zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2)
    -
    
        소스에서 정적인 메서드는 T6개의 source를 arg로 받는다.
        아래 예제는 두 개의 소스를 받아 zip
    
    ```java
    @Test
    void testZip() throws InterruptedException {
        final Flux<Integer> range = Flux.range(1, 7);
        final Flux<Long> interval = Flux.interval(Duration.ofMillis(700));
    
        final Flux<Tuple2<Integer, Long>> zip = Flux.zip(range, interval);
        zip.subscribe(System.out::println);
    }
    ```

<hr/>

## `window` : 원소 일괄처리
   
---   
    
## `group` : 원소 일괄처리

---

## `buffer` : 원소 일괄처리

[Buffer Duration - Reactor document](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#buffer-java.time.Duration-)

[Details Test code in this repository](essential/src/test/java/org/psawesome/testFlux/BufferTest.java)

    버퍼만큼 모아서 
    Flux<List<T>>로 반환
    

- buffer()
    -
        onComplete까지 모아서 반환

- buffer(size)
    - 
        size만큼 잘라서 반환
        
        onComplete일 경우 0이상 배열 반환
        
- buffer(size, skip)
    -
        size만큼 모으는데, skip만큼 건너뜀
        
        인자가 없을 때 size == skip으로 인식해도 무방
        
        size > skip : 데이터 중복
        size < skip : 데이터 손실
        
        ex)
        buffer(4, 7)
        list의 사이즈는 4이고, 원소는 7개 단위로 스킵하여 
        각 배열마다 3개씩 데이터 손실이 일어난다.
    
- buffer(Duration timespan)
    -
        Duration (시간, 기간)만큼 모아서 List<T>로 다운 스트림
    
- buffer(Duration timespan, Duration timeshift)
    -
        timespan (기간)만큼 모으는데, interval의 shift(이동)시킨다.
        
        ** interval **
        timespan > timeshift : 중복
        timespan < timeshift : 데이터 손실
        
        ** not interval **
        timespan == timeshfit : 중복, 손실이 없음


<hr/>

## `reduce` : 원소 줄이기

[Details Test code in this repository](essential/src/test/java/org/psawesome/testFlux/ReduceScanTest.java)

    accumulate (축적)연산
    Mono<T> 반환
    
- reduce(BiFunction<T, T, T> aggregator)
    -
        reduce((acc, n) -> acc + n)
        첫 번째는 0, 1 원소의 연산
        두 번째부터 이전(0+1) 연산의 결과와 2 원소의 연산으로 마지막 원소까지 iterator
        
        exam)
        각 숫자는 element를 나타냄
        0 + 1
        (0+1) + 2
        (0+1+2) + 3
        (0+1+2+3) + n

- reduce(A initial, BiFunction<A, ? super T, A> accumulator)
    -
        초기값 A를 받아 Mono<A>를 반환
        Flux.range(1, 3)
        .reduce(3, (acc, n) -> acc + n)
        
        exam)
        각 숫자는 값을 나타냄
        3 + 1
        (3 + 1 = 4) + 2
        (4 + 2 = 6) + 3
        result : Mono<Integer>이고, subscribe 출력은 9

---
        
## `scan` : 원소 줄이기

[Details Test code in this repository](essential/src/test/java/org/psawesome/testFlux/ReduceScanTest.java)

    accmulate 축적 연산
    Flux<T> 반환
    
    
- scan(BiFunction<T, T, T> accumulator)
    -
        최종은 축적된 반환을 얻을 수 있으며,
        다운 스트림으로 연산 과정을 흘려보낸다.
        
- scan(A initial, BiFunction<A, ? super T, A> accumulator)
    -
        초기값이 StringBuilder 인스턴스를 생성하기 때문에
        아무것도 출력되지 않았다.
        
    ```java
        @Test
        void testScanAccStringBuilder() {
            Flux.range(1, 7)
                    .scan(new StringBuffer(), StringBuffer::append)
                    .subscribe(consumer -> log.info(consumer.toString()));
        }
    ```        
        
<hr/>
