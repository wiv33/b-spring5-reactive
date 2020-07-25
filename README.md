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
        
- buffer(size, skip)
    -
        size만큼 모으는데, skip만큼 건너뜀
        
        인자가 없을 때 size == skip으로 인식해도 무방
        
        size > skip : 데이터 중복
        size < skip : 데이터 손실
    
- buffer(Duration timespan, Duration timeshift)
    -
    
        timespan,  
