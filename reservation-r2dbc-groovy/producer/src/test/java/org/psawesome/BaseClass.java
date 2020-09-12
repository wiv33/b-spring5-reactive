package org.psawesome;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import(ReservationHttpConfiguration.class)
public class BaseClass {

  @LocalServerPort
  int port;

  @MockBean
  ReservationRepository reservationRepository;

  @Test
  void firstTest() {

    RestAssured.baseURI = "http://localhost:" + port;
    Mockito.when(this.reservationRepository.findAll())
            .thenReturn(Flux.just(new Reservation(1, "ps")));

  }
}
