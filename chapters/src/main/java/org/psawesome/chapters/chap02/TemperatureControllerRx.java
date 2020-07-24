package org.psawesome.chapters.chap02;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TemperatureControllerRx {
  private final TemperatureSensorRx temperatureSensorRx;

  public TemperatureControllerRx(TemperatureSensorRx temperatureSensorRx) {
    this.temperatureSensorRx = temperatureSensorRx;
  }

  @RequestMapping(value = "/temperature-stream-rx", method = RequestMethod.GET)
  public SseEmitter events(HttpServletRequest request) {
    final RxSeeEmitter emitter = new RxSeeEmitter();
    temperatureSensorRx.temperatureStream()
            .subscribe(emitter.getSubscriber());
    return emitter;
  }
}
