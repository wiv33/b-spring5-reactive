package org.psawesome;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

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
@Configuration
class ReservationHttpConfiguration {

  @Bean
  RouterFunction<ServerResponse> reservationRouter(ReservationRepository rr) {
    return route()
            .GET("/reservation", request ->
                    ServerResponse.ok().body(BodyInserters.fromPublisher(rr.findAll(), Reservation.class)))
            .build();
  }
}
