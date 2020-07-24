package org.psawesome.chapters.chap02;

import org.psawesome.chapters.Temperature;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import java.io.IOException;

public class RxSeeEmitter extends SseEmitter {
  static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
  private final Subscriber<Temperature> subscriber;

  public RxSeeEmitter() {
    super(SSE_SESSION_TIMEOUT);
    this.subscriber = new Subscriber<>() {
      @Override
      public void onNext(Temperature temperature) {
        try {
          RxSeeEmitter.this.send(temperature);
        } catch (IOException e) {
          unsubscribe();
        }
      }

      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {

      }
    };

    onCompletion(subscriber::onCompleted);
    onTimeout(subscriber::unsubscribe);
  }

  Subscriber<Temperature> getSubscriber() {
    return subscriber;
  }
}
