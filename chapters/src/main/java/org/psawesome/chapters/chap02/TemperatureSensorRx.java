package org.psawesome.chapters.chap02;

import org.psawesome.chapters.Temperature;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureSensorRx {

  private final Observable<Temperature> dataStream =
          Observable
                  .range(0, Integer.MAX_VALUE)
                  .concatMap(tick -> Observable
                          .just(tick)
                          .delay(ThreadLocalRandom.current().nextInt(5000), TimeUnit.MILLISECONDS)
                          .map(tickVal -> this.probe())
                  ).publish()
                  .refCount();

  private Temperature probe() {
    return new Temperature(16 + ThreadLocalRandom.current().nextGaussian() * 10);
  }
  public Observable<Temperature> temperatureStream() {
    return dataStream;
  }
}
