b-spring5-reactive
=

`Flux`와 `Mono`의 기능을 정리하는 저장소
=

# Flux

## `buffer`

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

---

## `reduce`

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
        
## `scan`

    accmulate 축적 연산
    Flux<T> 반환
    
    
- scan(BiFunction<T, T, T> accumulator)
    -
        최종은 축적된 반환을 얻을 수 있으며,
        다운 스트림으로 연산 과정을 흘려보낸다.
        
        (아래 출력을 참고)

        
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
        
        subscribe 출력 형태가 계단으로 출력
        15:18:44.629 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 1
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 12
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 123
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 1234
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 12345
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 123456
        15:18:44.630 [Test worker] INFO org.psawesome.testFlux.ReduceScanTest - 1234567

