package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 12. Saturday
 */
@RequiredArgsConstructor
@Component
public class ReservationClient {


  final WebClient client;

  public Flux<Reservation> getAll() {
    return this.client.get()
            .uri("http://localhost:8080/reservations")
            .retrieve()
            .bodyToFlux(Reservation.class);
  }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Reservation {

  private Integer id;
  private String name;
}
