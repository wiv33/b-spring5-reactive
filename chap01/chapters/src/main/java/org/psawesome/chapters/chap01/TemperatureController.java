package org.psawesome.chapters.chap01;

import org.psawesome.chapters.Temperature;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ps [https://github.com/wiv33/chapters]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 7. 20. Monday
 */
@RestController
public class TemperatureController {
  private final Set<SseEmitter> clients;

  public TemperatureController() {
    clients = new CopyOnWriteArraySet<>();
  }

  @RequestMapping(value = "/temperature-stream", method = RequestMethod.GET)
  public SseEmitter events(HttpServletRequest request) {
    final SseEmitter emitter = new SseEmitter();
    clients.add(emitter);

    emitter.onTimeout(() -> clients.remove(emitter));
    emitter.onCompletion(() -> clients.remove(emitter));
    return emitter;
  }

  @Async
  @EventListener
  public void handleMessage(Temperature temperature) {
    List<SseEmitter> deadEmitters = new ArrayList<>();
    clients.forEach(emitter -> {
      try {
        emitter.send(temperature, MediaType.APPLICATION_JSON);
      } catch (IOException e) {
        deadEmitters.add(emitter);
      }
    });
    clients.removeAll(deadEmitters);
  }
}
